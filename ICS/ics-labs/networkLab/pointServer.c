//
// Created by Bing Chen on 2018/6/20.
//
#include "csapp.h"

#define BUFLENTH 255
int listenFd;//服务器端文件描述符
int length = 10;
int clientFds[10];//存储客户端文件描述符
char *clientIp[10];//存储客户端IP
char *serverIp = "127.0.0.5";//服务器端IP
char *errorMassage = "Please enter the format massage";
char *noClientMassage = "There is no other client: ";
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

//根据传入的IP向对应的IP发送消息
void sendMassage(char *massage, char *targetIp, int fromFd) {
    int i;
    int flag = 0;
    for (i = 0; i < length; i++) {
//        判断是否存在对应的IP，如果有，则发送消息
        if (clientFds[i] != 0 && clientFds[i] != fromFd && strcmp(clientIp[i], targetIp) == 0) {
            flag = 1;
            char formatMassage[BUFLENTH];
            memset(formatMassage, 0, sizeof(formatMassage));
            sprintf(formatMassage, "%s", massage);
            sprintf(massage, "%s: %s", clientIp[i], formatMassage);
            printf("sent massage to : %s\n", clientIp[i]);
            send(clientFds[i], massage, strlen(massage), 0);
        }
    }
//    不存在目标IP时，向发送方返回错误消息
    if (!flag) {
        sprintf(massage, "%s%s", noClientMassage, targetIp);
        send(fromFd, massage, strlen(massage), 0);
    }
}

//服务器端服务线程，每个线程对应一个客户端
void *service_thread(void *pVoid) {
    int fd = (int) pVoid;
    pthread_detach(pthread_self());
    char buf[BUFLENTH];
    char targetIp[BUFLENTH];
    char massage[BUFLENTH];
    char *exitStr = "exit";
//    进入线程，不断等待接受客户端消息
    while (1) {
        memset(buf, 0, sizeof(buf));
        memset(targetIp, 0, sizeof(targetIp));
        memset(massage, 0, sizeof(massage));
        if (recv(fd, buf, sizeof(buf), 0) <= 0) {
            int i;
            for (i = 0; i < length; i++) {
                if (clientFds[i] == fd) {
                    clientFds[i] = 0;
                    clientIp[i] = "";
                    close(fd);
                    break;
                }
            }
            pthread_exit(NULL);
        }
//        由于客户端发送消息通常包含换行键，去除
        char *p;
        if ((p = strchr(buf, '\n')) != NULL) {
            *p = '\0';
        }
//        判断是否是退出命令
        if (strcmp(exitStr, buf) == 0) {
            int i;
            for (i = 0; i < length; i++) {
                if (clientFds[i] == fd) {
                    clientFds[i] = 0;
                    clientIp[i] = "";
                    close(fd);
                    break;
                }
            }
            pthread_exit(NULL);
        }
//        判断是否符合IP:massage的格式，不符合则返回错误消息给客户端
        if ((p = strchr(buf, ':')) == NULL) {
            send(fd, errorMassage, strlen(errorMassage), 0);
            continue;
        }
        *p = '\0';
        strcpy(targetIp, buf);
        strcpy(massage, p + 1);
        sendMassage(massage, targetIp, fd);
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
//        将客户端信息存储下来
        for (i = 0; i < length; i++) {
            if (clientFds[i] == 0) {
                clientFds[i] = fd;
                getnameinfo((SA *) &fromAddress, len, fromIp, sizeof(fromIp), NULL, 0, flags);
                printf("Connect to: %s\n", fromIp);
                clientIp[i] = fromIp;
                pthread_t tid;
//                开启新的线程为客户端服务
                pthread_create(&tid, NULL, service_thread, (void *) fd);
                break;
            }
        }
    }
}

int main() {
    setListenFd();
    startService();
    close(listenFd);
    exit(0);
}