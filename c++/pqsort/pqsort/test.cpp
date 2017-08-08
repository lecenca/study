// pqsort.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"

#include <cstdlib>
#include <algorithm>
#include <iostream>
#include <chrono>

#include "pqsort.h"

void test(unsigned length) {
    int *a = new int[length];
    int *b = new int[length];

    for (unsigned i = 0; i < length; ++i)
        a[i] = b[i] = std::rand();

    auto start = std::chrono::high_resolution_clock::now();
    std::sort(a, a + length);
    auto end = std::chrono::high_resolution_clock::now();
    auto stdSortDuration = std::chrono::duration_cast<std::chrono::microseconds>(end - start);

    start = std::chrono::high_resolution_clock::now();
    pqsort(b, b + length);
    end = std::chrono::high_resolution_clock::now();
    auto mySortDuration = std::chrono::duration_cast<std::chrono::microseconds>(end - start);

    bool correct = true;
    for (unsigned i = 0; i < length; ++i) {
        if (a[i] != b[i]) {
            correct = false;
            break;
        }
    }

    std::cout << "test : " << length << " int" << std::endl;
    std::cout << "result : " << (correct ? "correct" : "wrong") << std::endl;
    std::cout << "std::sort uses " << (double)stdSortDuration.count() / 1000 << " ms" << std::endl;
    std::cout << "pqsort uses " << (double)mySortDuration.count() / 1000 << " ms" << std::endl;
    std::cout << std::endl;

}


int main()
{
    test(1);
    test(2);
    test(1000);
    test(10000);
    test(1000000);
    test(10000000);
    return 0;
}

