//
// Created by chen on 19-5-19.
//
#include <stdio.h>
#include <signal.h>
#include <stdlib.h>

void sig_handler(int signo)
{
    printf("你确定要退出吗？\n");
    signal(SIGINT,NULL);
}
int main(){
    if(signal(SIGINT,sig_handler)==SIG_ERR){
        printf("信号绑定错误\n");
        exit(0);
    }
    while (1){
    }
}
