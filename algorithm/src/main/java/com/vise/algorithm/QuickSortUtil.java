package com.vise.algorithm;

/**
 * 快速排序
 * Created by xyy on 16/6/19.
 */
public class QuickSortUtil {

    public static StringBuilder[] stringBuilders;

    public static int[] quickSortTest(int[] A){
        if(A == null || A.length <= 1){
            return A;
        }
        stringBuilders = new StringBuilder[A.length];
        recursiveQuickSortTest(A, 0, A.length-1, 0);
        return A;
    }

    private static void recursiveQuickSortTest(int[] A, int p, int q, int i) {
        if(p < q){
            stringBuilders[i] = new StringBuilder();
            stringBuilders[i].append("\tStep"+i+":");
            for(int k = 0; k < A.length; k++){
                stringBuilders[i].append(A[k]);
                if(k != (A.length - 1)){
                    stringBuilders[i].append(",");
                } else{
                    stringBuilders[i].append("\n\n");
                }
            }
            i++;
            int r = partition(A, p, q);
            recursiveQuickSortTest(A, p, r-1, i);
            recursiveQuickSortTest(A, r+1, q, i);
        }
    }

    public static int[] quickSort(int[] A){
        if(A == null || A.length <= 1){
            return A;
        }
        recursiveQuickSort(A, 1, A.length);
        return A;
    }

    private static void recursiveQuickSort(int[] A, int p, int q) {
        if(p < q){
            int r = partition(A, p, q);
            recursiveQuickSort(A, p, r-1);
            recursiveQuickSort(A, r+1, q);
        }
    }

    private static int partition(int[] A, int p, int q){
        int pivot = A[p];
        while(p < q){
            while(p < q && A[q] >= pivot) --q;
            int tempFirst = A[q];
            A[q] = A[p];
            A[p] = tempFirst;
            while(p < q && A[p] <= pivot) ++p;
            int tempSecond = A[q];
            A[q] = A[p];
            A[p] = tempSecond;
        }
        return p;
    }

}
