//
// Created by Bing Chen on 2018/6/13.
//

#include <iostream>
#include <cstring>
#include "Bignum.h"

using namespace std;

//构造函数，用于将字符串转为bignum
BigNum::BigNum(string input) {
    int index;
    memset(bigNum, 0, sizeof(bigNum));
//    判断是否有空格，有就先去除空格
    while (true) {
        if ((index = input.find(" ")) != string::npos) {
            input.replace(index, 1, "");
            continue;
        }
        break;
    }
//    判断是否超过100位
    int lengthOfInput = input.length();
    if (input.length() >= MAXLENGTH) {
        throw "the num is too big!";
    }
//    由于采用数组存储，每四个数字一个单元，所以判断长度是否是4的倍数
    currentLen = lengthOfInput / INTLENGTH;
    if (lengthOfInput % INTLENGTH != 0) {
        currentLen++;
//        若长度不是4的倍数，补0；
        while (input.length() % INTLENGTH != 0) {
            input = "0" + input;
        }
    }
//    截取存储
    for (int i = 0; i < currentLen; i++) {
        bigNum[currentLen - 1 - i] = atoi(input.substr(i * INTLENGTH, INTLENGTH).c_str());
    }
}

//拷贝构造函数
BigNum::BigNum(const BigNum &input) {
    memset(bigNum, 0, sizeof(bigNum));
    currentLen = input.currentLen;
    isNegative = input.isNegative;
    for (int i = 0; i < currentLen; i++) {
        bigNum[i] = input.bigNum[i];
    }
}

//默认构造函数，初始化为0；
BigNum::BigNum() {
    memset(bigNum, 0, sizeof(bigNum));
    currentLen = 1;
    isNegative = false;
}

//加法运输符重载
const BigNum BigNum::operator+(const BigNum &input) const {
    BigNum right = input;
    BigNum left(*this);
//    两数不同号，转换为减法处理
//    左正右负
    if (!left.isNegative && right.isNegative) {
        right.isNegative = false;
        left = left - right;
        return left;
    }
//    左负右正
    if (left.isNegative && !right.isNegative) {
        left.isNegative = false;
        left = right - left;
        return left;
    }
//    同号处理
//    取大者长度循环
    int temLen = left.currentLen > right.currentLen ? left.currentLen : right.currentLen;
    for (int i = 0; i < temLen; i++) {
        left.bigNum[i] += right.bigNum[i];
//        判断是否需要进位
        if (left.bigNum[i] >= BASE) {
            left.bigNum[i + 1]++;
            left.bigNum[i] %= BASE;
        }
    }
//    判断结果最大长度
    if (left.bigNum[temLen] != 0) {
        left.currentLen = temLen + 1;
    } else {
        left.currentLen = temLen;
    }
    return left;
}

//减法运输符重载
const BigNum BigNum::operator-(const BigNum &input) const {
    BigNum right = input;
    BigNum left(*this);
//    两数异号,转为加法处理
//    左正右负
    if (!left.isNegative && right.isNegative) {
        right.isNegative = false;
        left = left + right;
        return left;
    }
//    左负右正
    if (left.isNegative && !right.isNegative) {
        right.isNegative = true;
        left = left + right;
        return left;
    }
//    以下处理两数同号,减法处理,保证始终是绝对值大者减小者
//    左边小于右边,且两者均为正数
    if (left < right && !left.isNegative) {
        left = right - left;
        left.isNegative = true;
        return left;
    }
//    左边大于右边,且两者均为负数
    if (left > right && left.isNegative) {
        left = right - left;
        left.isNegative = false;
        return left;
    }
//    此时已经保证两数同号,且左边绝对值大于等于右边
    int tempLen = left.currentLen;
    for (int i = 0; i < tempLen; i++) {
        left.bigNum[i] -= right.bigNum[i];
//        如果当前单元小于0,向高位借 1 ,相减
        if (left.bigNum[i] < 0) {
            left.bigNum[i + 1]--;
            left.bigNum[i] += BASE;
        }
    }
//    判断长度
    left.currentLen = 1;
    for (int i = tempLen - 1; i >= 0; i--) {
        if (left.bigNum[i] != 0) {
            left.currentLen = i + 1;
            break;
        }
    }
    return left;
}

//乘法运输符重载
const BigNum BigNum::operator*(const BigNum &input) const {
//    相乘
    BigNum mulResult;
    for (int i = 0; i < currentLen; i++) {
        for (int j = 0; j < input.currentLen; j++) {
            mulResult.bigNum[i + j] += bigNum[i] * input.bigNum[j];
        }
    }
//    处理进位
    int tmpLen = currentLen + input.currentLen;
    int carry;
    for (int i = 0; i < tmpLen; i++) {
        if (mulResult.bigNum[i] >= BASE) {
            carry = mulResult.bigNum[i] / BASE;
            mulResult.bigNum[i] %= BASE;
            mulResult.bigNum[i + 1] += carry;
        }
        if (mulResult.bigNum[i] != 0) {
            mulResult.currentLen = i + 1;
        }
    }
    mulResult.isNegative = (isNegative != input.isNegative);
    return mulResult;
}

//除法运输符重载
const BigNum BigNum::operator/(const BigNum &input) const {
//    判断除数是否为0
    if (input.isZero()) {
        throw "Division by zero condition!";
    }
//    构建必要的基本大数10000,1,10,100,1000
    BigNum bigNumBase(to_string(BASE));
    BigNum bigNumOne(to_string(1));
    BigNum bigNumTen(to_string(10));
    BigNum bigNumHundred(to_string(100));
    BigNum bigNumThousand(to_string(1000));

    BigNum bigNumTimes;
    BigNum divResult;
    BigNum tempBigNum;
    BigNum tempBigNumSub;
    BigNum tempTimes;
    BigNum right = input;
    BigNum left(*this);
    bool flag = (left.isNegative != right.isNegative);
    left.isNegative = false;
    right.isNegative = false;
//    判断是否左边绝对值大于右边
    if (!(left >= right)) {
        return divResult;
    }
//    判断倍数
    int times = left.currentLen - right.currentLen;
//    两者长度不同
    for (; times > 0;) {
//        计算基本倍数
        tempTimes = bigNumBase;
        for (int i = 0; i < times - 1; i++) {
            tempTimes = tempTimes * bigNumBase;
        }
//        以下按减法操作
//        如果可以,就循环减去除数的1000* 相差长度*10000倍
        tempBigNum = right * tempTimes;
        tempBigNumSub = tempBigNum * bigNumThousand;
        bigNumTimes = tempTimes * bigNumThousand;
        while (left >= tempBigNumSub) {
            left = left - tempBigNumSub;
            divResult = divResult + bigNumTimes;
        }
//        如果可以,就循环减去除数的100* 相差长度*10000倍
        tempBigNumSub = tempBigNum * bigNumHundred;
        bigNumTimes = tempTimes * bigNumHundred;
        while (left >= tempBigNumSub) {
            left = left - tempBigNumSub;
            divResult = divResult + bigNumTimes;
        }
//        如果可以,就循环减去除数的10* 相差长度*10000倍
        tempBigNumSub = tempBigNum * bigNumTen;
        bigNumTimes = tempTimes * bigNumTen;
        while (left >= tempBigNumSub) {
            left = left - tempBigNumSub;
            divResult = divResult + bigNumTimes;
        }
//        如果可以,就循环减去除数的 相差长度*10000倍
        while (left >= tempBigNum) {
            left = left - tempBigNum;
            divResult = divResult + tempTimes;
        }
        times--;
    }
//    处理剩余两者长度相同情况,与前面类似
    while (true) {
//        循环减去除数1000倍
        while (left >= (tempBigNumSub = right * bigNumThousand)) {
            left = left - tempBigNumSub;
            divResult = divResult + bigNumThousand;
        }
//        循环减去被除数100倍
        while (left >= (tempBigNumSub = right * bigNumHundred)) {
            left = left - tempBigNumSub;
            divResult = divResult + bigNumHundred;
        }
//        循环减去除数10倍
        while (left >= (tempBigNumSub = right * bigNumTen)) {
            left = left - tempBigNumSub;
            divResult = divResult + bigNumTen;
        }
//        循环减去除数
        while (left >= right) {
            left = left - right;
            divResult = divResult + bigNumOne;
        }
        break;
    }
    divResult.isNegative = flag;
    return divResult;
}

//等于号运算符重载
BigNum &BigNum::operator=(const BigNum &right) {
    currentLen = right.currentLen;
    isNegative = right.isNegative;
    memset(bigNum, 0, sizeof(bigNum));
    for (int i = 0; i < currentLen; i++) {
        bigNum[i] = right.bigNum[i];
    }
    return *this;
}

//大于号运算符重载,判断包含正负号
bool BigNum::operator>(const BigNum &input) const {
    if (isNegative && !input.isNegative) return false;
    if (!isNegative && input.isNegative) return true;
    int i = currentLen > input.currentLen ? currentLen - 1 : input.currentLen - 1;
    for (; i >= 0; i--) {
        if (bigNum[i] > input.bigNum[i]) {
            return !isNegative;
        } else if (bigNum[i] < input.bigNum[i]) {
            return isNegative;
        }
    }
    return false;
}

//小于号运算符重载,判断包含正负号
bool BigNum::operator<(const BigNum &input) const {
    if (isNegative && !input.isNegative) return true;
    if (!isNegative && input.isNegative) return false;
    int i = currentLen > input.currentLen ? currentLen - 1 : input.currentLen - 1;
    for (; i >= 0; i--) {
        if (bigNum[i] > input.bigNum[i]) {
            return isNegative;
        } else if (bigNum[i] < input.bigNum[i]) {
            return !isNegative;
        }
    }
    return false;
}

//大于等于号运算符重载,只判断绝对值,不包含正负号
bool BigNum::operator>=(const BigNum &input) const {
    if (currentLen < input.currentLen)return false;
    for (int i = currentLen - 1; i >= 0; i--) {
        if (bigNum[i] > input.bigNum[i]) {
            return true;
        } else if (bigNum[i] < input.bigNum[i]) {
            return false;
        }
    }
    return true;
}

//输入符重载
istream &operator>>(istream &in, BigNum &right) {
    string input;
    in >> input;
    right = BigNum(input);
    return in;
}

//输出符重载
ostream &operator<<(ostream &out, BigNum &right) {
    out << right.toString();
    return out;
}

//判断是否为0
bool BigNum::isZero() const {
    for (int i = 0; i < currentLen; i++) {
        if (bigNum[i] != 0) {
            return false;
        }
    }
    return true;
}

//将大数转为字符串
string BigNum::toString() {
    string result;
    if (isNegative) {
        result = "-";
    } else {
        result = "";
    }
    string tmpString;
    int temLen = currentLen - 1;
    for (int i = temLen; i >= 0; i--) {
        tmpString = to_string(bigNum[i]);
        while ((i != (temLen)) && (tmpString.length() % INTLENGTH != 0)) {
            tmpString = "0" + tmpString;
        }
        result += tmpString;
    }
    result = strcmp(result.c_str(), "-0") == 0 ? "0" : result;
    return result;
}