#include <stdlib.h>
#include <sys/time.h>
#include <printf.h>
#include <stdio.h>
#include <time.h>
#include <sys/unistd.h>
#include "handout.h"

//these are the implemented methods in 'handout.o' :

//double *generate(int n)
//bool check(double *Yours, double *A, double *B, int n)
//void printMatrix(double *M, int n)
//double *Mult(double *A, double *B, int n)

//the timer should be implemented in the YoursBlocked & YoursRecursive function and printed out in a format like "TIME: 0.000000 seconds"

int totalSize;
double *matAdd(int n,int m,double* A,double* B);
double *YoursRecursive(int n,int l,int m,double* A,double* B);
double *YoursBlocked(int n,double* A,double* B) {
    double *a;
    a = (double *) malloc(n * n * sizeof(double));
// fill your code here, a is your output matrix
	int i,j,k;
	int i1,j1,k1;
	int size;
	for(i=30;i>0;i--){
		if(n%i==0){
			size=i;
			break;
		}
	}
//	printf("%d\n",size);
	double sum=0;
	for(i=0;i<n;i+=size){
		for (j = 0; j < n; j+=size){
			for (k = 0; k < n; k+=size){
				for(i1=i;i1<i+size;i1++){
					for(j1=j;j1<j+size;j1++){
						sum=*(a+i1*n+j1);
						for(k1=k;k1<k+size;k1++){
							sum +=*(A+i1*n+k1)*(*(B+k1*n+j1));
						}
						*(a+i1*n+j1)=sum;
					}
				}
			}
		}
	}
    return a;
}

double *YoursRecursive(int n,int l,int m,double* A,double* B) {
    double *a;
	int i,j,k;
	int size1,size2;
	int multI1,multI2;
	int index,index1,index2;
	double temp;
	double *a1,*a2,*a3,*a4;
	int tempSize;
    index=n/2;
    index1=n-index;
    index2=m-index;
	tempSize=index*totalSize;
//	a1 = (double *) malloc(index * index * sizeof(double));
//	a2 = (double *) malloc(index * index2 * sizeof(double));
//	a3 = (double *) malloc(index1 * index * sizeof(double));
//	a4 = (double *) malloc(index1 * index2 * sizeof(double));
	double *tempMat1,*tempMat2;
    a = (double *) malloc(n * m * sizeof(double));
// fill your code here, a is your output matrix
    if(n<=64){
		for(i=0;i<n;i++){
			for(k=0;k<l;k++){
				temp=*(A+i*totalSize+k);
				for(j=0;j<m;j++){
					*(a+i*m+j) += *(B+k*totalSize+j)*temp ;
				}
			}
		}
	}else{
		tempMat1=YoursRecursive(index,index,index,A,B);
		tempMat2=YoursRecursive(index,l-index,index,A+index,B+tempSize);
		a1=matAdd(index,index,tempMat1,tempMat2);
		tempMat1=YoursRecursive(index,index,index2,A,B+index);
		tempMat2=YoursRecursive(index,l-index,index2,A+index,B+tempSize+index);
		a2=matAdd(index,index2,tempMat1,tempMat2);
		tempMat1=YoursRecursive(index1,index,index,A+tempSize,B);
		tempMat2=YoursRecursive(index1,l-index,index,A+tempSize+index,B+tempSize);
		a3=matAdd(index1,index,tempMat1,tempMat2);
		tempMat1=YoursRecursive(index1,index,index2,A+tempSize,B+index);
		tempMat2=YoursRecursive(index1,l-index,index2,A+tempSize+index,B+tempSize+index);
		a4=matAdd(index1,index2,tempMat1,tempMat2);
		for(i=0;i<index;i++){
			multI1=i*m;
			multI2=i*index;
			for(j=0;j<index;j++){
				*(a+multI1+j)=*(a1+multI2+j);
			}
		}
		for(i=0;i<index;i++){
			multI1=i*m+index;
			multI2=i*index2;
			for(j=0;j<index2;j++){
				*(a+multI1+j)=*(a2+multI2+j);
			}
		}
		size1=index*m;
		for(i=0;i<index1;i++){
			multI1=i*m+size1;
			multI2=i*index;
			for(j=0;j<index;j++){
				*(a+multI1+j)=*(a3+multI2+j);
			}
		}
		size1=index*m+index;
		for(i=0;i<index1;i++){
			multI1=i*m+size1;
			multI2=i*index2;
			for(j=0;j<index2;j++){
				*(a+multI1+j)=*(a4+multI2+j);
			}
		}
//		free(a1);
//		free(a2);
//		free(a3);
//		free(a4);
	}
//	free(a1);
//	free(a2);
//	free(a3);
//	free(a4);
    return a;
}
double *matAdd(int n,int m,double* A,double* B){
	double *a;
	int i,j,k;
	a = (double *) malloc(n * m * sizeof(double));
	for(i=0;i<n;i++){
		k=i*m;
		for(j=0;j<m;j++){
			*(a+k+j)=*(A+k+j)+*(B+k+j);
		}
	}
//	free(A);
//	free(B);
	return a;
}
int main(int argc, char *argv[]) {
    srand((unsigned int) time(NULL));
    int n = atoi(argv[1]);
	totalSize=n;
    double *A, *B;
	clock_t start,end;
	double totalTime;
    A = generate(n);
    B = generate(n);
//    printf("A\n");
//    printMatrix(A, n);
//    printf("B\n");
//    printMatrix(B, n);
    double *Y;
    Y = (double *) malloc(n * n * sizeof(double));
    Y = generate(n);

	start=clock();
    Y = YoursBlocked(n, A, B);
	end=clock();
	totalTime=(double)(end-start)/CLOCKS_PER_SEC*1000;
	printf("B: total time is %f ms\n",totalTime);
//    Y = Mult(A, B, n);
//    printf("Y\n");
//    printMatrix(Y, n);
    if (check(Y, A, B, n))
        printf("B TRUE%d\n", 1);
    else
        printf("B FALSE%d\n", 0);

	start=clock();
    Y = YoursRecursive(n,n,n, A, B);
	end=clock();
	totalTime=(double)(end-start)/CLOCKS_PER_SEC*1000;
	printf("R: total time is %f ms\n",totalTime);
    if (check(Y, A, B, n))
        printf("R TRUE%d\n", 1);
    else
        printf("R FALSE%d\n", 0);

    free(A);
    free(B);
    free(Y);
}
