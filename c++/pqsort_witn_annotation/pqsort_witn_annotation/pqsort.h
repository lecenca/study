#pragma once

#include <queue>
#include <mutex>
#include <iterator>
#include <algorithm>
#include <vector>
#include <iostream>


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

void processTask(int* begin, int* end, ShareData& shareData) {
	shareData.mtx.lock();
	std::cout << std::this_thread::get_id() << " is processing task ,";
	std::cout << "and now taskQueue's size is " << shareData.taskQueue.size() << std::endl;
	shareData.mtx.unlock();
	int *left, *right, t, benchmark;
	for (;;) {
		if (std::distance(begin, end) <= 500) {
			std::sort(begin, end);
			return;
		}

		left = begin;
		right = end - 1;
		benchmark = *begin;
		while (left != right)
		{
			while (right != left && *right >= benchmark) --right;
			t = *left;
			*left = *right;
			*right = t;
			while (left != right && *left <= benchmark) ++left;
			t = *right;
			*right = *left;
			*left = t;
		}
		*left = benchmark;

		shareData.mtx.lock();
		shareData.taskQueue.push(Task(begin, left));
		shareData.mtx.unlock();

		shareData.mtx.lock();
		std::cout << std::this_thread::get_id() << " notify all ,";
		std::cout << " sort is unfinish ";
		std::cout << "and now taskQueue's size is " << shareData.taskQueue.size() << std::endl;
		shareData.mtx.unlock();

		shareData.mtx.lock();
		shareData.cv.notify_all();
		shareData.mtx.unlock();
		begin = left + 1;
	}
}

void subProgram(ShareData& shareData) {
	shareData.mtx.lock();
	std::cout << std::this_thread::get_id() << " start\n";
	shareData.mtx.unlock();
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
				std::cout << std::this_thread::get_id() << " notify all ";
				std::cout << "sort is finish\n";
				shareData.cv.notify_all();
				return;
			}
			else {
				++shareData.numOfStoppingThread;
				std::cout << std::this_thread::get_id() << " wait\n";
				shareData.cv.wait(ulk, [&taskQueue] {return !taskQueue.empty(); });
				--shareData.numOfStoppingThread;
				std::cout << std::this_thread::get_id() << " be notified ,";
				std::cout << " when taskQueue's size is " << taskQueue.size() << std::endl;
			}
		}
		Task task = taskQueue.front();
		taskQueue.pop();
		ulk.unlock();
		if (task.finish)
			return;
		processTask(task.begin, task.end, shareData);
	}
}

void pqsort(int* begin, int* end) {
	ShareData shareData;
	shareData.mtx.lock();
	std::cout << std::this_thread::get_id() << " is main thread\n";
	shareData.mtx.unlock();
	shareData.taskQueue.push(Task(begin, end));
	std::vector<std::thread> threadPool;
	for (unsigned i = 1; i <= std::thread::hardware_concurrency() - 1; ++i)
		threadPool.push_back(std::thread(subProgram, std::ref(shareData)));
	subProgram(shareData);
	for (unsigned i = 0; i <= std::thread::hardware_concurrency() - 2; ++i)
		threadPool[i].join();
}