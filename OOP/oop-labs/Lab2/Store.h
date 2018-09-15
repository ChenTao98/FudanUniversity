//
// Created by Bing Chen on 2018/5/6.
//

#ifndef LAB2_STORE_H
#define LAB2_STORE_H

#include <vector>
#include <string>
#include "Commodity.h"

using namespace std;
class Store {
    double todayAmount=0;
    double totalAmount=0;
    int dayOfNoPurchase=0;
    vector<Commodity> purchaseVector;
    vector<Commodity> stockVector;
    vector<string> purchase;
    vector<string> drop;
public:
    void initialPurchaseVector(Commodity &commodity);
    vector<string> purchaseCommodity(Date &date);
    bool sellCommodity(string name, double discount=1);
    vector<string> dropCommodity(Date &date);
    bool isShouldClose();
    void insertByOrder(Commodity &commodity);
    double getTodayAmount();
    double getTotalAmount();
    void setTotalAmount();
};


#endif //LAB2_STORE_H
