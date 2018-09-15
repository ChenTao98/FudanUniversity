#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <string.h>
#include "toll.h"

using namespace std;

Toll toll;
vector<Truck> vecTruck;
vector<int> vecDue;
ofstream outfile;
void resetToll();
void addTruck(string tempStr);

int main(){
	string resetStr="RESET";
	string tempInput;
	fstream inputfile;
	inputfile.open("input.txt");
	outfile.open("result.txt",ios::out | ios::trunc);
	if(!inputfile){
		cout << "There is no file: inupt.txt" <<endl;
		return 0;
	}
	while(inputfile >> tempInput){
		if(strcmp(tempInput.c_str(),resetStr.c_str())==0){
			resetToll();
		}else{
			addTruck(tempInput);
		}
	}
	return 0;
}

void resetToll(){
	vecTruck=toll.getTruckVector();
	vecDue=toll.getDueOfTruckVector();
	outfile << toll.getTotalDue() << "," << toll.getNumOfTruck() << "\r\n";
	Truck truck;
	for(int i=0;i<vecTruck.size();i++){
		truck=vecTruck[i];
		outfile << truck.getTruckName() << "," << truck.getAxles() << "," << truck.getWeight() << "," << vecDue[i] << "\r\n";
	}
	toll.reset();
}

void addTruck(string tempStr){
	Truck truck(tempStr);
	toll.addNewTruck(truck);
}
