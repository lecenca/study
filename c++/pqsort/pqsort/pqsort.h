#pragma once

#include <queue>
#include <mutex>
#include <iterator>
#include <algorithm>
#include <vector>


class Task {
public:
    bool finish;
    int* begin;
    int* end;

    Task(int* a, int* b) :finish(false), begin(a), end(b) {}
    Task() :finish(true), begin(nullptr), end(nullptr) {}
};

class ShareData {
public:
    std::queue<Task> taskQueue;
    unsigned numOfStoppingThread = 0;
    std::condition_variable cv;
    std::mutex mtx;
};

void processTask(int* begin, int* end, unsigned minLength, ShareData& shareData) {
    int *left, *right, benchmark;
    for (;;) {
        if (std::distance(begin, end) <= minLength) {
            std::sort(begin, end);
            return;
        }

        left = begin;
        right = end - 1;
        benchmark = *begin;
        while (left != right)
        {
            while (right != left && *right >= benchmark) --right;
            *left = *right;
            *right = benchmark;
            while (left != right && *left <= benchmark) ++left;
            *right = *left;
            *left = benchmark;
        }
        *left = benchmark;

        shareData.mtx.lock();
        shareData.taskQueue.push(Task(begin, left));
        shareData.cv.notify_all();
        shareData.mtx.unlock();
        begin = left + 1;
    }
}

void subProgram(ShareData& shareData, unsigned minLength) {
    auto& taskQueue = shareData.taskQueue;
    unsigned& numOfStoppingThread = shareData.numOfStoppingThread;
    std::unique_lock<std::mutex> ulk{ shareData.mtx,std::defer_lock };
    for (;;) {
        ulk.lock();
        if (taskQueue.empty()) {
            if (numOfStoppingThread == std::thread::hardware_concurrency() - 1) {
                //it means sorting is finish
                for (unsigned i = 1; i <= std::thread::hardware_concurrency() - 1; ++i)
                    taskQueue.push(Task());//let other threads know sort is finish
                shareData.cv.notify_all();
                return;
            }
            else {
                ++shareData.numOfStoppingThread;
                shareData.cv.wait(ulk, [&taskQueue] {return !taskQueue.empty(); });
                --shareData.numOfStoppingThread;
            }
        }
        Task task = taskQueue.front();
        taskQueue.pop();
        ulk.unlock();
        if (task.finish)
            return;
        processTask(task.begin, task.end, minLength, shareData);
    }
}

void pqsort(int* begin, int* end) {
    unsigned minLength = std::distance(begin, end);
    if (minLength <= 1000) {
        std::sort(begin, end);
        return;
    }
    minLength = minLength / (2 * std::thread::hardware_concurrency());
    ShareData shareData;
    shareData.taskQueue.push(Task(begin, end));
    std::vector<std::thread> threadPool;
    for (unsigned i = 1; i <= std::thread::hardware_concurrency() - 1; ++i)
        threadPool.push_back(std::thread(subProgram, std::ref(shareData), minLength));
    subProgram(shareData, minLength);
    for (unsigned i = 0; i <= std::thread::hardware_concurrency() - 2; ++i)
        threadPool[i].join();
}