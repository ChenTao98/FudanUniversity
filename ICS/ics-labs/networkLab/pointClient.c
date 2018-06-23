//
// Created by Bing Chen on 2018/6/20.
//
#include "csapp.h"

#define BUFLENTH 255
int clientFd;
char *hostIp = "127.0.0.5";//服务器端IP
short port = 15123;//服务器端端口
typedef struct sockaddr SA;

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
    void *receiveMassage_thread(void *);
    pthread_create(&tid, NULL, receiveMassage_thread, NULL);
    char buf[BUFLENTH];
//    等待客户端输入消息并发送到服务器端
    while (1) {
        memset(buf, 0, sizeof(buf));
        fgets(buf, BUFLENTH, stdin);
        fflush(stdin);
        send(clientFd, buf, strlen(buf), 0);
//        如果客户端输入exit，则退出客户端服务
        if (strcmp(buf, "exit\n") == 0) {
            break;
        }
    }
    close(clientFd);
}

//接受服务器端发送来的消息的线程
void *receiveMassage_thread(void *pVoid) {
    char buf[BUFLENTH];
    pthread_detach(pthread_self());
    while (1) {
        memset(buf, 0, sizeof(buf));
        if (recv(clientFd, buf, sizeof(buf), 0) <= 0) {
            printf("接收消息失败");
            pthread_exit(NULL);
        }
        printf("%s\n", buf);
        fflush(stdout);
    }
}

int main() {
    setClientFd();
    startClient();
    exit(0);
}