//
// WARNING: You are not allowed to modify this file!
//
#include <iostream>

# define FALSE 0
# define TRUE 1
# define NOTFALSE TRUE

int main() {
    if (FALSE == 0 && TRUE == 1 && NOTFALSE == 1) {
        std::cout << "simple test passed." << std::endl;
    }

    return 0;
}
