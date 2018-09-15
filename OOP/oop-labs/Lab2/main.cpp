#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <cstring>
#include <cstdlib>
#include "Date.h"
#include "Store.h"
#include "DateUtil.h"

using namespace std;

string stringTemp;
const char pattern = '\t';
ofstream outfile;
fstream purchaseFile;
fstream sellFile;
vector<string> purchase;
vector<string> drop;
Store store;
Date today(2018, 4, 23);

void initialPurchase() {
    getline(purchaseFile, stringTemp);
    string name;
    double price;
    int life;
    int year;
    int month;
    int day;
    int index;
    while (getline(purchaseFile, stringTemp)) {
        index = stringTemp.find(pattern);
        name = stringTemp.substr(0,index);
        stringTemp = stringTemp.substr(index + 1);
        index = stringTemp.find(pattern);
        price = atof(stringTemp.substr(0,index).c_str());
        stringTemp = stringTemp.substr(index + 1);
        index = stringTemp.find(pattern);
        life = atoi(stringTemp.substr(0, index).c_str());
        stringTemp = stringTemp.substr(index + 1);
        index = stringTemp.find("/");
        year = atoi(stringTemp.substr(0, index).c_str());
        stringTemp = stringTemp.substr(index + 1);
        index = stringTemp.find("/");
        month = atoi(stringTemp.substr(0, index).c_str());
        day = atoi(stringTemp.substr(index + 1).c_str());
        Date date(year, month, day);
        Commodity commodity(name, price, date, life);
        store.initialPurchaseVector(commodity);
    }
}

void outDayBegin() {
    outfile << "#Day " << today.toString() << "\r\n";
    outfile << "\r\n";
}

void purchaseCommodity() {
    purchase = store.purchaseCommodity(today);
    outfile << "PURCHASE:" << "\r\n";
    for (int i = 0; i < purchase.size(); ++i) {
        outfile << purchase[i] << "\r\n";
    }
    outfile << "\r\n";
}

void sellCommodity() {
    sellFile.open("sell.txt");
    if (!sellFile) {
        cout << "no sell file" << endl;
        return;
    }
    int index;
    bool isSell = false;
    double discount;
    string outString;
    outfile << "SELL SUCCESSFULLY:" << "\r\n";
    getline(sellFile, stringTemp);
    while (getline(sellFile, stringTemp)) {
        index = stringTemp.find(pattern);
        if (stringTemp.length() <= (index + 2)) {
            isSell = store.sellCommodity(stringTemp.substr(0,index));
            outString = stringTemp.substr(0, stringTemp.length()-1);
        } else {
            discount = atof(stringTemp.substr(index + 1).c_str());
            isSell = store.sellCommodity(stringTemp.substr(0, index), discount);
            outString = stringTemp.substr(0, index) + " / " + to_string(discount).substr(0,4);
        }
        if (isSell) {
            outfile << outString << "\r\n";
        }
    }
    outfile << "\r\n";
    sellFile.close();
}

void dropCommodity() {
    drop = store.dropCommodity(today);
    outfile << "DROP THE EXPIRED:" << "\r\n";
    for (int i = 0; i < drop.size(); i++) {
        outfile << drop[i] << " is dropped" << "\r\n";
    }
    outfile << "\r\n";
}

void outDayEnd() {
    outfile << "AMOUNT OF SALE ON " << today.toString() << ": "
            << store.getTodayAmount() << "\r\n";
    outfile << "-------------------------------------------------" << "\r\n";
    outfile << "\r\n";
}

int main() {
    purchaseFile.open("purchase.txt");
    outfile.open("answer.txt", ios::out | ios::trunc);
    if (!purchaseFile) {
        cout << "no purchase file" << endl;
        return 0;
    }
    initialPurchase();
    purchaseFile.close();
    int count=0;
    while ((!store.isShouldClose())&&count<40) {
        outDayBegin();
        purchaseCommodity();
        sellCommodity();
        dropCommodity();
        outDayEnd();
        store.setTotalAmount();
        dateIncrease(today);
        count++;
    }
    outDayBegin();
    outfile << "Today close the FamilyMart immediately" << "\r\n";
    outfile << "The total money we have earned: " << store.getTotalAmount();
    outfile.close();
    return 0;
}