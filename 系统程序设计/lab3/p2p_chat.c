//
// Created by chen on 19-5-25.
//

#include <stdio.h>
#include <stdlib.h>
#include <sys/msg.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include <wait.h>
#include <time.h>

#define IS_CLIENT_TRUE 1  //表示用户是允许的客户端
#define CLIENT_A 1        //A的send—type
#define CLIENT_B 2        //B的send—type
#define CLIENT_C 3        //C的send—type
#define CLIENT_D 4        //D的send—type
#define FTOK_PATH "msg.txt"  //ftok的path
#define PRO_ID 10            //ftok的pro_id
#define MSG_SIZE 512         //消息队列中消息大小
#define TIME_SIZE 32       //屏幕上获取信息的消息大小

pid_t pid;
int msqid;
//消息结构体，包含类型、发送时间、消息
typedef struct msg_struct {
    long type;//消息类型
    char time[TIME_SIZE];//发送消息的时间
    char message[MSG_SIZE];//消息内容
} my_msg;

//SIGINT处理函数，先杀死子进程，再退出父进程
void sig_handle_parent(int signo) {
    kill(pid, SIGKILL);
    wait(NULL);
    exit(0);
}

int main(int argc, char **argv) {
    long send_type;//发送类型
    long recv_type;//接受类型
    key_t key = -1;
    int is_client = 0;//是否是正确的客户端
    my_msg msg_recv; //接受的消息
    my_msg msg_send; //发送的消息
    time_t t;//时间结构
    struct tm *time_p;//时间指针


    if (argc != 2) {
        printf("使用方法:%s <用户，例如 A>\n", argv[0]);
        exit(0);
    }
    //判断是哪个用户
    //设定特定发送与接收消息类型
    if (strcmp(argv[1], "A") == 0) {
        send_type = CLIENT_A;
        recv_type = CLIENT_B;
        is_client = IS_CLIENT_TRUE;
    }
    if (strcmp(argv[1], "B") == 0) {
        send_type = CLIENT_B;
        recv_type = CLIENT_A;
        is_client = IS_CLIENT_TRUE;
    }
    if (strcmp(argv[1], "C") == 0) {
        send_type = CLIENT_C;
        recv_type = CLIENT_D;
        is_client = IS_CLIENT_TRUE;
    }
    if (strcmp(argv[1], "D") == 0) {
        send_type = CLIENT_D;
        recv_type = CLIENT_C;
        is_client = IS_CLIENT_TRUE;
    }
    if (is_client != IS_CLIENT_TRUE) {
        printf("用户必须是：A、B、C、D中的一个\n");
        exit(0);
    }
    if ((key = ftok(FTOK_PATH, PRO_ID)) < 0) {
        printf("ftok生成key失败\n");
        exit(0);
    }
    if ((msqid = msgget(key, 00777)) == -1) {
        if ((msqid = msgget(key, IPC_CREAT | IPC_EXCL | 00777)) == -1) {
            printf("创建message queue失败\n");
            exit(0);
        }
    }
    //输出登录成功消息
    time(&t);
    time_p = localtime(&t);
    printf("%d-%d-%d %d:%d:%d %s 登录成功\n", 1900 + time_p->tm_year, 1 + time_p->tm_mon,
           time_p->tm_mday, time_p->tm_hour, time_p->tm_min, time_p->tm_sec, argv[1]);
    msg_send.type = send_type;//确定发送消息类型
    //创建子进程开始接收消息
    if ((pid = fork()) == 0) {
        while (1) {
            //每次清空上次的消息
            memset(&msg_recv, 0, sizeof(my_msg));
            //接收与打印消息
            if (msgrcv(msqid, &msg_recv, MSG_SIZE + TIME_SIZE, recv_type, 0) != -1) {
                printf("接收：%s %s", msg_recv.time, msg_recv.message);
            }
        }
    } else {
        //设置SIGINT处理函数
        signal(SIGINT, sig_handle_parent);
        while (1) {
            //清空消息结构
            memset(msg_send.message, 0, MSG_SIZE);
            memset(msg_send.time, 0, TIME_SIZE);
            //阻塞等待消息
            fgets(msg_send.message, MSG_SIZE, stdin);
            fflush(stdin);
            //设置时间
            time(&t);
            time_p = localtime(&t);
            sprintf(msg_send.time, "%d-%d-%d %d:%d:%d", 1900 + time_p->tm_year, 1 + time_p->tm_mon,
                    time_p->tm_mday, time_p->tm_hour, time_p->tm_min, time_p->tm_sec);
            //发送消息
            msgsnd(msqid, &msg_send, MSG_SIZE + TIME_SIZE, 0);
            printf("%d-%d-%d %d:%d:%d 发送成功\n", 1900 + time_p->tm_year, 1 + time_p->tm_mon,
                   time_p->tm_mday, time_p->tm_hour, time_p->tm_min, time_p->tm_sec);
        }
    }
}