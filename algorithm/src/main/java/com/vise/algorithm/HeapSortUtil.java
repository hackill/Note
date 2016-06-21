package com.vise.algorithm;

/**
 * 堆排序
 * Created by xyy on 16/6/19.
 */
public class HeapSortUtil {

    public static StringBuilder[] stringBuilders;

    public static int[] heapSortTest(int[] A){
        if(A == null || A.length <= 1){
            return A;
        }
        buildMaxHeap(A);
        int heapSize = A.length;
        stringBuilders = new StringBuilder[heapSize-1];
        for(int i = heapSize-1; i > 0; i--){
            /**
             * 将堆顶最大元素与未调整元素的尾元素进行交换
             */
            int temp = A[i];
            A[i] = A[0];
            A[0] = temp;
            //调整剩余元素为最大堆
            heapAdjust(A, 0, i);
            stringBuilders[heapSize-i-1] = new StringBuilder();
            stringBuilders[heapSize-i-1].append("\tStep"+(heapSize-i)+":");
            for(int k = 0; k < A.length; k++){
                stringBuilders[heapSize-i-1].append(A[k]);
                if(k != (A.length - 1)){
                    stringBuilders[heapSize-i-1].append(",");
                } else{
                    stringBuilders[heapSize-i-1].append("\n\n");
                }
            }
        }
        return A;
    }

    public static int[] heapSort(int[] A){
        if(A == null || A.length <= 1){
            return A;
        }
        buildMaxHeap(A);
        int heapSize = A.length;
        for(int i = heapSize-1; i > 0; i--){
            int temp = A[i];
            A[i] = A[0];
            A[0] = temp;
            heapAdjust(A, 0, i);
        }
        return A;
    }

    /**
     * 初始最大堆
     * @param A
     */
    private static void buildMaxHeap(int[] A){
        for (int i = (A.length -1) / 2 ; i >= 0; --i){
            heapAdjust(A, i, A.length);
        }
    }

    /**
     * 调整堆，保证堆顶为最大元素
     * @param A 待调整数组
     * @param s 位置
     * @param length 剩余待调整长度
     */
    private static void heapAdjust(int[] A, int s, int length){
        int temp  = A[s];
        int child = 2*s+1;
        while (child < length) {
            if(child+1 < length && A[child] < A[child+1]){
                child++;
            }
            if(A[s] < A[child]){
                A[s] = A[child];
                s = child;
                child = 2*s+1;
            } else{
                 break;
            }
            A[s] = temp;
        }
    }
}
