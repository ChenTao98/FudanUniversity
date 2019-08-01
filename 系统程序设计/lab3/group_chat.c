//
// Created by chen on 19-5-25.
//
#include <stdlib.h>
#include <stdio.h>
#include <sys/sem.h>
#include <sys/shm.h>
#include <unistd.h>
#include <time.h>
#include <string.h>
#include <signal.h>
#include <sys/wait.h>

#define MSG_SIZE 512 //消息长度
#define TIME_SIZE 32 //时间长度
#define NAME_SIZE 64 //用户名字长度
#define PATH_SHARE "share.txt" //共享内存path
#define PATH_SEM "sem.txt"  //信号量path
#define PRO_ID_SHARE 20  //共享内存pro—id
#define PRO_ID_SEM 30   //信号量pro-id

//消息结构体，共享内存结构也和这个一样
typedef struct massage {
    int read_tag;//读取标志，只有在本地标志与共享标志相等时才可以读取
    pid_t pid;//pid，写入本次消息的pid
    char time[TIME_SIZE];//消息发送的时间
    char name[NAME_SIZE];//发送消息的用户名称
    char message[MSG_SIZE];//消息的主体内容
} my_msg;

pid_t pid;
int sem_id = -1;//信号量id
int share_id = -1;//共享内存id
my_msg *share_point;//共享内存指针
struct shmid_ds shmid_ds_;//共享内存管理结构
//父进程SIGIINT处理事件
void sig_handle_parent(int signo) {
    //给子进程发送SIGINT信号
    kill(pid, SIGINT);
    wait(NULL);
    //解除共享内存绑定
    shmdt((void *) share_point);
    shmctl(share_id, IPC_STAT, &shmid_ds_);
    //如果没有其他进程与共享内存绑定，清除共享内存
    if (shmid_ds_.shm_nattch == 0) {
        shmctl(share_id, IPC_RMID, NULL);
        semctl(sem_id, 0, IPC_RMID, 0);
    }
    exit(0);
}

//子进程SIGINT处理事件，解除共享内存绑定
void sig_handle_child(int signo) {
    shmdt((void *) share_point);
    exit(0);
}

//初始化信号量
void init_sem() {
    key_t sem_key;
    if ((sem_key = ftok(PATH_SEM, PRO_ID_SEM)) < 0) {
        printf("ftok生成sem_key失败\n");
        exit(0);
    }
    if ((sem_id = semget(sem_key, 0, 00777)) == -1) {
        if ((sem_id = semget(sem_key, 1, IPC_CREAT | IPC_EXCL | 00777)) == -1) {
            printf("生成信号量失败\n");
            exit(0);
        } else {
            if (semctl(sem_id, 0, SETVAL, 1) == -1) {
                printf("初始化信号量内存失败\n");
                exit(0);
            }
        }
    }
}

//初始化共享内存
void init_share() {
    int flag = -1;
    key_t share_key;
    if ((share_key = ftok(PATH_SHARE, PRO_ID_SHARE)) < 0) {
        printf("ftok生成share_key失败\n");
        exit(0);
    }

    if ((share_id = shmget(share_key, sizeof(my_msg), 00777)) == -1) {
        if ((share_id = shmget(share_key, sizeof(my_msg), IPC_CREAT | IPC_EXCL | 00777)) == -1) {
            printf("生成共享内存失败\n");
            exit(0);
        } else {
            flag = 1;
        }
    }
    if ((share_point = (my_msg *) shmat(share_id, 0, 0)) == -1) {
        printf("映射共享内存失败\n");
        exit(0);
    }
    if (flag == 1) {
        memset(share_point, 0, sizeof(my_msg));
    }
}

//获取资源，即信号量减1
void sem_get() {
    struct sembuf sem_buf;
    sem_buf.sem_num = 0;
    sem_buf.sem_op = -1;
    sem_buf.sem_flg = SEM_UNDO;
    semop(sem_id, &sem_buf, 1);
}

//释放资源，即信号量加1
void sem_release() {
    struct sembuf sem_buf;
    sem_buf.sem_num = 0;
    sem_buf.sem_op = +1;
    sem_buf.sem_flg = SEM_UNDO;
    semop(sem_id, &sem_buf, 1);
}

int main(int argc, char **argv) {
    int read_tag;//读取标志，判断当前消息是否要读取
    my_msg msg_send;//发送消息结构
    time_t t;//时间结构
    struct tm *time_p;//时间指针
    pid_t ppid;
    //判断是否用户名符合要求
    if (argc != 2) {
        printf("用法：%s <用户名称，例如： A>\n", argv[1]);
        exit(0);
    }
    if (strlen(argv[1]) > NAME_SIZE) {
        printf("用户名长度必须小于%d\n", NAME_SIZE);
        exit(0);
    }
    //初始化信号量与共享内存
    init_sem();
    init_share();

    //打印登录成功消息
    time(&t);
    time_p = localtime(&t);
    printf("%d-%d-%d %d:%d:%d %s 登录成功\n", 1900 + time_p->tm_year, 1 + time_p->tm_mon,
           time_p->tm_mday, time_p->tm_hour, time_p->tm_min, time_p->tm_sec, argv[1]);

    //父子进程协作
    if ((pid = fork()) == 0) {
        signal(SIGINT, sig_handle_child);
        //设置read_tag为共享内存下次的read_tag
        read_tag = share_point->read_tag + 1;
        ppid = getppid();
        while (1) {
            sleep(0.5);
            //判断read_tag是否和本身read_tag相同，是的话说明有新的消息
            if (share_point->read_tag == read_tag) {
                sem_get();
                read_tag = read_tag + 1;
                //如果pid与父进程pid不同才打印消息
                if (share_point->pid != ppid) {
                    printf("%s %s说: %s", share_point->time, share_point->name, share_point->message);
                }
                sem_release();
            }
        }
    } else {
        //初始化name，pid，设置SIGINT处理事件
        signal(SIGINT, sig_handle_parent);
        memset(msg_send.name, 0, NAME_SIZE);
        memcpy(msg_send.name, argv[1], strlen(argv[1]));
        msg_send.pid = getpid();
        while (1) {
            //等待输入消息
            memset(msg_send.time, 0, TIME_SIZE);
            memset(msg_send.message, 0, MSG_SIZE);
            fgets(msg_send.message, MSG_SIZE, stdin);
            fflush(stdin);
            //获取信号量
            sem_get();
            //设置时间
            time(&t);
            time_p = localtime(&t);
            sprintf(msg_send.time, "%d-%d-%d %d:%d:%d", 1900 + time_p->tm_year, 1 + time_p->tm_mon,
                    time_p->tm_mday, time_p->tm_hour, time_p->tm_min, time_p->tm_sec);
            //将tag加1，复制到共享内存
            msg_send.read_tag = share_point->read_tag + 1;
            memset(share_point, 0, sizeof(my_msg));
            memcpy(share_point, &msg_send, sizeof(my_msg));
            //打印发送成功
            printf("%d-%d-%d %d:%d:%d 发送成功\n", 1900 + time_p->tm_year, 1 + time_p->tm_mon,
                   time_p->tm_mday, time_p->tm_hour, time_p->tm_min, time_p->tm_sec);
            //释放信号量
            sem_release();
        }
    }
}
