package com.vise.algorithm;

import java.util.Arrays;

/**
 * 基数排序
 * Created by xyy on 16/6/22.
 */
public class RadixSortUtil {

    public static StringBuilder[] stringBuilders;

    public static int[] radixSort(int[] A, int radix, int d){
        if(A == null || A.length <= 1){
            return A;
        }
        int[] tmp = new int[A.length];
        int[] buckets = new int[radix];
        for (int i = 0, rate = 1; i < d; i++) {
            Arrays.fill(buckets, 0);
            System.arraycopy(A, 0, tmp, 0, A.length);
            for (int j = 0; j < A.length; j++) {
                int subKey = (tmp[j] / rate) % radix;
                buckets[subKey]++;
            }
            for (int j = 1; j < radix; j++) {
                buckets[j] = buckets[j] + buckets[j - 1];
            }
            for (int m = A.length - 1; m >= 0; m--) {
                int subKey = (tmp[m] / rate) % radix;
                A[--buckets[subKey]] = tmp[m];
            }
            rate *= radix;
        }
        return A;
    }

    public static int[] radixSortTest(int[] A, int radix, int d){
        if(A == null || A.length <= 1){
            return A;
        }
        int[] tmp = new int[A.length];
        int[] buckets = new int[radix];
        stringBuilders = new StringBuilder[d];
        for (int i = 0, rate = 1; i < d; i++) {
            Arrays.fill(buckets, 0);
            System.arraycopy(A, 0, tmp, 0, A.length);
            for (int j = 0; j < A.length; j++) {
                int subKey = (tmp[j] / rate) % radix;
                buckets[subKey]++;
            }
            for (int j = 1; j < radix; j++) {
                buckets[j] = buckets[j] + buckets[j - 1];
            }
            for (int m = A.length - 1; m >= 0; m--) {
                int subKey = (tmp[m] / rate) % radix;
                A[--buckets[subKey]] = tmp[m];
            }
            rate *= radix;
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
