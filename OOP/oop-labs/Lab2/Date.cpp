//
// Created by Bing Chen on 2018/5/6.
//

#include "Date.h"

Date::Date(int year, int month, int day) {
    this->year = year;
    this->month = month;
    this->day = day;
}
Date::~Date() {}
int Date::getYear() { return year; }

int Date::getMonth() { return month; }

int Date::getDay() { return day; }
void Date::setYear(int year) {this->year=year;}
void Date::setMonth(int month) {this ->month=month;}
void Date::setDay(int day) {this->day=day;}
string Date::toString() {
    return to_string(year)+"/"+to_string(month)+"/"+to_string(day);
}
Date::Date() {}
