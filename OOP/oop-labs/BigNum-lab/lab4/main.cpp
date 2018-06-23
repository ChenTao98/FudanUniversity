#include <iostream>
#include <fstream>
#include <vector>
#include <cstring>
#include "Bignum.h"

using namespace std;
ofstream outputFile;
fstream inputFile;
vector<string> expression;//存储表达式分割结果
//格式化表达式，即把读入的一行表达式分割存储到向量中
void formatExpression(string tmpString) {
    expression.clear();
    int indexOfAdd;
    int indexOfSub;
    int indexOfMul;
    int indexOfDiv;
//根据运算符号分割
    while (true) {
        int index = 1000;
        indexOfAdd = tmpString.find("+");
        indexOfSub = tmpString.find("-");
        indexOfMul = tmpString.find("*");
        indexOfDiv = tmpString.find("/");
        if ((indexOfAdd == string::npos) && (indexOfSub == string::npos) && (indexOfMul == string::npos) &&
            (indexOfDiv == string::npos)) {
            expression.push_back(tmpString);
            break;
        }
        index = ((index > indexOfAdd) && (indexOfAdd != -1)) ? indexOfAdd : index;
        index = ((index > indexOfSub) && (indexOfSub != -1)) ? indexOfSub : index;
        index = ((index > indexOfMul) && (indexOfMul != -1)) ? indexOfMul : index;
        index = ((index > indexOfDiv) && (indexOfDiv != -1)) ? indexOfDiv : index;
        expression.push_back(tmpString.substr(0, index));
        expression.push_back(tmpString.substr(index, 1));
        tmpString = tmpString.substr(index + 1);
    }
}

//计算结果，每次分割表达式后，调用计算该表达式结果
BigNum calculate() {
    BigNum result(expression[0]);
    BigNum right;
    string tmpSting;
//    循环计算，每次计算一个运算符号
    for (int i = 1; i < expression.size(); i += 2) {
        if (result.currentLen > 25) {
            throw "the result is too large";
        }
        tmpSting = expression[i];
        right = BigNum(expression[i + 1]);
        if (strcmp(tmpSting.c_str(), "+") == 0) {
            result = result + right;
        } else if (strcmp(tmpSting.c_str(), "-") == 0) {
            result = result - right;
        } else if (strcmp(tmpSting.c_str(), "*") == 0) {
            result = result * right;
        } else if (strcmp(tmpSting.c_str(), "/") == 0) {
            result = result / right;
        }
    }
    return result;
}

int main() {
    inputFile.open("sample.in");
    outputFile.open("sample.out", ios::out | ios::trunc);
    if (!inputFile) {
        cout << "the input file is not exist" << endl;
        exit(0);
    }
    string tmpString;
    getline(inputFile, tmpString);
    BigNum bigNum;
    int n = atoi(tmpString.c_str());
    for (int i = 0; i < n; i++) {
        getline(inputFile, tmpString);
        formatExpression(tmpString);
        try {
            bigNum = calculate();
            outputFile << bigNum << "\n";
        } catch (const char *msg) {
            outputFile << msg << "\n";
        }
    }
    inputFile.close();
    outputFile.close();
    return 0;
}