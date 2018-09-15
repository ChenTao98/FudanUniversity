#include "iostream"
int a;
int b;
int sum(int arg1, int arg2){
    return arg1 + arg2;
}
int main() {
    a = 3;
    b = 5;
    int s = sum(a, b);
    int p = (a*b);
    int flag = 1;
    std::cout << "#include pass!" << std::endl;
    flag++;
    std::cout << "#define check1 pass!" << std::endl;
    std::cout << "#ifdef pass!" << std::endl;
    flag++;
    std::cout << "#undef pass! " << std::endl;
    std::cout << "#else pass!" << std::endl;
    flag++;
    std::cout << "#ifndef pass!" << std::endl;
    flag++;
    std::cout << "#if pass!" << std::endl;
    flag++;
    std::cout << "#define check2 pass!" << std::endl;
    flag++;
    std::cout << "#define check3 pass!" << std::endl;
    flag++;
    std::cout << "#define check4 pass!" << std::endl;
    if (s == 8 && p == 15) {
        flag++;
        std::cout << "#define check5 pass!" << std::endl;
    } else {
        std::cout << "#define check5 fail!" << std::endl;
    }
    if (flag == 9) {
        std::cout << "PART 1 pass!!!!!" << std::endl;
    } else {
        std::cout << "PART 1 fail!!!!!" << std::endl;
    }
    int d = (2*a);
    if (d == 2 * a) {
        std::cout << "PART 2 pass!!!!!" << std::endl;
    } else {
        std::cout << "PART 2 fail!!!!!" << std::endl;
    }
    std::cout << "PLUSES pass" << "!!!!!" << std::endl;
    return 0;
}
