package com.vise.note.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vise.algorithm.BubbleSortUtil;
import com.vise.algorithm.HeapSortUtil;
import com.vise.algorithm.InsertionSortUtil;
import com.vise.algorithm.MergeSortUtil;
import com.vise.algorithm.QuickSortUtil;
import com.vise.algorithm.RadixSortUtil;
import com.vise.algorithm.SelectionSortUtil;
import com.vise.algorithm.ShellSortUtil;
import com.vise.common_base.activity.BaseCustomBarActivity;
import com.vise.note.R;

/**
 * @Description: 排序
 * @author: <a href="http://xiaoyaoyou1212.360doc.com">DAWI</a>
 * @date: 2016-07-05 15:31
 */
public class AlgorithmActivity extends BaseCustomBarActivity {

    private TextView sortTv;
    private Button bubbleSortBtn;
    private Button heapSortBtn;
    private Button insertionSortBtn;
    private Button mergeSortBtn;
    private Button quickSortBtn;
    private Button radixSortBtn;
    private Button selectionSortBtn;
    private Button shellSortBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithm);
        init();
    }

    private void init() {
        sortTv = (TextView) findViewById(R.id.sort_tv);
        bubbleSortBtn = (Button) findViewById(R.id.bubble_sort);
        heapSortBtn = (Button) findViewById(R.id.heap_sort);
        insertionSortBtn = (Button) findViewById(R.id.insertion_sort);
        mergeSortBtn = (Button) findViewById(R.id.merge_sort);
        quickSortBtn = (Button) findViewById(R.id.quick_sort);
        radixSortBtn = (Button) findViewById(R.id.radix_sort);
        selectionSortBtn = (Button) findViewById(R.id.selection_sort);
        shellSortBtn = (Button) findViewById(R.id.shell_sort);

        bubbleSortBtn.setOnClickListener(this);
        heapSortBtn.setOnClickListener(this);
        insertionSortBtn.setOnClickListener(this);
        mergeSortBtn.setOnClickListener(this);
        quickSortBtn.setOnClickListener(this);
        radixSortBtn.setOnClickListener(this);
        selectionSortBtn.setOnClickListener(this);
        shellSortBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int[] A = new int[]{23, 12, 34, 24, 35, 67, 11, 23, 33, 45};
        switch (v.getId()){
            case R.id.bubble_sort:
                bubbleSortTest(A);
                break;
            case R.id.heap_sort:
                heapSortTest(A);
                break;
            case R.id.insertion_sort:
                insertionSortTest(A);
                break;
            case R.id.merge_sort:
                mergeSortTest(A);
                break;
            case R.id.quick_sort:
                quickSortTest(A);
                break;
            case R.id.radix_sort:
                radixSortTest(A);
                break;
            case R.id.selection_sort:
                selectionSortTest(A);
                break;
            case R.id.shell_sort:
                shellSortTest(A);
                break;
        }
    }

    private void mergeSortTest(int[] A) {
        MergeSortUtil.mergeSortTest(A);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder[] stringBuilders = MergeSortUtil.stringBuilders;
        stringBuilder.append("MergeSort A\n");
        for(int i = 0; i < stringBuilders.length; i++){
            stringBuilder.append(stringBuilders[i]);
        }
        sortTv.setText(stringBuilder);
    }

    private void radixSortTest(int[] A) {
        RadixSortUtil.radixSortTest(A, 10, 2);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder[] stringBuilders = RadixSortUtil.stringBuilders;
        stringBuilder.append("RadixSort A\n");
        for(int i = 0; i < stringBuilders.length; i++){
            stringBuilder.append(stringBuilders[i]);
        }
        sortTv.setText(stringBuilder);
    }

    private void quickSortTest(int[] A) {
        QuickSortUtil.quickSortTest(A);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder[] stringBuilders = QuickSortUtil.stringBuilders;
        stringBuilder.append("QuickSort A\n");
        for(int i = 0; i < stringBuilders.length; i++){
            if(stringBuilders[i] != null){
                stringBuilder.append(stringBuilders[i]);
            }
        }
        sortTv.setText(stringBuilder);
    }

    private void heapSortTest(int[] A) {
        HeapSortUtil.heapSortTest(A);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder[] stringBuilders = HeapSortUtil.stringBuilders;
        stringBuilder.append("HeapSort A\n");
        for(int i = 0; i < stringBuilders.length; i++){
            stringBuilder.append(stringBuilders[i]);
        }
        sortTv.setText(stringBuilder);
    }

    private void shellSortTest(int[] A) {
        ShellSortUtil.shellSortTest(A);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder[] stringBuilders = ShellSortUtil.stringBuilders;
        stringBuilder.append("ShellSort A\n");
        for(int i = 0; i < stringBuilders.length; i++){
            stringBuilder.append(stringBuilders[i]);
        }
        sortTv.setText(stringBuilder);
    }

    private void bubbleSortTest(int[] A) {
        BubbleSortUtil.bubbleSortTest(A);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder[] stringBuilders = BubbleSortUtil.stringBuilders;
        stringBuilder.append("BubbleSort A\n");
        for(int i = 0; i < stringBuilders.length; i++){
            stringBuilder.append(stringBuilders[i]);
        }
        sortTv.setText(stringBuilder);
    }

    private void selectionSortTest(int[] A) {
        SelectionSortUtil.selectionSortTest(A);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder[] stringBuilders = SelectionSortUtil.stringBuilders;
        stringBuilder.append("SelectionSort A\n");
        for(int i = 0; i < stringBuilders.length; i++){
            stringBuilder.append(stringBuilders[i]);
        }
        sortTv.setText(stringBuilder);
    }

    private void insertionSortTest(int[] A) {
        InsertionSortUtil.insertionSortTest(A);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder[] stringBuilders = InsertionSortUtil.stringBuilders;
        stringBuilder.append("InsertionSort A\n");
        for(int i = 0; i < stringBuilders.length; i++){
            stringBuilder.append(stringBuilders[i]);
        }
        sortTv.setText(stringBuilder);
    }
}
