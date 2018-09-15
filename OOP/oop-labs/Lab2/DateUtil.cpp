//
// Created by Bing Chen on 2018/5/6.
//

#include "DateUtil.h"

bool isLeapYear(int year) {
    return year % 400 == 0 || (year % 100 != 0 && year % 4 == 0);
}

int getTotalDayOfMonth(int year, int month) {
    if (month == 4 || month == 6 || month == 9 || month == 11) {
        return 30;
    } else if (isLeapYear(year) && month == 2) {
        return 29;
    } else if (month == 2) {
        return 28;
    } else {
        return 31;
    }
}

bool isBefore(Date &a, Date &b) {
    if (a.getYear() > b.getYear()) {
        return false;
    } else if (a.getYear() < b.getYear()) {
        return true;
    } else if (a.getMonth() > b.getMonth()) {
        return false;
    } else if (a.getMonth() < b.getMonth()) {
        return true;
    } else {
        return a.getDay() <= b.getDay();
    }
}

bool isTheSameDay(Date &a, Date &b) {
    return a.getDay() == b.getDay() && a.getMonth() == b.getMonth() && a.getYear() == b.getYear();
}

void dateIncrease(Date &date) {
    int year = date.getYear();
    int month = date.getMonth();
    int day = date.getDay();
    if (day < getTotalDayOfMonth(year, month)) {
        day++;
    } else if (month < 12) {
        month++;
        day = 1;
    } else {
        year++;
        month = 1;
        day = 1;
    }
    date.setYear(year);
    date.setMonth(month);
    date.setDay(day);
}