package com.vise.algorithm;

/**
 * 希尔排序
 * Created by xyy on 16/6/20.
 */
public class ShellSortUtil {

    public static StringBuilder[] stringBuilders;

    public static int[] shellSort(int[] A){
        if(A == null || A.length <= 1){
            return A;
        }
        int d = (int) Math.ceil((double) A.length / 2);
        while(d >= 1){
            shellInsertSort(A, d);
            if(d/2 >= 1){
                d = (int) Math.ceil((double) d / 2);
            } else{
                d = d / 2;
            }
        }
        return A;
    }

    private static void shellInsertSort(int[] A, int d){
        for(int i = d; i < A.length; i++){
            if(A[i] < A[i-d]){
                int j = i - d;
                int key = A[i];
                while(j >= 0 && A[j] > key){
                    A[j+d] = A[j];
                    j -= d;
                }
                A[j+d] = key;
            }
        }
    }

    public static int[] shellSortTest(int[] A){
        if(A == null || A.length <= 1){
            return A;
        }
        int d = (int) Math.ceil((double)A.length / 2);
        int i = 0;
        stringBuilders = new StringBuilder[(int) Math.ceil(Math.sqrt(A.length))];
        while(d >= 1){
            shellInsertSort(A, d);
            if(d/2 >= 1){
                d = (int) Math.ceil((double) d / 2);
            } else{
                d = d / 2;
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
            i++;
        }
        return A;
    }
}
