#include <iostream>
#include "truck.h"
#include <string>
#include <stdlib.h>

using namespace std;

Truck::Truck(string str){
	str=str.substr(6);
	int index=0;
	index=str.find(",");
	truckName=str.substr(0,index);
	str=str.substr(index+1);
	index=str.find(",");
	weight=atoi(str.substr(0,index).c_str());
	axles=atoi(str.substr(index+1).c_str());
}

Truck::Truck(){}

int Truck::getAxles(){
	return axles;
}
int Truck::getWeight(){
	return weight;
}
string Truck::getTruckName(){
	return truckName;
}
