#include <string>
using namespace std;
class Truck{
	public:
		int getAxles();
		int getWeight();
		string getTruckName();
		Truck(string str);
        Truck();
	private:
		int axles;
		int weight;
		string truckName;
};
