#include <iostream>
#include <fstream>
#include "preprocessLogic.h"
using namespace std;

string get_unprocessed_code(int number);

void put_processed_code(int number, string code);

void run_test(int test_case_number);

int main() {
    for (int test_case_number = 1; test_case_number <= 2; test_case_number++) {
        run_test(test_case_number);
    }
    return 0;
}

void run_test(int test_case_number) {

    string raw_code = get_unprocessed_code(test_case_number);
    /*
     * TODO: Your Code Here!
     * TODO: Take raw_code as your input, and output your processed code.
     * TODO: You'd better create new classes to handle your logic and use here only as an entrance.
     * */
    string processed_code = preProcess(raw_code);
    put_processed_code(test_case_number, processed_code);
    reset();
}

string get_unprocessed_code(int number) {
    string filename = "test/test" + to_string(number) + ".cpp";
    string file;
    ifstream is(filename);
    if (!is.is_open()) {
        cout << "Broken input file.";
    } else {
        string line;
        while (getline(is, line)) {
            file.append(line).push_back('\n');
        }
        is.close();
    }
    return file;
}

void put_processed_code(int number, string code) {
    string filename = "test/test" + to_string(number) + ".out.cpp";
    ofstream os(filename);
    if (!os.is_open()) {
        cout << "Broken output file.";
    } else {
        os << code;
        os.close();
    }
}