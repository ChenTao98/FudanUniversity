//
// Created by chen on 19-5-19.
//
#include <stdio.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/wait.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/mman.h>

char *buf2;
//信号处理事件，根据信号类型的不同输出不同语句并退出
void sig_handler(int signo) {
    if (signo == SIGFPE) {
        printf("捕捉到SIGFPE信号，程序发生错误\n");
    } else if (signo == SIGSEGV) {
        printf("捕捉到SIGSEGV信号，程序发生错误\n");
    } else if (signo == SIGILL) {
        printf("捕捉到SIGILL信号，程序发生错误\n");
    } else if (signo == SIGABRT) {
        printf("捕捉到SIGABRT信号，程序发生错误\n");
    } else if (signo == SIGBUS) {
        printf("捕捉到SIGBUS信号，程序发生错误\n");
    } else if (signo == SIGPIPE) {
        printf("捕捉到SIGPIPE信号，程序发生错误\n");
    }
    exit(0);
}

//以下是产生不同信号的函数，有的执行代码产生，有的不知道如何尝试
//就直接采用raise函数
void sigfpe() {
    int a = 10 / 0;
}

void sigsegv() {
    int *p = NULL;
    int a = *p;
}

void sigill() {
//    __asm__("pushq %rsp");
    raise(SIGILL);
}

void sigabrt() {
    abort();
}

void sigbus() {
    int fd = open("1.txt", O_RDONLY);
    int fd2 = open("2.txt", O_CREAT | O_RDWR | O_TRUNC, 00777); //mmap进行文件映射时必须先读取文件`
    struct stat st;
    fstat(fd, &st);
    void *_src = mmap(NULL, st.st_size, PROT_READ, MAP_SHARED, fd, 0);
    void *_des = mmap(NULL, 1, PROT_WRITE, MAP_SHARED, fd2, 0);
    close(fd);
    memcpy(_des, _src, st.st_size);
}

void sigpipe() {
    struct sockaddr_in address;
    struct sockaddr_in seraddr;
    int fd;
    int clientFd;
    typedef struct sockaddr SA;
    int listenFd;//服务器端文件描述符
    int length = 10;
    char *serverIp = "127.0.0.5";//服务器端IP
    short port = 15123;//服务器端端口号
    if (fork() == 0) {
        sleep(2);
        clientFd = socket(AF_INET, SOCK_STREAM, 0);
        address.sin_family = AF_INET;
        address.sin_port = htons(port);
        address.sin_addr.s_addr = inet_addr(serverIp);
        if (connect(clientFd, (SA *) &address, sizeof(address)) == -1) {
            perror("无法连接到服务器");
            exit(-1);
        }
        printf("客户端启动成功\n");
        exit(0);
    } else {
        listenFd = socket(AF_INET, SOCK_STREAM, 0);
        if (listenFd == -1) {
            perror("创建socket失败");
            exit(-1);
        }
        seraddr.sin_family = AF_INET;
        seraddr.sin_port = htons(port);
        seraddr.sin_addr.s_addr = inet_addr(serverIp);
        if (bind(listenFd, (SA *) &seraddr, sizeof(seraddr)) == -1) {
            perror("绑定失败");
            exit(-1);
        }
        if (listen(listenFd, length) == -1) {
            perror("设置监听失败");
            exit(-1);
        }
//        阻塞等待客户端连接请求
        fd = accept(listenFd, NULL, NULL);
        if (fd == -1) {
            printf("客户端连接出错...\n");
            exit(0);
        }
        while (1) {
            sleep(1);
            printf("hello\n");
            write(fd, "hello", sizeof("hello"));
        }
    }
}

int main(int argc, char **argv) {
    char buf[2];
    buf2 = buf;
    //绑定信号处理事件
    signal(SIGFPE, sig_handler);
    signal(SIGSEGV, sig_handler);
    signal(SIGILL, sig_handler);
    signal(SIGABRT, sig_handler);
    signal(SIGBUS, sig_handler);
    signal(SIGPIPE, sig_handler);
    if (argc != 2) {
        printf("usage:%s <信号，如 SIGFPE>\n", argv[0]);
        exit(0);
    }
    //判断参数执行不同的产生信号的函数
    if (strcmp(argv[1], "SIGFPE") == 0) {
        sigfpe();
    } else if (strcmp(argv[1], "SIGSEGV") == 0) {
        sigsegv();
    } else if (strcmp(argv[1], "SIGILL") == 0) {
        sigill();
    } else if (strcmp(argv[1], "SIGABRT") == 0) {
        sigabrt();
    } else if (strcmp(argv[1], "SIGBUS") == 0) {
        sigbus();
    } else if (strcmp(argv[1], "SIGPIPE") == 0) {
        sigpipe();
    }
    printf("该信号未在该程序实现捕捉\n");
    return 0;
}
