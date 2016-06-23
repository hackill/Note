package com.vise.note;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.vise.algorithm.BubbleSortUtil;
import com.vise.algorithm.HeapSortUtil;
import com.vise.algorithm.InsertionSortUtil;
import com.vise.algorithm.MergeSortUtil;
import com.vise.algorithm.QuickSortUtil;
import com.vise.algorithm.RadixSortUtil;
import com.vise.algorithm.SelectionSortUtil;
import com.vise.algorithm.ShellSortUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView sortTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        init();
    }

    private void init() {
        sortTv = (TextView) findViewById(R.id.insertion_sort);
        int[] A = new int[]{23, 12, 34, 24, 35, 67, 11, 23, 33, 45};
//        insertionSortTest(A);
//        selectionSortTest(A);
//        bubbleSortTest(A);
//        shellSortTest(A);
//        heapSortTest(A);
        quickSortTest(A);
//        radixSortTest(A);
//        mergeSortTest(A);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
