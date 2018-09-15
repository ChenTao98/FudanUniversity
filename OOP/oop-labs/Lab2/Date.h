//
// Created by Bing Chen on 2018/5/6.
//

#ifndef LAB2_DATE_H
#define LAB2_DATE_H

#include <string>

using namespace std;

class Date {
    int year;
    int month;
    int day;
public:
    Date(int year, int month, int day);
    Date();
    ~Date();
    int getYear();
    int getMonth();
    int getDay();
    void setYear(int year);
    void setMonth(int month);
    void setDay(int day);
    string toString();
};


#endif //LAB2_DATE_H
