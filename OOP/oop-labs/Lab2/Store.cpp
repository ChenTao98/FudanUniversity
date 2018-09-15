//
// Created by Bing Chen on 2018/5/6.
//

#include <cstring>
#include <iostream>
#include "Store.h"
#include "DateUtil.h"

void Store::initialPurchaseVector(Commodity &commodity) {
    purchaseVector.push_back(commodity);

}

vector<string> Store::purchaseCommodity(Date &date) {
    purchase.clear();
    for (int i = 0; i < purchaseVector.size(); i++) {
        if (isTheSameDay(purchaseVector[i].getDateProduce(), date)) {
            insertByOrder(purchaseVector[i]);
            purchase.push_back(purchaseVector[i].getName());
            purchaseVector.erase(purchaseVector.begin() + i);
            i--;
        }
    }
    if (purchase.size() != 0) {
        dayOfNoPurchase = 0;
    } else {
        dayOfNoPurchase++;
    }
    return purchase;
}

bool Store::sellCommodity(string name, double discount) {
    for (int i = 0; i < stockVector.size(); i++) {
        if (strcmp(name.c_str(), stockVector[i].getName().c_str()) == 0) {
            todayAmount += (stockVector[i].getPrice() * discount);
            stockVector.erase(stockVector.begin() + i);
            return true;
        }
    }
    cout << name << " is sold out" << endl;
    return false;
}

vector<string> Store::dropCommodity(Date &date) {
    drop.clear();
    for (int i = 0; i < stockVector.size(); i++) {
        if (isBefore(stockVector[i].getDateDrop(), date)) {
            drop.push_back(stockVector[i].getName());
            stockVector.erase(stockVector.begin() + i);
            i--;
        }
    }
    return drop;
}

bool Store::isShouldClose() {
    return dayOfNoPurchase >= 3 && stockVector.size() == 0;
}

void Store::insertByOrder(Commodity &commodity) {
    if (stockVector.size() == 0 ||
        (isBefore(stockVector[stockVector.size()-1].getDateProduce(), commodity.getDateProduce()))) {
        stockVector.push_back(commodity);
        return;
    } else if (isBefore(commodity.getDateProduce(), stockVector[0].getDateProduce())) {
        stockVector.insert(stockVector.begin(), commodity);
        return;
    }
    for (int i = 0; i < stockVector.size() - 1; i++) {
        if (isBefore(stockVector[i].getDateProduce(), commodity.getDateProduce()) &&
            isBefore(commodity.getDateProduce(), stockVector[i + 1].getDateProduce())) {
            stockVector.insert(stockVector.begin() + (i + 1), commodity);
            return;
        }
    }
}

double Store::getTodayAmount() { return todayAmount; }

double Store::getTotalAmount() { return totalAmount; }

void Store::setTotalAmount() {
    totalAmount += todayAmount;
    todayAmount = 0;
}