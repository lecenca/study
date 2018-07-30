import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Sorter {

    public static void pqsort(int[] arr){
        if(arr.length<10000){
            Arrays.sort(arr);
            return;
        }

        Lock lock = new ReentrantLock();
        int threadCount = Runtime.getRuntime().availableProcessors();
        int threshole = (int) (arr.length / (2*threadCount));
        if(threshole<10000)
            threshole = 10000;

        ShareData shareData = new ShareData(threadCount,threshole);
        Thread[] threads = new Thread[threadCount-1];
        for(int i = 0;i<threadCount-1;++i)
            threads[i] = new ProcessThread(shareData);
        shareData.addTask(new Task(arr,0,arr.length));

        for(int i = 0;i<threads.length;++i)
            threads[i].start();

        processTask(shareData);
    }

    public static class ProcessThread extends Thread {
        private ShareData shareData;

        public ProcessThread(ShareData shareData){
            this.shareData = shareData;
        }
        public ProcessThread(){}

        public void run() {
            processTask(shareData);
        }
    }

    private static void processTask(ShareData shareData){
        Task task;
        for(;;){
            task = shareData.getTask();
            if(task.isWorkFinish())
                return;

            while(task.size()>shareData.getThreshole())
                task = cut(task,shareData);
            deal(task);
        }
    }


    private static Task cut(Task task, ShareData shareData){
        int left = task.getBegin();
        int right = task.getEnd() - 1;
        int[] arr = task.getArr();
        int benchmark = arr[left];

        while (left != right){
            while (right != left && arr[right] >= benchmark) --right;
            arr[left] = arr[right];
            arr[right] = benchmark;
            while (left != right && arr[left] < benchmark) ++left;
            arr[right] = arr[left];
            arr[left] = benchmark;
        }
        arr[left] = benchmark;

        shareData.addTask(new Task(arr,task.getBegin(),left));
        return new Task(arr,left+1,task.getEnd());
    }

    private static void deal(Task task){
        Arrays.sort(task.getArr(),task.getBegin(),task.getEnd());
    }
}
