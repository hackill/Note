package com.vise.algorithm;

/**
 * 直接插入排序
 * Created by xyy on 16/6/13.
 */
public class InsertionSortUtil {

    public static StringBuilder[] stringBuilders;

    public static int[] insertionSort(int[] A){
        if(A == null || A.length <= 1){
            return A;
        }
        for(int i = 1; i < A.length; i++){
            int j = i - 1;
            int key = A[i];
            while(j >= 0 && A[j] > key){
                A[j+1] = A[j];
                j--;
            }
            A[j+1] = key;
        }
        return A;
    }

    public static int[] insertionSortTest(int[] A){
        if(A == null || A.length <= 1){
            return A;
        }
        stringBuilders = new StringBuilder[A.length-1];
        for(int i = 1; i < A.length; i++){
            int j = i - 1;
            int key = A[i];
            while(j >= 0 && A[j] > key){
                A[j+1] = A[j];
                j--;
            }
            A[j+1] = key;
            stringBuilders[i-1] = new StringBuilder();
            stringBuilders[i-1].append("\tStep"+i+":");
            for(int k = 0; k < A.length; k++){
                stringBuilders[i-1].append(A[k]);
                if(k != (A.length - 1)){
                    stringBuilders[i-1].append(",");
                } else{
                    stringBuilders[i-1].append("\n\n");
                }
            }
        }
        return A;
    }
}
