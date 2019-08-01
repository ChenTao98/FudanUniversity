//
// Created by chen on 19-5-19.
//
#include <stdio.h>
#include <signal.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <ctype.h>

int i = 1;
pid_t cpid;
int top = 8;
//信号处理函数，打印信息并且发生信号给子进程
void sig_handle(int signo) {
    if (i < top) {
        printf("b%d%d%d%d%d%d%d%d\n", i, i, i, i, i, i, i, i);
        i += 2;
        signal(SIGUSR1, sig_handle);
        kill(cpid, SIGUSR1);
    }
}

int main() {
    char *argv[] = {"./ccc", 0};
    signal(SIGUSR1, sig_handle);
    sigset_t waitSet;
    sigemptyset(&waitSet);
    //创建子进程
    if ((cpid = fork()) == 0) {
        //子进程运行ccc程序
        if (execvp("./ccc", argv) == -1) {
            printf("execvp出错\n");
        }
    } else {
        sleep(1);
        //主动打印第一句，借此发信号给子进程
        sig_handle(0);
        //sigsuspend
        while (i < top) {
            sigsuspend(&waitSet);
        }
        wait(NULL);
    }

}