//
// Created by Bing Chen on 2018/5/6.
//

#ifndef LAB2_DATEUTIL_H
#define LAB2_DATEUTIL_H

#include "Date.h"

bool isLeapYear(int year);
int getTotalDayOfMonth(int year,int month);
bool isBefore(Date &a,Date &b);
bool isTheSameDay(Date &a,Date &b);
void dateIncrease(Date &date);
#endif //LAB2_DATEUTIL_H
