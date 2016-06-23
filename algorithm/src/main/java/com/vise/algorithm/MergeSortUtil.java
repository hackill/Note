package com.vise.algorithm;

/**
 * 归并排序
 * Created by xyy on 16/6/22.
 */
public class MergeSortUtil {

    public static StringBuilder[] stringBuilders;

    public static int[] mergeSortTest(int[] A){
        if(A == null || A.length <= 1){
            return A;
        }
        stringBuilders = new StringBuilder[(int) Math.ceil(Math.sqrt(A.length))];
        mergeSortTest(A, 0, A.length-1, (int) Math.ceil(Math.sqrt(A.length)));
        return A;
    }

    public static int[] mergeSortTest(int[] A, int low, int high, int i){
        int mid = (low + high) / 2;
        if (low < high) {
            i--;
            mergeSortTest(A, low, mid, i);
            mergeSortTest(A, mid + 1, high, i);
            mergeTest(A, low, mid, high, i);
        }
        return A;
    }

    private static void mergeTest(int[] A, int low, int mid, int high, int index){
        int[] temp = new int[high-low+1];
        int i = low;
        int j = mid + 1;
        int k = 0;
        while (i <= mid && j <= high) {
            if (A[i] < A[j]) {
                temp[k++] = A[i++];
            } else {
                temp[k++] = A[j++];
            }
        }
        while (i <= mid) {
            temp[k++] = A[i++];
        }
        while (j <= high) {
            temp[k++] = A[j++];
        }
        for (int t = 0; t < temp.length; t++) {
            A[t+low] = temp[t];
        }
        stringBuilders[index] = new StringBuilder();
        stringBuilders[index].append("\tStep"+(index+1)+":");
        for(int m = 0; m < A.length; m++){
            stringBuilders[index].append(A[m]);
            if(m != (A.length - 1)){
                stringBuilders[index].append(",");
            } else{
                stringBuilders[index].append("\n\n");
            }
        }
    }

    public static int[] mergeSort(int[] A){
        if(A == null || A.length <= 1){
            return A;
        }
        mergeSort(A, 0, A.length-1);
        return A;
    }

    private static int[] mergeSort(int[] A, int low, int high){
        int mid = (low + high) / 2;
        if (low < high) {
            mergeSort(A, low, mid);
            mergeSort(A, mid + 1, high);
            merge(A, low, mid, high);
        }
        return A;
    }

    private static void merge(int[] A, int low, int mid, int high){
        int[] temp = new int[high-low+1];
        int i = low;
        int j = mid + 1;
        int k = 0;
        while (i <= mid && j <= high) {
            if (A[i] < A[j]) {
                temp[k++] = A[i++];
            } else {
                temp[k++] = A[j++];
            }
        }
        while (i <= mid) {
            temp[k++] = A[i++];
        }
        while (j <= high) {
            temp[k++] = A[j++];
        }
        for (int t = 0; t < temp.length; t++) {
            A[t+low] = temp[t];
        }
    }
}
