//
// Created by Bing Chen on 2018/6/3.
//
#include <iostream>
#include <cstdio>
#include <string>
#include <fstream>
#include <sstream>
#include <map>
#include <vector>
#include <stack>

using namespace std;
#ifndef LAB3_PREPROCESSLOGIC_H
#define LAB3_PREPROCESSLOGIC_H

void reset();

string preProcess(string raw_code);

void instructionJudge();

void normal_instruction_handler();

bool function_handler(string name);

void not_function_handler(string name);

void include_handler();

void define_handler();

void undef_handler();

void ifdef_handler();

void else_handler();

void ifndef_hander();

void if_handler();

void endif_handler();

bool includeOtherFile(string filename);

vector<string> splitString(const string &str, const string &pattern);

#endif //LAB3_PREPROCESSLOGIC_H
