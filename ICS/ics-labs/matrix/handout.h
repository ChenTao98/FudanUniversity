//
// Created by Yunfan Li on 2018/3/19.
//

#ifndef LAB1_HANDOUT_H
#define LAB1_HANDOUT_H

#include <stdbool.h>

void printMatrix(double *M, int n);
double *generate(int n);
double *Mult(double *A, double *B, int n);
bool check(double *Yours, double *A, double *B, int n);

#endif //LAB1_HANDOUT_H
