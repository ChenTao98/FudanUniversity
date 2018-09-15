//
// Created by Bing Chen on 2018/5/6.
//

#include <iostream>
#include "Commodity.h"
#include "DateUtil.h"
using namespace std;
Commodity::Commodity(string name, double price, Date &date, int life) {
    this->name=name;
    this->price=price;
    this->dateProduce=date;
    this->life=life;
    this->dateDrop=computerDateDrop(date,life);
}
Commodity::~Commodity() {
    cout << name << " is dropped" << endl;
}
string Commodity::getName() { return name; }
double Commodity::getPrice() { return price;}
Date & Commodity::getDateProduce() { return dateProduce; }
Date& Commodity::getDateDrop() { return dateDrop;}
int Commodity::getLife() { return life;}
bool Commodity::isExpired(Date &today) {
    return isBefore(dateDrop,today);
}
Date Commodity::computerDateDrop(Date &date, int life) {
    Date dateTemp(date.getYear(),date.getMonth(),date.getDay());
    for(int i=1;i<life;i++){
        dateIncrease(dateTemp);
    }
    return dateTemp;
}