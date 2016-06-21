package com.vise.algorithm;

/**
 * 冒泡排序
 * Created by xyy on 16/6/19.
 */
public class BubbleSortUtil {

    public static StringBuilder[] stringBuilders;

    public static int[] bubbleSort(int[] A){
        if(A == null || A.length <= 1){
            return A;
        }
        for(int i = 0; i < A.length; i++){
            for(int j = A.length-1; j > i; j--){
                if(A[j] < A[j-1]){
                    int temp = A[j];
                    A[j] = A[j-1];
                    A[j-1] = temp;
                }
            }
        }
        return A;
    }

    public static int[] bubbleSortTest(int[] A){
        if(A == null || A.length <= 1){
            return A;
        }
        stringBuilders = new StringBuilder[A.length];
        for(int i = 0; i < A.length; i++){
            for(int j = A.length-1; j > i; j--){
                if(A[j] < A[j-1]){
                    int temp = A[j];
                    A[j] = A[j-1];
                    A[j-1] = temp;
                }
            }
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
