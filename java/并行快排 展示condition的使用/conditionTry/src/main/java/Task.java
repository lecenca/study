public class Task{
    int[] arr;
    private int begin;
    private int end;
    private boolean workFinish;

    Task(){
        workFinish = true;
    }

    Task(int[] arr, int begin, int end){
        this.arr = arr;
        this.begin = begin;
        this.end = end;
        workFinish = false;
    }

    public int size(){
        return end - begin;
    }

    public int[] getArr() {
        return arr;
    }

    public Task(int begin, int end){
        this.begin = begin;
        this.end = end;
        workFinish = false;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public boolean isWorkFinish() {
        return workFinish;
    }

    public void setWorkFinish(boolean workFinish) {
        this.workFinish = workFinish;
    }
}