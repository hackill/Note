package com.vise.algorithm;

/**
 * 直接选择排序
 * Created by xyy on 16/6/19.
 */
public class SelectionSortUtil {

    public static StringBuilder[] stringBuilders;

    public static int[] selectionSort(int[] A){
        if(A == null || A.length <= 1){
            return A;
        }
        for(int i = 0; i < A.length - 1; i++){
            int minIndex = i;
            for(int j = i+1; j < A.length; j++){
                if(A[minIndex] > A[j]){
                    minIndex = j;
                }
            }
            //swap A[minIndex],A[i]
            int temp = A[minIndex];
            A[minIndex] = A[i];
            A[i] = temp;
        }
        return A;
    }

    public static int[] selectionSortTest(int[] A){
        if(A == null || A.length <= 1){
            return A;
        }
        stringBuilders = new StringBuilder[A.length-1];
        for(int i = 0; i < A.length - 1; i++){
            int minIndex = i;
            for(int j = i+1; j < A.length; j++){
                if(A[minIndex] > A[j]){
                    minIndex = j;
                }
            }
            //swap A[minIndex],A[i]
            int temp = A[minIndex];
            A[minIndex] = A[i];
            A[i] = temp;
            stringBuilders[i] = new StringBuilder();
            stringBuilders[i].append("\tStep"+(i+1)+":");
            for(int k = 0; k < A.length; k++){
                stringBuilders[i].append(A[k]);
                if(k != (A.length - 1)){
                    stringBuilders[i].append(",");
                } else{
                    stringBuilders[i].append("\n\n");
                }
            }
        }
        return A;
    }
}
