#include "timer.h"
#include "stdio.h"

void timer_start(struct timeval *starter) {
    gettimeofday(starter, NULL);
}

int timer_diff(struct timeval *starter, struct timeval *now) {
    long diff_sec = now->tv_sec - starter->tv_sec;
    long diff_usec = now->tv_usec - starter->tv_usec;
    int ret = diff_sec * 1000 + diff_usec / 1000; // return the ms
    return ret;
}

int timer_diff_now(struct timeval *starter) {
    struct timeval now;
    timer_start(&now);
    return timer_diff(starter, &now);
}