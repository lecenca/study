import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ShareData{
    private Stack<Task> tasks;
    private ReentrantLock lock;
    private Condition condition;
    private int threadCount;
    private int stopingThreadCount;
    private int threshole;

    public ShareData(int threadCount,int threshole){
        tasks = new Stack<Task>();
        lock = new ReentrantLock();
        condition = lock.newCondition();
        this.threadCount = threadCount;
        stopingThreadCount = 0;
        this.threshole = threshole;
    }

    public int getThreshole() {
        return threshole;
    }

    public void addTask(Task task){
        lock.lock();
        tasks.push(task);
        condition.signal();
        lock.unlock();
    }

    public Task getTask(){
        lock.lock();
        if(!tasks.empty()){
            Task task = tasks.pop();
            lock.unlock();
            return task;
        }else if(allOtherThreadStop()){
            for(int i = 1;i<=stopingThreadCount;++i)
                tasks.push(new Task());
            condition.signalAll();
            lock.unlock();
            return new Task();
        }else{
            while(tasks.empty()) {
                try {
                    ++stopingThreadCount;
                    condition.await();
                    --stopingThreadCount;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Task task = tasks.pop();
            lock.unlock();
            return task;
        }
    }

    private boolean allOtherThreadStop(){
        return 1==(threadCount-stopingThreadCount);
    }
}