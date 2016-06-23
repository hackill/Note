package com.vise.algorithm;

import java.util.ArrayList;

/**
 * 索引查找
 * Created by xyy on 16/6/23.
 */
public class IndexSearchUtil {

    public static int indexSearch(int[] A, int key, int indexRule){
        if(A == null || A.length <= 0 || indexRule == 0){
            return -1;
        }
        IndexItem item = null;
        IndexItem[] indexItems = createIndexList(A, indexRule);
        if(indexItems == null || indexItems.length <= 0){
            return -1;
        }
        int index = key / indexRule;
        for (int i = 0; i < indexItems.length; i++){
            if (indexItems[i].index == index){
                item = new IndexItem(index, indexItems[i].start,
                        indexItems[i].length);
                break;
            }
        }
        if (item == null){
            return -1;
        }
        for (int i = item.start; i < item.start + item.length; i++){
            if (A[i] == key){
                return i;
            }
        }
        return -1;
    }

    private static IndexItem[] createIndexList(int[] A, int indexRule){
        if(A == null || A.length <= 0 || indexRule == 0){
            return null;
        }
        ArrayList<IndexItem> indexItemArrayList = new ArrayList<>();
        for(int i = 0; i < A.length; i++){
            //根据索引规则建立索引表，规则不一，此处省略
        }
        IndexItem[] indexItems = (IndexItem[]) indexItemArrayList.toArray();
        return indexItems;
    }

    // 索引项实体
    static class IndexItem {
        // 对应主表的值
        public int index;
        // 主表记录区间段的开始位置
        public int start;
        // 主表记录区间段的长度
        public int length;

        public IndexItem() {
        }

        public IndexItem(int index, int start, int length) {
            this.index = index;
            this.start = start;
            this.length = length;
        }
    }
}
