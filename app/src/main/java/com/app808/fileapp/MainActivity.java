package com.app808.fileapp;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
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

import com.app808.fileapp.adapter.MyLcoalRecyclerViewAdapter;
import com.app808.fileapp.dummy.LocalFileDummy;
import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.fragment.CategoryFileFragment;
import com.app808.fileapp.fragment.LocalFileFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LocalFileFragment.OnListFragmentInteractionListener,
                   CategoryFileFragment.OnFragmentInteractionListener {

    LocalFileFragment mLocalFileFragment;
    MyLcoalRecyclerViewAdapter mMyLcoalRecyclerViewAdapter;
    RecyclerView mRecyclerView;

    CategoryFileFragment mCategoryFileFragment;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        System.out.println("顶部导航栏...");
        //顶部导航栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        System.out.println("FAB按钮功能...");
        //FAB按钮功能
        FloatingActionButton fabCopy = findViewById(R.id.fab_button_copy);
        fabCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FAB","Copy...");
                mMyLcoalRecyclerViewAdapter.getSelectItem()
                        .forEach((FileBean fileBean)->Log.i("select item",fileBean.getName()));
            }
        });
        FloatingActionButton fabMove = findViewById(R.id.fab_button_move);
        fabMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FAB","Move...");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        setLocalFragement();
    }

    private void getRecyclerView(){
        if(mRecyclerView == null){
            Log.i("加载recyleViewer",mLocalFileFragment.toString());
            mRecyclerView = (RecyclerView) mLocalFileFragment.getView();
            mMyLcoalRecyclerViewAdapter = (MyLcoalRecyclerViewAdapter) mRecyclerView.getAdapter();
            Log.i("替换LocalFileFragment","替换LocalFileFragment完成...");
        }
    }

    private void setLocalFragement(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(mLocalFileFragment == null){
            Log.i("创建LocalFileFragment","创建LocalFileFragment...");
            mLocalFileFragment =LocalFileFragment.newInstance(1);
            transaction.add(R.id.fragement_main, mLocalFileFragment);
            transaction.commit();
            if(mLocalFileFragment==null)
                Log.i("mLocalFileFragment","..........");
            return;
        }
        Log.i("替换LocalFileFragment","替换LocalFileFragment...");
        transaction.hide(mCategoryFileFragment)
                .show(mLocalFileFragment).addToBackStack(null);
        transaction.commit();
    }

    // 后退按键
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        getRecyclerView();
        if(mMyLcoalRecyclerViewAdapter.isMulited()){
            // 数据是多选状态--退出多选状态
            mMyLcoalRecyclerViewAdapter.setMulited(false);
            mMyLcoalRecyclerViewAdapter.setAllChecked(false);
            // 刷新数据
            mMyLcoalRecyclerViewAdapter.notifyDataSetChanged();
        }else if(mMyLcoalRecyclerViewAdapter.isBack()){
            // 可回到上一级
            mMyLcoalRecyclerViewAdapter.backPath();
        }

        // 数据可返回，则返回
        // getSupportFragmentManager().popBackStack();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getRecyclerView();
        if(mMyLcoalRecyclerViewAdapter.isMulited()){
            menu.findItem(R.id.action_mulited).setTitle(R.string.action_mulited_false);
        } else {
            menu.findItem(R.id.action_mulited).setTitle(R.string.action_mulited_true);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_mulited:
                // 多选菜单项
                getRecyclerView();
                if(!mMyLcoalRecyclerViewAdapter.isMulited()){
                    // 不是多选状态
                    mMyLcoalRecyclerViewAdapter.setMulited(true);
                    // 刷新数据
                    mMyLcoalRecyclerViewAdapter.notifyDataSetChanged();
                    Log.i("进入多选状态", "多选菜单");
                } else {
                    mMyLcoalRecyclerViewAdapter.setMulited(false);
                    Log.i("取消多选状态", "取消多选菜单");
                    // 清空数据的多选状态
                    mMyLcoalRecyclerViewAdapter.setAllChecked(false);
                    mMyLcoalRecyclerViewAdapter.notifyDataSetChanged();
                }
                return true;

            case R.id.action_sort_date_asc:
                // 排序菜单项
                Log.i("排序菜单项", "日期升序排序...");
                mMyLcoalRecyclerViewAdapter.sortDate(true);
                return true;

            case R.id.action_sort_date_des:
                // 排序菜单项
                Log.i("排序菜单项", "日期降序排序...");
                mMyLcoalRecyclerViewAdapter.sortDate(false);
                return true;

            case R.id.action_sort_name_asc:
                // 日期升序排序菜单项
                Log.i("排序菜单项", "名称升序排序...");
                mMyLcoalRecyclerViewAdapter.sortName(true);
                return true;

            case R.id.action_sort_name_des:
                // 日期升序排序菜单项
                Log.i("排序菜单项", "名称降序排序...");
                mMyLcoalRecyclerViewAdapter.sortName(false);
                return true;

            case R.id.action_reverse:
                // 反选菜单项
                Log.i("反选菜单项", "反选菜单项...");
                mMyLcoalRecyclerViewAdapter.setMulited(true);
                mMyLcoalRecyclerViewAdapter.reverseChecked();
                return true;
            default:

        }

        return super.onOptionsItemSelected(item);
    }

    private void setCategoryFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(mCategoryFileFragment==null){
            Log.i("创建categoryFragment","创建categoryFragment...");
            mCategoryFileFragment = new CategoryFileFragment();
            if(mLocalFileFragment==null)
                Log.i("mLocalFileFragment","null");
            transaction.hide(mLocalFileFragment)
                    .add(R.id.fragement_main, mCategoryFileFragment).addToBackStack(null).commit();
            Log.i("...","................");
            return;
        }
        Log.i("替换categoryFragment","跳转至categoryFragment...");
        transaction.hide(mLocalFileFragment)
                .show(mCategoryFileFragment)
                .commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_onedrive) {
            Log.i("onNavigationItemSelected","跳转至categoryFragment");
            setLocalFragement();
            // Handle the camera action
        } else if (id == R.id.nav_file_category) {
            // 跳转至categoryFragment
            setCategoryFragment();
        } else if (id == R.id.nav_other) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(LocalFileDummy item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
