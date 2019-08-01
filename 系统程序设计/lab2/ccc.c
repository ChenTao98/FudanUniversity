//
// Created by chen on 19-5-19.
//
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>

int i = 2;
pid_t ppid;
//信号处理函数，打印信息并且发送信号给父进程
void sig_handle(int signo) {
    printf("c%d%d%d%d%d%d%d%d\n", i, i, i, i, i, i, i, i);
    i += 2;
    signal(SIGUSR1, sig_handle);
    kill(ppid,SIGUSR1);
}

int main(int argc, char **argv) {
    sigset_t wait;
    ppid = getppid();
    sigemptyset(&wait);
    signal(SIGUSR1, sig_handle);
    //sigsuspend
    while (i < 9) {
        if (sigsuspend(&wait) != -1) {
            printf("ccc.c中sigsuspend出错");
            exit(0);
        }
    }
}
