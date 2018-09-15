//
// Created by Bing Chen on 2018/5/6.
//

#ifndef LAB2_COMMODITY_H
#define LAB2_COMMODITY_H

#include <string>
#include "Date.h"

using namespace std;
class Commodity {
    string name;
    double price;
    Date dateProduce;
    Date dateDrop;
    int life;
    Date  computerDateDrop(Date &date,int life);
public:
    Commodity(string name, double price,Date &date,int life);
    ~Commodity();
    string getName();
    double getPrice();
    Date & getDateProduce();
    Date & getDateDrop();
    int getLife();
    bool isExpired(Date &today);
};


#endif //LAB2_COMMODITY_H
