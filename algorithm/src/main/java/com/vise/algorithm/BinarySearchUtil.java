package com.vise.algorithm;

/**
 * 二分查找
 * Created by xyy on 16/6/23.
 */
public class BinarySearchUtil {

    public static int binarySearch(int[] A, int key){
        if(A == null || A.length <= 0){
            return -1;
        }
        int low = 0;
        int high = A.length - 1;
        while ((low <= high) && (low < A.length) && (high < A.length)) {
            int middle = (high + low) >> 1;
            if (key == A[middle]) {
                return middle;
            } else if (key < A[middle]) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return -1;
    }
}
