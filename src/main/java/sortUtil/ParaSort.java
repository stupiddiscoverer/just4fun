package sortUtil;

import sortUtil.Sort;

import java.util.ArrayList;

public class ParaSort{
    private static ArrayList<Integer> splitIndexes;
    private static int processors, sortedCount, mergedCount;

    private static int[] copy;

    /**
     * 如果cpu开启了超线程，请调用这个方法，因为这个方法执行时，cpu的空闲时间很少，大概就读写内存时有点空闲
     */
    public static void sortWithHalfProcessors(int[] arr) {
        processors = Runtime.getRuntime().availableProcessors() / 2;
        sort(arr, processors);
    }

    public static void sortWithAllProcessors(int[] arr) {
        processors = Runtime.getRuntime().availableProcessors();
        sort(arr, processors);
    }

    public static void sort(int arr[], int threads) {
        copy = arr.clone();

        processors = threads;

        if (processors <= 1 || arr.length < processors) {
            Sort.mergeSort(arr);
            return;
        }

        splitIndexes = new ArrayList<>();
        int partLen = arr.length / processors;
        for (int i=1; i<processors; i++) {
            splitIndexes.add(partLen * i);
        }
        splitIndexes.add(arr.length);

        int start = 0;
        for (int i=0; i<processors-1; i++) {
            int end = splitIndexes.get(i);
            new SortThread(arr, copy, start, end).start();
            System.out.println("_______线程" + end + "已启动————————————");
            start = end;
        }
        System.out.println("processors = " + processors);
        Sort.mergeSortPart(arr, copy, start, splitIndexes.get(processors - 1));
        sortedCount++;

//                归并processors个有序子数组。。

        mergeSplitSortedArr(arr, copy, splitIndexes);
    }

    private static class SortThread extends Thread{
        private int start, end;
        private int[] arr, copy;

        public SortThread(int[] arr, int[] copy, int start, int end) {
            this.start = start;
            this.end = end;
            this.arr = arr;
            this.copy = copy;
        }

        @Override
        public void run() {
            Sort.mergeSortPart(arr, copy, start, end);   //已验证通过，
            sortedCount++;
        }

    }

    private static class MergeThread extends Thread{
        private int start, mid, end;
        private int[] arr, copy;

        public MergeThread(int[] arr, int[] copy, int start, int mid, int end) {
            this.start = start;
            this.mid = mid;
            this.end = end;
            this.arr = arr;
            this.copy = copy;
        }

        @Override
        public void run() {
            Sort.mergeTwoPart(arr, copy, start, mid, end);
            mergedCount++;
        }
    }

    private static void waitForSorted() {
        while (sortedCount < processors) {
            wait(10);
        }
    }

    //这个最好也用多线程。。。
    private static void mergeSplitSortedArr(int[] arr, int[] copy, ArrayList<Integer> splitIndexs) {

        boolean needCopy = false;
        ArrayList<Integer> indexRecord = (ArrayList<Integer>) splitIndexs.clone();

//        Sort.printArray(arr);
        waitForSorted();

        while (indexRecord.size() > 1) {
            mergedCount = 0;
            int start = 0;
            for (int i=0; i<indexRecord.size(); i++) {
                if (i+2 < indexRecord.size()) {
//                    Sort.mergeTwoPart(arr, copy, start, indexRecord.get(i), indexRecord.get(i+1));
                    new MergeThread(arr, copy, start, indexRecord.get(i), indexRecord.get(i + 1)).start();
                    i++;
                    start = indexRecord.get(i);
                    continue;
                }

                if (i+2 == indexRecord.size()) {
                    Sort.mergeTwoPart(arr, copy, start, indexRecord.get(i), indexRecord.get(i+1));
//                    new MergeThread(arr, copy, start, indexRecord.get(i), indexRecord.get(i+1)).run();
                } else {
                    Sort.mergeTwoPart(arr, copy, start, indexRecord.get(i), indexRecord.get(i));
//                    new MergeThread(arr, copy, start, indexRecord.get(i), indexRecord.get(i)).run();
                }
                break;
            }

            for(int i=0; i<indexRecord.size()-1; i++) {    //末尾的记录代表arr长度，不能删除
                indexRecord.remove(i);      //合并后，删除0，2，4...的节点，长度减半
            }

            int[] temp = arr;
            arr = copy;
            copy = temp;
            needCopy = !needCopy;

            mergedCount++;
            waitForMerged(indexRecord.size());
        }


        if (needCopy) {
            System.arraycopy(arr, 0, copy, 0, arr.length);
        }
    }

    private static void waitForMerged(int size) {
        while (mergedCount < size) {
            wait(1);
        }
    }

    public static void wait(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
