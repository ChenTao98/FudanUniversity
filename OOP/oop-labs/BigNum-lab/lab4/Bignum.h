//
// Created by Bing Chen on 2018/6/13.
//

#include <string>

using namespace std;
#ifndef LAB4_BIGNUM_H
#define LAB4_BIGNUM_H
const int MAXLENGTH = 100;//接受的字符串最大长度
const int MAXARRAYLENGTH = 50;
const int BASE = 10000;
const int INTLENGTH = 4;

class BigNum {
public:
    int bigNum[MAXARRAYLENGTH];//用于存储大数，每一个元素存储大数的 4 位
    bool isNegative = false;//是否是复负数标志
    int currentLen;//当前数组存储的长度

    BigNum(const BigNum &input);

    BigNum(string input);

    BigNum();

    const BigNum operator+(const BigNum &input) const;

    const BigNum operator-(const BigNum &input) const;

    const BigNum operator*(const BigNum &input) const;

    const BigNum operator/(const BigNum &input) const;

    BigNum &operator=(const BigNum &right);

    bool operator>(const BigNum &input) const;

    bool operator<(const BigNum &input) const;

    bool operator>=(const BigNum &input) const;

    friend istream &operator>>(istream &in, BigNum &right);

    friend ostream &operator<<(ostream &out, BigNum &right);

    bool isZero() const;

    string toString();
};

#endif //LAB4_BIGNUM_H
