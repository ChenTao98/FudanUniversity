//
// Created by Bing Chen on 2018/6/3.
//
#include <iostream>
#include <cstdio>
#include <string>
#include <cstring>
#include <fstream>
#include <sstream>
#include <map>
#include <vector>
#include <stack>
#include "preprocessLogic.h"

#define NPOS string::npos

using namespace std;

string processed_code;
//存储宏及其值的map
map<string, string> macroMap;
string macroName;
string macroValue;
//每次读取一行存储
string readLine;
//条件码，判断是否读取
bool isShouldRead = true;
//条件码历史，遇到if，ifdef，ifndef是压榨，遇到endif出栈
stack<bool> isShouldReadStack;

//执行完一次预处理后调用
void reset() {
    processed_code = "";
    macroMap.clear();
    isShouldRead = true;
    while (!isShouldReadStack.empty())
        isShouldReadStack.pop();
}

//外部接口，lab3.cpp调用，返回预处理后的字符串
string preProcess(string raw_code) {
    vector<string> raw_code_vector = splitString(raw_code, "\n");
    int length = raw_code_vector.size();
    for (int i = 0; i < length; i++) {
        readLine = raw_code_vector[i];
        instructionJudge();
    }
    return processed_code;
}

//判断指令类型，调用不同函数
void instructionJudge() {
    if (readLine.find("//") != NPOS) {
        return;
    }
    if (readLine.find("#") == 0) {
//        判断指令类型
        string tmpString = readLine.substr(readLine.find(" ", 2) + 1);
        if (readLine.find("else") != NPOS) {
            else_handler();
            return;
        } else if (readLine.find("endif") != NPOS) {
            endif_handler();
            return;
        } else if (readLine.find("ifdef") != NPOS) {
            macroName = tmpString;
            ifdef_handler();
            return;
        } else if (readLine.find("ifndef") != NPOS) {
            macroName = tmpString;
            ifndef_hander();
            return;
        } else if (readLine.find("undef") != NPOS) {
            macroName = tmpString;
            undef_handler();
            return;
        }
        if (isShouldRead) {
            if (readLine.find("include") != NPOS) {
                macroName = tmpString;
                include_handler();
                return;
            } else if (readLine.find("define") != NPOS) {
                int index = tmpString.find(" ");
                macroName = tmpString.substr(0, index);
                macroValue = tmpString.substr(index + 1);
                define_handler();
                return;
            } else if (readLine.find("if") != NPOS) {
                macroName = tmpString;
                if_handler();
                return;
            }
        }
    } else {
        if (isShouldRead) {
            normal_instruction_handler();
        }
    }
}

void normal_instruction_handler() {
    map<string, string>::iterator iter;
    iter = macroMap.begin();
    while (iter != macroMap.end()) {
        macroName = iter->first;
        macroValue = iter->second;
        string name, functionName;
        if (macroName.find("(") != NPOS) {
            functionName = macroName.substr(0, macroName.find("(") + 1);
        } else {
            name = macroName;
        }
//        if (!function_handler(functionName))
        function_handler(functionName);
        not_function_handler(name);
        iter++;
    }
    processed_code.append(readLine).push_back('\n');
}

// 包含函数宏调用
bool function_handler(string name) {
    if (name.compare("") == 0 || readLine.find(name) == NPOS || macroValue.compare("") == 0) {
        return false;
    }
    int indexOfLeftParenthesis = macroName.find("(");
    int indexOfRightParenthesis = macroName.find(")");
    string argOrigin = macroName.substr(indexOfLeftParenthesis + 1,
                                        indexOfRightParenthesis - indexOfLeftParenthesis - 1);
    string functionName = macroName.substr(0, indexOfLeftParenthesis);
    int index;
    string argInput;
    if ((index = readLine.find(functionName)) != NPOS) {
        indexOfLeftParenthesis = readLine.find("(", index);
        indexOfRightParenthesis = readLine.find(")", index);
        argInput = readLine.substr(indexOfLeftParenthesis + 1, indexOfRightParenthesis - indexOfLeftParenthesis - 1);
        int tempIndex;
        string tmpValue;
        if ((tempIndex = macroValue.find("\"#")) != NPOS) {
            tmpValue = macroValue;
            argOrigin = "\"#" + argOrigin;
            tmpValue.replace(tempIndex, argOrigin.length(), argInput + "\"");
        } else {
            tempIndex = macroValue.find(argOrigin);
            tmpValue = macroValue;
            tmpValue.replace(tempIndex, argOrigin.length(), argInput);
        }
        readLine.replace(index, indexOfRightParenthesis - index + 1, tmpValue);
    }
    return true;
}

//不包含函数宏调用
void not_function_handler(string name) {
    if (name.compare("") == 0 || readLine.find(name) == NPOS || macroValue.compare("") == 0) {
        return;
    }
    int index;
    if ((index = readLine.find(name)) != NPOS) {
        readLine.replace(index, name.length(), macroValue);
    }
}

void include_handler() {
    string filename = macroName.substr(1, macroName.length() - 2);
    if (macroName.find("<") == 0) {
        processed_code.append("#include ").append(macroName).push_back('\n');
    } else {
        if (!includeOtherFile(filename)) {
            processed_code.append("#include ").append(macroName).push_back('\n');
        }
    }
}

void define_handler() {
    macroMap.erase(macroName);
    while (macroMap.count(macroValue)) {
        macroValue = macroMap[macroValue];
    }
    macroMap.insert(map<string, string>::value_type(macroName, macroValue));
}

void undef_handler() {
    macroMap.erase(macroName);
}

void ifdef_handler() {
    isShouldReadStack.push(isShouldRead);
    isShouldRead = (macroMap.count(macroName) != 0);
}

void else_handler() {
    isShouldRead = !isShouldRead;
}

void ifndef_hander() {
    isShouldReadStack.push(isShouldRead);
    isShouldRead = (macroMap.count(macroName) == 0);
}

void if_handler() {
    isShouldReadStack.push(isShouldRead);
    while (macroMap.count(macroName) != 0) {
        macroName = macroMap[macroName];
    }
    isShouldRead = (!macroName.compare("1"));
}

void endif_handler() {
    if (!isShouldReadStack.empty()) {
        isShouldRead = isShouldReadStack.top();
        isShouldReadStack.pop();
    }
}

//当预处理文件包含其他头文件时，优先处理其他头文件
bool includeOtherFile(string filename) {
    if (!filename.compare("iostream")) {
        return false;
    }
    filename = "test/" + filename;
    string file;
    ifstream is(filename);
    if (!is.is_open()) {
        cout << "Broken input " + filename;
        return false;
    } else {
        string line;
        while (getline(is, line)) {
            file.append(line).push_back('\n');
        }
        is.close();
    }
    stack<bool> temStack = isShouldReadStack;
    bool tmpBool = isShouldRead;
    while (!isShouldReadStack.empty())
        isShouldReadStack.pop();
    preProcess(file);
    isShouldReadStack = temStack;
    isShouldRead = tmpBool;
    return true;
}

//分割字符串
vector<string> splitString(const string &str, const string &pattern) {
    char *strTmp = new char[strlen(str.c_str()) + 1];
    strcpy(strTmp, str.c_str());
    vector<string> resultVec;
    char *tmpStr = strtok(strTmp, pattern.c_str());
    while (tmpStr != NULL) {
        resultVec.push_back(string(tmpStr));
        tmpStr = strtok(NULL, pattern.c_str());
    }

    delete[] strTmp;

    return resultVec;
}
