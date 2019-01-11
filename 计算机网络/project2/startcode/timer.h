#ifndef PEER_TIMER_H
#define PEER_TIMER_H

#include <sys/time.h>

//对传入的时间赋值当前时间
void timer_start(struct timeval *);

//计算两个时间的差值，返回为毫秒
int timer_diff(struct timeval *starter, struct timeval *now);

//计算传入时间与当前时间的差值
int timer_diff_now(struct timeval *starter);

#endif //PEER_TIMER_H
