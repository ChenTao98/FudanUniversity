//
// Created by Bing Chen on 2018/6/21.
//
#include "csapp.h"

#define BUFLENGTH 255
int clientFd;
char *hostIp = "127.0.0.5";//服务器端IP
short port = 15123;//服务器端端口
char *name;//存储客户端姓名
char *roomFull = "Sorry,the chat room is full";
typedef struct sockaddr SA;

void *receiveMassage_thread(void *);

//设置客户端的socket，并连接服务器端
void setClientFd() {
    clientFd = socket(AF_INET, SOCK_STREAM, 0);
    struct sockaddr_in address;
    address.sin_family = AF_INET;
    address.sin_port = htons(port);
    address.sin_addr.s_addr = inet_addr(hostIp);
    if (connect(clientFd, (SA *) &address, sizeof(address)) == -1) {
        perror("无法连接到服务器");
        exit(-1);
    }
    printf("客户端启动成功\n");
}

//开始客户端服务
void startClient() {
    pthread_t tid;
    //    设置线程，并发执行
    pthread_create(&tid, NULL, receiveMassage_thread, NULL);
    char buf[BUFLENGTH];
    char massage[BUFLENGTH];
//    发送消息，告诉其他人当前客户端进入聊天室
    memset(buf, 0, sizeof(buf));
    sprintf(buf, "%s enter the chat room", name);
    send(clientFd, buf, strlen(buf), 0);
//    循环等待客户端输入消息
    while (1) {
        memset(buf, 0, sizeof(buf));
        memset(massage, 0, sizeof(massage));
        fgets(buf, BUFLENGTH, stdin);
        fflush(stdin);
//        判断如果是exit指令，退出客户端
        if (strcmp(buf, "exit\n") == 0) {
            memset(buf, 0, sizeof(buf));
            sprintf(buf, "%s :exitboom93", name);
            send(clientFd, buf, strlen(buf), 0);
            break;
        }
//        发送包含姓名的消息到服务器端
        sprintf(massage, "%s: %s", name, buf);
        send(clientFd, massage, strlen(massage), 0);
    }
    close(clientFd);
}

//接受服务器端发送来的消息的线程
void *receiveMassage_thread(void *pVoid) {
    char buf[BUFLENGTH];
    pthread_detach(pthread_self());
    while (1) {
        memset(buf, 0, sizeof(buf));
        if (recv(clientFd, buf, sizeof(buf), 0) <= 0) {
            printf("接收消息失败\n");
            close(clientFd);
            exit(0);
        }
//        判断聊天室是否已满
        if (strcmp(roomFull, buf) == 0) {
            printf("%s\n", buf);
            fflush(stdout);
            close(clientFd);
            exit(0);
        }
        printf("%s\n", buf);
        fflush(stdout);
    }
}

//主函数，输入参数为用户姓名
int main(int argc, char **argv) {
//    判断参数个数，如果不为2，打印消息，退出
    if (argc != 2) {
        printf("Start client fail: usage:%s <name>\n", argv[0]);
        exit(0);
    }
//    默认第二个参数为用户姓名
    name = argv[1];
    setClientFd();
    startClient();
    exit(0);
}
