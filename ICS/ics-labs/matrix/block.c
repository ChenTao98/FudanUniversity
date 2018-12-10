#include <stdlib.h>
#include <sys/time.h>
#include <printf.h>
#include <stdio.h>
#include <time.h>
#include <sys/unistd.h>
#include <string.h>
#include <math.h>
int N;
int M;
int K;
const double epsilon = 1e-3;
int baseSize=64;
//定义结构体，用于递归函数的参数
typedef struct{
	int n;
	int k;
	int m;
	double *A;
	double *B;
	double *C;
}matPara;
double *matAdd(int n,int m,double* A,double* B);
void YoursRecursive(matPara paraIn);
//生成矩阵n*m
double *generate(int n,int m) {
    double *a;
    a = (double *) malloc(n * m * sizeof(double));
    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++) {
            a[i * m + j] = rand() / (double) (RAND_MAX / 100);
        }
    return a;
}
void YoursRecursive(matPara paraIn) {
	int n=paraIn.n;
	int m=paraIn.m;
	int k=paraIn.k;
	int max=(n>m)? n:m;
	max=(max>k)? max:k;
	double *A=paraIn.A;
	double *B=paraIn.B;
	double *C=paraIn.C;
	//当最大的边小于basesize时，采用普通的矩阵计算
	if(max<=baseSize){
		double temp;
		for(int i=0;i<n;i++){
			for(int l=0;l<k;l++){
			temp=*(A+i*K+l);
			for(int j=0;j<m;j++){
				*(C+i*M+j) += *(B+l*M+j)*temp ;
				}
			}
		}
	}else if(max==k){
		//最大的边是K时，分割k
		max/=2;
		matPara s1={
			n,max,m,A,B,C
		};
		YoursRecursive(s1);
		matPara s2={
			n,k-max,m,A+max,B+max*M,C
		};
		YoursRecursive(s2);
	}else if(max==n){
		//分割n
		max/=2;
		matPara s1={
			max,k,m,A,B,C
		};
		YoursRecursive(s1);
		matPara s2={
			n-max,k,m,A+max*K,B,C+max*M
		};
		YoursRecursive(s2);
	}else{
		//分割m
		max/=2;
		matPara s1={
			n,k,max,A,B,C
		};
		YoursRecursive(s1);
		matPara s2={
			n,k,m-max,A,B+max,C+max
		};
		YoursRecursive(s2);
	}
}
double *Mult(double *A, double *B, int n,int l,int m) {
    double *C;
    C = (double *) malloc(n * m * sizeof(double));
    memset(C, 0, n * m * sizeof(double));
    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            for (int k = 0; k < l; k++) {
                C[i * m + j] += A[i * l + k] * B[k * m + j];
            }
    return C;
}
int check(double *Yours, double *A, double *B, int n,int k,int m) {
    double *Mi;
    Mi = Mult(A, B, n,k,m);
    for (int i = 0; i < n * m; i++)
        if (fabs(Mi[i] - Yours[i]) > epsilon) {
            free(Mi);
            printf("The first place where your program went wrong (i is the index within a 1-d array): \ni=%d\n", i);
            return 0;
        }
    free(Mi);
    return 1;
}
int main(int argc, char *argv[]) {
    srand((unsigned int) time(NULL));
	if(argc!=4){
		printf("usage: %s N K M\n",argv[0]);
		exit(0);
	}
    N= atoi(argv[1]);
	K= atoi(argv[2]);
	M= atoi(argv[3]);
    double *A, *B;
	clock_t start,end;
	double totalTime;
    A = generate(N,K);
    B = generate(K,M);

    double *C;
    C = (double *) calloc(N * M , sizeof(double));

	start=clock();
    matPara s1={
		N,K,M,A,B,C
	};
	YoursRecursive(s1);
	end=clock();
	totalTime=(double)(end-start)/CLOCKS_PER_SEC*1000;
	printf("R: total time is %f ms\n",totalTime);
    if (check(C, A, B, N,K,M))
        printf("R TRUE%d\n", 1);
    else
        printf("R FALSE%d\n", 0);
    free(A);
    free(B);
    free(C);
}
