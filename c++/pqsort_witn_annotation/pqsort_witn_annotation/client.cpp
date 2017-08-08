
// pqsort.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"

#include <cstdlib>
#include <algorithm>
#include <iostream>

#include "pqsort.h"

int main()
{
    const unsigned length = 10000;
    int a[length];
    int b[length];
    for (int i = 0; i < length; ++i)
        a[i] = b[i] = std::rand();
    std::sort(a, a + length);
    std::cout << "std::sort is finish\n";
    pqsort(b, b + length);
    std::cout << "my sort is finish\n";
    int i = 0;
    for (; i < length; ++i) {
        if (a[i] != b[i]) {
            std::cout << "wrong\n";
            break;
        }
    }
    if (i == length)
        std::cout << "correct\n";
    return 0;
}

