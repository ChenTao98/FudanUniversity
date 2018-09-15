#include <vector>
#include <string>
#include "truck.h"
using namespace std;
class Toll{
	public:
		int getTotalDue();
		int getNumOfTruck();
		vector<Truck> getTruckVector();
		vector<int> getDueOfTruckVector();
		void calculateDue(Truck &truck);
		void addNewTruck(Truck &truck);
		void reset();
	private:
		vector<Truck> truckVector;
		vector<int> dueOfTruckVector;
		int due;
		int totalDue;
		int numOfTruck;
};
