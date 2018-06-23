//
// Created by Bing Chen on 2018/6/21.
//
#include "csapp.h"

#define BUFLENTH 255
int listenFd;//服务器端文件描述符
int length = 100;
int clientFds[100];
char *serverIp = "127.0.0.5";//服务器端IP
char *noOtherClient = "There is no other client";
char *roomFull = "Sorry,the chat room is full";
short port = 15123;//服务器端端口号
typedef struct sockaddr SA;

//设置服务器端监听描述符，并绑定
void setListenFd() {
    listenFd = socket(AF_INET, SOCK_STREAM, 0);
    if (listenFd == -1) {
        perror("创建socket失败");
        exit(-1);
    }
    struct sockaddr_in address;
    address.sin_family = AF_INET;
    address.sin_port = htons(port);
    address.sin_addr.s_addr = inet_addr(serverIp);
    if (bind(listenFd, (SA *) &address, sizeof(address)) == -1) {
        perror("绑定失败");
        exit(-1);
    }
    if (listen(listenFd, length) == -1) {
        perror("设置监听失败");
        exit(-1);
    }
}

//将消息发送给所有客户端（除了消息发送方）
void sendMassage(char *massage, int fromFd) {
    int i;
    int count = 0;
    if (strcmp(massage, roomFull) == 0) {
        sprintf(massage, "%s ", massage);
    }
//    向客户端发送消息
    for (i = 0; i < length; i++) {
        if (clientFds[i] != 0 && clientFds[i] != fromFd) {
            count++;
            send(clientFds[i], massage, strlen(massage), 0);
        }
    }
//    判断是否发送给其他客户端，有则服务器端打印出发送给几个客户端，没有则发送错误消息给发送方
    if (count != 0) {
        printf("send massage success to %d clients\n", count);
    } else {
        send(fromFd, noOtherClient, strlen(noOtherClient), 0);
    }
}

//服务器端服务线程，每个线程对应一个客户端
void *service_thread(void *pVoid) {
    int fd = (int) pVoid;
    pthread_detach(pthread_self());
    char buf[BUFLENTH];
    char *exitStr = ":exitboom93";
//    进入线程，不断等待接受客户端消息
    while (1) {
        memset(buf, 0, sizeof(buf));
        if (recv(fd, buf, sizeof(buf), 0) <= 0) {
            int i;
            for (i = 0; i < length; i++) {
                if (clientFds[i] == fd) {
                    clientFds[i] = 0;
                    close(fd);
                    break;
                }
            }
            pthread_exit(NULL);
        }
//        判断是否是退出指令
        char *p;
        if ((p = strstr(buf, exitStr)) != NULL) {
            *p = '\0';
            char massage[BUFLENTH];
            memset(massage, 0, sizeof(massage));
            strcpy(massage, buf);
            sprintf(massage, "%sleave the chat room", massage);
            int i;
            for (i = 0; i < length; i++) {
                if (clientFds[i] == fd) {
                    clientFds[i] = 0;
                    close(fd);
                    break;
                }
            }
            sendMassage(massage, fd);
            pthread_exit(NULL);
        }
        if ((p = strchr(buf, '\n')) != NULL) {
            *p = '\0';
        }
        sendMassage(buf, fd);
    }
}

//服务器端开始服务
void startService() {
    printf("服务器启动\n");
    while (1) {
        char fromIp[BUFLENTH];
        struct sockaddr_in fromAddress;
        socklen_t len = sizeof(fromAddress);
//        阻塞等待客户端连接请求
        int fd = accept(listenFd, (SA *) &fromAddress, &len);
        int flags = NI_NUMERICHOST;
        if (fd == -1) {
            printf("客户端连接出错...\n");
            continue;
        }
        int i = 0;
        int flag = 0;
        for (i = 0; i < length; i++) {
            if (clientFds[i] == 0) {
                clientFds[i] = fd;
                getnameinfo((SA *) &fromAddress, len, fromIp, sizeof(fromIp), NULL, 0, flags);
                printf("Connect to: %s\n", fromIp);
                pthread_t tid;
//                开启新的线程为新客户端服务
                pthread_create(&tid, NULL, service_thread, (void *) fd);
                flag = 1;
                break;
            }
        }
        if (flag == 0) {
            send(fd, roomFull, strlen(roomFull), 0);
            close(fd);
        }
    }
}

int main() {
    setListenFd();
    startService();
    close(listenFd);
    exit(0);
}
