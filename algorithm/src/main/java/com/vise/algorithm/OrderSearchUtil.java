package com.vise.algorithm;

/**
 * 顺序查找
 * Created by xyy on 16/6/23.
 */
public class OrderSearchUtil {

    public static int orderSearch(int[] A,int key){
        if(A == null || A.length <= 0){
            return -1;
        }
        for(int i = 0; i < A.length; i++){
            if(key == A[i]){
                return i;
            }
        }
        return -1;
    }
}
