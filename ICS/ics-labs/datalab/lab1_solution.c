/* 
 * CS:APP Data Lab 
 * 
 * <Please put your name and userid here>
 * 
 * bits.c - Source file with your solutions to the Lab.
 *          This is the file you will hand in to your instructor.
 *
 * WARNING: Do not include the <stdio.h> header; it confuses the dlc
 * compiler. You can still use printf for debugging without including
 * <stdio.h>, although you might get a compiler warning. In general,
 * it's not good practice to ignore compiler warnings, but in this
 * case it's OK.  
 */

#if 0
/*
 * Instructions to Students:
 *
 * STEP 1: Read the following instructions carefully.
 */

You will provide your solution to the Data Lab by
editing the collection of functions in this source file.

INTEGER CODING RULES:
 
  Replace the "return" statement in each function with one
  or more lines of C code that implements the function. Your code 
  must conform to the following style:
 
  int Funct(arg1, arg2, ...) {
      /* brief description of how your implementation works */
      int var1 = Expr1;
      ...
      int varM = ExprM;

      varJ = ExprJ;
      ...
      varN = ExprN;
      return ExprR;
  }

  Each "Expr" is an expression using ONLY the following:
  1. Integer constants 0 through 255 (0xFF), inclusive. You are
      not allowed to use big constants such as 0xffffffff.
  2. Function arguments and local variables (no global variables).
  3. Unary integer operations ! ~
  4. Binary integer operations & ^ | + << >>
    
  Some of the problems restrict the set of allowed operators even further.
  Each "Expr" may consist of multiple operators. You are not restricted to
  one operator per line.

  You are expressly forbidden to:
  1. Use any control constructs such as if, do, while, for, switch, etc.
  2. Define or use any macros.
  3. Define any additional functions in this file.
  4. Call any functions.
  5. Use any other operations, such as &&, ||, -, or ?:
  6. Use any form of casting.
  7. Use any data type other than int.  This implies that you
     cannot use arrays, structs, or unions.

 
  You may assume that your machine:
  1. Uses 2s complement, 32-bit representations of integers.
  2. Performs right shifts arithmetically.
  3. Has unpredictable behavior when shifting an integer by more
     than the word size.

EXAMPLES OF ACCEPTABLE CODING STYLE:
  /*
   * pow2plus1 - returns 2^x + 1, where 0 <= x <= 31
   */
  int pow2plus1(int x) {
     /* exploit ability of shifts to compute powers of 2 */
     return (1 << x) + 1;
  }

  /*
   * pow2plus4 - returns 2^x + 4, where 0 <= x <= 31
   */
  int pow2plus4(int x) {
     /* exploit ability of shifts to compute powers of 2 */
     int result = (1 << x);
     result += 4;
     return result;
  }

FLOATING POINT CODING RULES

For the problems that require you to implent floating-point operations,
the coding rules are less strict.  You are allowed to use looping and
conditional control.  You are allowed to use both ints and unsigneds.
You can use arbitrary integer and unsigned constants.

You are expressly forbidden to:
  1. Define or use any macros.
  2. Define any additional functions in this file.
  3. Call any functions.
  4. Use any form of casting.
  5. Use any data type other than int or unsigned.  This means that you
     cannot use arrays, structs, or unions.
  6. Use any floating point data types, operations, or constants.


NOTES:
  1. Use the dlc (data lab checker) compiler (described in the handout) to 
     check the legality of your solutions.
  2. Each function has a maximum number of operators (! ~ & ^ | + << >>)
     that you are allowed to use for your implementation of the function. 
     The max operator count is checked by dlc. Note that '=' is not 
     counted; you may use as many of these as you want without penalty.
  3. Use the btest test harness to check your functions for correctness.
  4. Use the BDD checker to formally verify your functions
  5. The maximum number of ops for each function is given in the
     header comment for each function. If there are any inconsistencies 
     between the maximum ops in the writeup and in this file, consider
     this file the authoritative source.

/*
 * STEP 2: Modify the following functions according the coding rules.
 * 
 *   IMPORTANT. TO AVOID GRADING SURPRISES:
 *   1. Use the dlc compiler to check that your solutions conform
 *      to the coding rules.
 *   2. Use the BDD checker to formally verify that your solutions produce 
 *      the correct answers.
 */


#endif
/* Copyright (C) 1991-2014 Free Software Foundation, Inc.
   This file is part of the GNU C Library.

   The GNU C Library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.

   The GNU C Library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public
   License along with the GNU C Library; if not, see
   <http://www.gnu.org/licenses/>.  */
/* This header is separate from features.h so that the compiler can
   include it implicitly at the start of every compilation.  It must
   not itself include <features.h> or any other header that includes
   <features.h> because the implicit include comes before any feature
   test macros that may be defined in a source file before it first
   explicitly includes a system header.  GCC knows the name of this
   header in order to preinclude it.  */
/* glibc's intent is to support the IEC 559 math functionality, real
   and complex.  If the GCC (4.9 and later) predefined macros
   specifying compiler intent are available, use them to determine
   whether the overall intent is to support these features; otherwise,
   presume an older compiler has intent to support these features and
   define these macros by default.  */
/* wchar_t uses ISO/IEC 10646 (2nd ed., published 2011-03-15) /
   Unicode 6.0.  */
/* We do not support C11 <threads.h>.  */
/* 
 * bitAnd - x&y using only ~ and | 
 *   Example: bitAnd(6, 5) = 4
 *   Legal ops: ~ |
 *   Max ops: 8
 *   Rating: 1
 */
int bitAnd(int x, int y) {
  return ~(~x | ~y);
}
/* 
 * getByte - Extract byte n from word x
 *   Bytes numbered from 0 (LSB) to 3 (MSB)
 *   Examples: getByte(0x12345678,1) = 0x56
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 6
 *   Rating: 2
 */
int getByte(int x, int n) {
  int mask = 0xFF;
  return (x >> (n << 3)) & mask; // (x >> (8 * n)) & mask
}
/* 
 * logicalShift - shift x to the right by n, using a logical shift
 *   Can assume that 0 <= n <= 31
 *   Examples: logicalShift(0x87654321,4) = 0x08765432
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 20
 *   Rating: 3 
 */
int logicalShift(int x, int n) {
  // 要求用算术右移实现逻辑右移，核心需要去掉算术右移负数时自动在高位补上的1
  // 用(1 << (32 - n)) - 1获得一个低32-n位为1的mask，与算术右移后的结果&一下即可
  // 用!!n这个小技巧处理一下n=0的情况
  int mask = (!!n << (32 + (~n + 1))) + (~1 + 1); // use !!n instead of 1 to handle when n=0
  return (x >> n) & mask;
}
/*
 * bitCount - returns count of number of 1's in word
 *   Examples: bitCount(5) = 2, bitCount(7) = 3
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 40
 *   Rating: 4
 */
int bitCount(int x) {
  // 移位相加的算法，细节可以直接百度bitCount，大量的答案
  int mask1 = 0x55 + (0x55 << 8);
  int mask2 = 0x33 + (0x33 << 8);
  int mask4 = 0x0F + (0x0F << 8);
  int mask8 = 0xFF + (0xFF << 16); // 0x00FF00FF
  int mask16 = (1 << 16) + ~0; // 0x0000FFFF
  mask1 = mask1 + (mask1 << 16); // 0x55555555
  mask2 = mask2 + (mask2 << 16); // 0x33333333
  mask4 = mask4 + (mask4 << 16); // 0x0F0F0F0F
  x = (x & mask1) + ((x >> 1) & mask1);
  x = (x & mask2) + ((x >> 2) & mask2);
  x = (x & mask4) + ((x >> 4) & mask4);
  x = (x & mask8) + ((x >> 8) & mask8);
  x = (x & mask16) + ((x >> 16) & mask16);
  return x;
}
/* 
 * bang - Compute !x without using !
 *   Examples: bang(3) = 0, bang(0) = 1
 *   Legal ops: ~ & ^ | + << >>
 *   Max ops: 12
 *   Rating: 4 
 */
int bang(int x) {
  // 其实就是需要一个手段把0和其他的数区分开
  // 对x|-x而言，只有x=0时其符号位为0，依次为依据进行判断
  return ~((x | (~x + 1)) >> 31) & 0x1; // use sign bit to distinguish 0
}
/* 
 * tmin - return minimum two's complement integer 
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 4
 *   Rating: 1
 */
int tmin(void) {
  return 1 << 31;
}
/* 
 * fitsBits - return 1 if x can be represented as an 
 *  n-bit, two's complement integer.
 *   1 <= n <= 32
 *   Examples: fitsBits(5,3) = 0, fitsBits(-4,3) = 1
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 15
 *   Rating: 2
 */
int fitsBits(int x, int n) {
  // 注意题中two's complement integer的理解，其实考察的是符号数的二进制表示
  // 核心是如果一个符号数可以用n位表示，那么将其高于n的位去掉后值应该不变
  // 注意不能用n位mask来&，而必须用先左移再右移的方法
  int offset = 32 + (~n + 1);
  return !(x ^ ((x << offset) >> offset));
}
/* 
 * divpwr2 - Compute x/(2^n), for 0 <= n <= 30
 *  Round toward zero
 *   Examples: divpwr2(15,1) = 7, divpwr2(-33,4) = -2
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 15
 *   Rating: 2
 */
int divpwr2(int x, int n) {
  // 注意题目要求Round toward zero
  // 对于正数不存在问题，但对于负数存在
  // 所以引入bias，对负数做修正
  int mask = (1 << n) + ~0;
  int bias = (x >> 31) & mask;
  return (x + bias) >> n;
}
/* 
 * negate - return -x 
 *   Example: negate(1) = -1.
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 5
 *   Rating: 2
 */
int negate(int x) {
  return ~x + 1;
}
/* 
 * isPositive - return 1 if x > 0, return 0 otherwise 
 *   Example: isPositive(-1) = 0.
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 8
 *   Rating: 3
 */
int isPositive(int x) {
  // 注意思考对于0的处理
  return !((x >> 31) | !x);
}
/* 
 * isLessOrEqual - if x <= y  then return 1, else return 0 
 *   Example: isLessOrEqual(4,5) = 1.
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 24
 *   Rating: 3
 */
int isLessOrEqual(int x, int y) {
  // 将对x <= y的判断转化为对y-x>=0的判断
  // 需要仔细思考溢出的问题
  // 请自行根据x,y,y-x的符号进行讨论
  int diff = y + (~x + 1);
  int sign_x = (x >> 31) & 0x1;
  int sign_y = (y >> 31) & 0x1;
  int sign_diff = (diff >> 31) & 0x1;
  int same = !(x ^ y);
  int not_same_sign_x_y = sign_x ^ sign_y;
  int same_sign_x_diff = !(sign_x ^ sign_diff);
  int overflow = not_same_sign_x_y & same_sign_x_diff;
  return same | ((!same) & ((overflow & sign_diff) | (!overflow & (!sign_diff))));
}
/*
 * ilog2 - return floor(log base 2 of x), where x > 0
 *   Example: ilog2(16) = 4
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 90
 *   Rating: 4
 */
int ilog2(int x) {
  // 本质是寻找x中最高位的1的位置
  // to find the index of highest "1" in x
  int result = 0;
  int offset;
  int zero;

  zero = !!(x >> 16);
  offset = zero << 4;
  result += offset;
  x = x >> offset;

  zero = !!(x >> 8);
  offset = zero << 3;
  result += offset;
  x = x >> offset;

  zero = !!(x >> 4);
  offset = zero << 2;
  result += offset;
  x = x >> offset;

  zero = !!(x >> 2);
  offset = zero << 1;
  result += offset;
  x = x >> offset;

  zero = !!(x >> 1);
  offset = zero;
  result += offset;

  return result;
}
/* 
 * float_neg - Return bit-level equivalent of expression -f for
 *   floating point argument f.
 *   Both the argument and result are passed as unsigned int's, but
 *   they are to be interpreted as the bit-level representations of
 *   single-precision floating point values.
 *   When argument is NaN, return argument.
 *   Legal ops: Any integer/unsigned operations incl. ||, &&. also if, while
 *   Max ops: 10
 *   Rating: 2
 */
unsigned float_neg(unsigned uf) {
  unsigned mask = 1 << 31;
  unsigned abs = uf & ~mask;
  if(abs > 0x7F800000)
    return uf;
  return uf ^ mask;
}
/* 
 * float_i2f - Return bit-level equivalent of expression (float) x
 *   Result is returned as unsigned int, but
 *   it is to be interpreted as the bit-level representation of a
 *   single-precision floating point values.
 *   Legal ops: Any integer/unsigned operations incl. ||, &&. also if, while
 *   Max ops: 30
 *   Rating: 4
 */
unsigned float_i2f(int x) {
  unsigned frac_mask = (1 << 23) - 1;
  unsigned abs = (x < 0) ? -x : x;
  unsigned sign = (x < 0);
  unsigned exp = 127 + 31;
  unsigned frac = 0;
  unsigned residue;

  if(abs == 0) {
    exp = 0;
    frac = 0;
  } else {
    while((abs & (1 << 31)) == 0) {
      abs <<= 1;
      exp--;
    }
    // handle rounding
    residue = abs & 0xFF;
    frac = (abs >> 8) & frac_mask;
    if(residue > 0x80 || (residue == 0x80 && (frac & 0x1))) {
      frac++;
      if(frac > frac_mask) {
        frac &= frac_mask;
        exp++;
      }
    }
  }

  return (sign << 31) | (exp << 23) | frac;
}
/* 
 * float_twice - Return bit-level equivalent of expression 2*f for
 *   floating point argument f.
 *   Both the argument and result are passed as unsigned int's, but
 *   they are to be interpreted as the bit-level representation of
 *   single-precision floating point values.
 *   When argument is NaN, return argument
 *   Legal ops: Any integer/unsigned operations incl. ||, &&. also if, while
 *   Max ops: 30
 *   Rating: 4
 */
unsigned float_twice(unsigned uf) {
  unsigned sign = !!(uf & (1 << 31));
  unsigned exp = (uf >> 23) & 0xFF;
  unsigned frac = uf & 0x7FFFFF;

  if(exp == 0) {
    frac <<= 1;
    if(frac > 0x7FFFFF) {
      frac = frac & 0x7FFFFF;
      exp = 1;
    }
  } else if(exp < 0xFF) {
    exp++;
    if(exp == 0xFF)
      frac = 0;
  }

  return (sign << 31) | (exp << 23) | frac;
}
