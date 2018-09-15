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
	/*取反，使用 | 语句将x与y原先同为1的位改为0，再取反*/
	int result=~(~x|~y);
  return result;
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
	/*计算出需要右移的位数，右移，再保留低位即可*/
	int result=(x>>(n<<3));
	result=result&0xff;
  return result;
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
	/*右移之后，将高位改为0，低位保持不变*/
	int result=x>>n;
	int bit=~(((1<<31)>>n)<<1);
	result=result&bit;
  return result;
}
/*
 * bitCount - returns count of number of 1's in word
 *   Examples: bitCount(5) = 2, bitCount(7) = 3
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 40
 *   Rating: 4
 */
int bitCount(int x) {
	/*使用二分法，通过以为设置五个字符串：
	0101 0101 0101 0101 0101 0101 0101 0101
	0011 0011 0011 0011 0011 0011 0011 0011
	0000 1111 0000 1111 0000 1111 0000 1111
	0000 0000 1111 1111 0000 0000 1111 1111
	0000 0000 0000 0000 1111 1111 1111 1111
	分别通过按位与统计1的个数，并将个数记录在下一个字符串1出现的位置。
	最后可以得到1的个数。 */
	int result;
	int	half_one=(0x55)|(0x55<<8);
	int one=(half_one)|(half_one<<16);
	int half_two=(0x33)|(0x33<<8);
	int two=(half_two)|(half_two<<16);
	int half_three=(0x0f)|(0x0f<<8);
	int three=(half_three)|(half_three<<16);
	int four=(0xff)|(0xff<<16);
	int five=(0xff)|(0xff<<8);
    result=(x&one)+((x>>1)&one);
	result=(result&two)+((result>>2)&two);
	result=(result+(result>>4))&three;
	result=(result+(result>>8))&four;
	result=(result+(result>>16))&five;
	return result;
}
/*
 * bang - Compute !x without using !
 *   Examples: bang(3) = 0, bang(0) = 1
 *   Legal ops: ~ & ^ | + << >>
 *   Max ops: 12
 *   Rating: 4
 */
int bang(int x) {
	/*除了0，其他数与其相反按位或，符号位均为1，
	所以异或之后，判断符号位即可*/
	int result=(x|((~x)+1))>>31;
	result=(~result)&1;
  return result;
}
/*
 * tmin - return minimum two's complement integer
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 4
 *   Rating: 1
 */
int tmin(void) {
	/*最小数为0x80000000，用1移位即可*/
  return 1<<31;
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
	/*减 n计算位移的位数，左移再右移之后判断与原数异或，
	若两者相同表示x可被表示为一个n位整数，！0为1*/
	int bit=33+~n;
	int result=x^((x<<bit)>>bit);
	result=!result;
  return result;
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
	/*先计算出偏置量，加上右移即可*/
	int result=x>>31;
	int bit=~((~0)<<n);
	int bias=result&bit;
	result=(x+bias)>>n;
    return result;
}
/*
 * negate - return -x
 *   Example: negate(1) = -1.
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 5
 *   Rating: 2
 */
int negate(int x) {
	/*取反加一*/
	int result=~x+1;
  return result;
}
/*
 * isPositive - return 1 if x > 0, return 0 otherwise
 *   Example: isPositive(-1) = 0.
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 8
 *   Rating: 3
 */
int isPositive(int x) {
	/*判断符号位，判断是否为0，按位与即可*/
	int result=(~(x>>31))&1;
	result=(!!x)&result;
  return result;
}
/*
 * isLessOrEqual - if x <= y  then return 1, else return 0
 *   Example: isLessOrEqual(4,5) = 1.
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 24
 *   Rating: 3
 */
int isLessOrEqual(int x, int y) {
	/*取符号位，分三种情况，符号不同，符号相同且不等，相等
	符号不同时，对x符号取非两次，y符号取非一次，按位与即可
	符号相同不等，两者相减，判断差的符号位即可
	判断是否相等按位异或即可*/
	int signBitX=x>>31;
	int signBitY=y>>31;
	int different=(!!signBitX)&(!signBitY);
	int subtract=(x+(~y+1))>>31;
	int same=(!!subtract)&(!(signBitX^signBitY));
	int equal=!(x^y);
	int result=!!(same|different|equal);
  return result;
}
/*
 * ilog2 - return floor(log base 2 of x), where x > 0
 *   Example: ilog2(16) = 4
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 90
 *   Rating: 4
 */
int ilog2(int x) {
	/*使用二分法，先右移16位后若大于0即得到有效数字，否则得到0，
	判断最高位是否为0，若不为0，则包含2的16次方。即得到最高位的log数。
	同理其他*/
	int result=0;
	result=(!!(x>>16))<<4;
	result=result+((!!(x>>(result+8)))<<3);
	result=result+((!!(x>>(result+4)))<<2);
	result=result+((!!(x>>(result+2)))<<1);
	result=result+(!!(x>>(result+1)));
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
	/*将该数字的最高位进行取反。然后分类讨论，比较最高位为零时，
	是否大于 0 11111111 0000 0000 0000 0000 0000。即阶为最大时，此时，为NaN，返回即可
	否则返回取反结果*/
	unsigned x=uf&0x7fffffff;
	unsigned result=uf^0x80000000;
	if(x>0x7f800000)
		result=uf;
 return result;
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
	/*Int型整数在转化为float型数的时候需要注意的是负数的表示，
	在int型中负数使用补码的形式表示，而float直接表示，所以先要对负数进行转化。
	然后进行循环，每移位一次阶码记录一次。最后把得到的三部分综合起来即可*/
	unsigned shiftleft=0;
	unsigned aftershift,tmp,flag;
	unsigned absx=x;
	unsigned sign=0;
	if(0==x)
		return 0;
	if(x<0){
		sign=0x80000000;
		absx=-x	;
	}
	aftershift=absx;
	while (1){
		tmp=aftershift;
		aftershift<<=1;
		shiftleft++;
		if(tmp&0x80000000)
			break;
	}
	if ((aftershift & 0x01ff)>0x0100)
		flag=1;
	else if ((aftershift&0x03ff)==0x0300)
		flag=1;
	else
		flag=0;
	return sign + (aftershift>>9)+((159-shiftleft)<<23)+flag;
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
	/*先进行判断，如果阶码全零，则需要对尾数进行移位操作，并令阶码加一，判定符号位。
	如果阶码不为零，则只需要领阶码加一即可。当阶码全为1，不操作直接输出*/
  unsigned f=uf;
  if ((f & 0x7f800000)==0){
	  f=((f&0x007fffff)<<1)|(0x80000000&f);
  } else if((f&0x7f800000)!=0x7f800000){
	  f=f+0x00800000;
  }
  return f;
}
