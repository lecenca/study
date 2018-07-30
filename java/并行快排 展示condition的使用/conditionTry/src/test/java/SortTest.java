import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class SortTest {

    @Test
    public void testSort(){
        testArray(10000);
        testArray(100000);
        testArray(1000000);
        testArray(10000000);
        testArray(20000000);
        testArray(30000000);
        testArray(40000000);
    }

    private void testArray(int count){

        System.out.println("test array length "+count);

        int[] arr01 = new int[count];
        int[] arr02 = new int[count];
        int[] arr03 = new int[count];

        Random random = new Random();
        for(int i = 0;i<count;++i){
            arr01[i] = arr02[i] = arr03[i] = random.nextInt();
        }

        long startTime = System.currentTimeMillis();
        Sorter.pqsort(arr01);
        long endTime = System.currentTimeMillis();
        System.out.println("pqsort 用时 "+(endTime-startTime)+" ms");

        startTime = System.currentTimeMillis();
        Arrays.sort(arr02);
        endTime = System.currentTimeMillis();
        System.out.println("Arrays.sort 用时 "+(endTime-startTime)+" ms");

        startTime = System.currentTimeMillis();
        Arrays.parallelSort(arr03);
        endTime = System.currentTimeMillis();
        System.out.println("Arrays.parallelSort 用时 "+(endTime-startTime)+" ms");

        System.out.println();

        Assert.assertTrue(equal(arr01,arr02));
    }

    private boolean equal(int[] arr01, int[] arr02){
        if(arr01.length!=arr02.length)
            return false;
        for(int i = 0;i<arr01.length;++i){
            if(arr01[i]!=arr02[i])
                return false;
        }
        return true;
    }
}
