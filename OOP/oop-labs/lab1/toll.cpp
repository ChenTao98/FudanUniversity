#include <iostream>
#include <string>
#include "toll.h"
#include <vector>

using namespace std;

int Toll::getTotalDue(){
	return totalDue;
}
int Toll::getNumOfTruck(){
	return numOfTruck;
}
vector<Truck> Toll::getTruckVector(){
	return truckVector;
}
vector<int> Toll::getDueOfTruckVector(){
	return dueOfTruckVector;
}
void Toll::calculateDue(Truck &truck){
	due=truck.getAxles()*5+truck.getWeight()*20/1000;
	totalDue+=due;
	numOfTruck++;
}
void Toll::addNewTruck(Truck &truck){
	calculateDue(truck);
	truckVector.push_back(truck);
	dueOfTruckVector.push_back(due);
}
void Toll::reset(){
	truckVector.clear();
	dueOfTruckVector.clear();
	numOfTruck=0;
	totalDue=0;
}
