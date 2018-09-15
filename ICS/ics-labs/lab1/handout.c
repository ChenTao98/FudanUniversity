//
// Created by Yunfan Li on 2018/3/18.
//
#include<stdio.h>
#include<stdlib.h>
#include<time.h>
#include <stdbool.h>
#include <math.h>
#include <string.h>

void printMatrix(double *M, int n) {
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            printf("%f\t", M[i * n + j]);
    printf("\n");
}

double *Yours1(int n) {
    double *a;
    a = (double *) malloc(n * n * sizeof(double));
// fill your code here, change '0' to your output matrix
    return a;
}

double *generate(int n) {
    double *a;
    a = (double *) malloc(n * n * sizeof(double));
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++) {
            a[i * n + j] = rand() / (double) (RAND_MAX / 100);
        }
    return a;
}


//your output matrix should be presented as an 1-D array (not 2-D array)
double *Mult(double *A, double *B, int n) {
    double *C;
    C = (double *) malloc(n * n * sizeof(double));
    memset(C, 0, n * n * sizeof(double));
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            for (int k = 0; k < n; k++) {
                C[i * n + j] += A[i * n + k] * B[k * n + j];
            }
//    printf("C\n");
//    printMatrix(C, n);
    return C;
}
//include the check function from this file to check your program, use '#include "check.c"'
//your output matrix should be presented as an 1-D array (not 2-D array)

bool check(double *Yours, double *A, double *B, int n) {
    double *Mi;
    Mi = Mult(A, B, n);
    for (int i = 0; i < n * n; i++)
        if (fabs(Mi[i] - Yours[i]) > 1e-3) {
            free(Mi);
            printf("The first place where your program went wrong (i is the index within a 1-d array): \ni=%d\n", i);
            return false;
        }
    free(Mi);
    return true;
}

int main3() {
    srand((unsigned int) time(NULL));
    int n = 2;
    double *A, *B;
    A = generate(n);
    B = generate(n);
//    printf("A\n");
//    printMatrix(A, n);
//    printf("B\n");
//    printMatrix(B, n);
    double *Y;
    Y = (double *) malloc(n * n * sizeof(double));
    Y = Yours1(n);
//    Y = Mult(A, B, n);
//    printf("Y\n");
//    printMatrix(Y, n);
    if (check(Y, A, B, n))
        printf("TRUE%d\t", 1);
    else
        printf("FALSE%d\t", 0);
    free(A);
    free(B);
    free(Y);
    return 0;
}
