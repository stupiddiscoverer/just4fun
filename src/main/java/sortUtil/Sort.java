package sortUtil;

import java.util.List;

public class Sort {
    /**
     * 快速排序
     * @param arr
     * @return
     */
    public static void quickSort(int arr[]) {
        quickSort(arr, 0, arr.length - 1);
    }
    public static void quickSort(int arr[], int left, int right) {
        if (arr == null || arr.length-1 < right || left < 0) {
            System.out.println("参数错误");
            return;
        }
        qkSort(arr, left, right);
    }
    private static void qkSort(final int arr[], int left, int right) {
        if (left >= right) {
            return;
        }
        int l = left, r = right;
        int temp = arr[l];

        while (l < r) {
            while (l < r && arr[r] >= temp) {
                r--;
            }
            arr[l] = arr[r];
            while (l < r && arr[l] <= temp) {
                l++;
            }
            arr[r] = arr[l];
        }

        arr[l] = temp;

        quickSort(arr, left, l-1);
        quickSort(arr, l+1, right);
    }


    public static void mergeSort(int arr[]) {
        int[] copy = new int[arr.length];
        mergeSortPart(arr, copy, 0, arr.length);
    }

    public static void mergeSortPart(int[] arr, int[] copy, int start, int end) {
        int len = end - start;
        boolean needCopy = false;

        for (int i=1; i<len; i=i*2) {       //生成该次循环的合并数组长度，开始是长度为1的数组合并，结束是长度为 min(>len/2)(2的指数数) 的数组合并
            for (int j=start+i; j<end+i; j+=i) {    //开始每2段子数组执行一次合并，j为2个子数组的分割坐标，即第二个子数组的开始坐标,

                if (j+i > end) {        //考虑是否到数组尾端，这时，len作为第二个数组的边界而不是j+i
                    mergeTwoPart(arr, copy, j-i, j, end);
                } else {
                    mergeTwoPart(arr, copy, j-i, j, j+i);
                }
                j += i;
            }
            int[] temp = arr;
            arr = copy;     //arr永远占领归并果实
            copy = temp;
            needCopy = !needCopy;
        }

        if (needCopy)
            System.arraycopy(arr, start, copy, start, len);
    }

    /**
     *
     * @param arr
     * @param copy
     * @param a  第一个子数组开头
     * @param b  第二个子数组开头
     * @param end 第二个子数组结尾
     */
    public static void mergeTwoPart(int[] arr, int[] copy, int a, int b, int end) {     //执行一次合并
        int mid = b;
        if (mid >= end) {               //考虑到只有第一个数组，
            if (end - a >= 0) System.arraycopy(arr, a, copy, a, end - a);
            return;
        }
        for (int k = a; k < end; k++) {   //k从j-i到j+i，每次存两个子数组较小的数然后跳向下一个坐标
            if (arr[a] <= arr[b]) {
                copy[k] = arr[a];
                a++;
                if (a == mid) {
                    while (b < end) {
                        k++;
                        copy[k] = arr[b];
                        b++;
                    }
                }
            } else {
                copy[k] = arr[b];
                b++;
                if (b == end) {
                    while (a < mid) {
                        k++;
                        copy[k] = arr[a];
                        a++;
                    }
                }
            }
        }
    }

    public static void printArray(int[] arr) {
        int len = arr.length;
        if (len < 100) {
            System.out.print("------------------------------------");
            for (int i : arr) {
                System.out.print(i + "\t");
            }
            System.out.println("------------------------------------");
            return;
        }
        int mount = len / 100;
        System.out.print("------------------------------------");
        for (int i=0; i<len; i+=mount) {
            if (i % (10 * mount) == 0){
                System.out.println();
            }
            System.out.print(arr[i] + "  \t");
        }
        System.out.println("\n------------------------------------");
    }

    public static void printArray(List<Integer> arr) {
        int len = arr.size();
        int mount = len / 100;
        System.out.print("------------------------------------");
        for (int i=0; i<len; i+=mount) {
            if (i % (10 * mount) == 0){
                System.out.println();
            }
            System.out.print(arr.get(i) + "  \t");
        }
        System.out.println("\n------------------------------------");
    }
}
