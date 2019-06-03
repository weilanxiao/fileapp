package com.app808.fileapp;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.app808.fileapp.adapter.MyLcoalRecyclerViewAdapter;
import com.app808.fileapp.dummy.LocalFileDummy;
import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.fragment.CategoryFileFragment;
import com.app808.fileapp.fragment.LocalFileFragment;
import com.app808.fileapp.utils.FileUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LocalFileFragment.OnListFragmentInteractionListener,
                   CategoryFileFragment.OnFragmentInteractionListener,
                   MyLcoalRecyclerViewAdapter.FABListener {

    LocalFileFragment mLocalFileFragment;
    MyLcoalRecyclerViewAdapter mMyLcoalRecyclerViewAdapter;
    RecyclerView mRecyclerView;

    LocalFileFragment secondFragment;
    RecyclerView secondRecycleView;
    MyLcoalRecyclerViewAdapter secondAdapter;

    CategoryFileFragment mCategoryFileFragment;
    List<FileBean> listBean;

    FloatingActionsMenu mfab;
    FloatingActionButton mfabCopy;
    FloatingActionButton mfabMove;
    FloatingActionButton mfabPaste;
    FloatingActionButton mfabDelete;


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
        initFAB();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // setLocalFragment();
        setCategoryFragment();
    }

    // 后退
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragement_main);
        if(fragment instanceof LocalFileFragment){
            // 是否为localFileFragment
            LocalFileFragment localFileFragment = (LocalFileFragment) fragment;
            if(localFileFragment.getIsSecond()==0){
                // 是否为主fragment
                getRecyclerView();
                if(mMyLcoalRecyclerViewAdapter.isMulited()){
                    // 数据是多选状态--退出多选状态
                    mMyLcoalRecyclerViewAdapter.setMulited(false);
                    mMyLcoalRecyclerViewAdapter.setAllChecked(false);
                    // 刷新数据
                    mMyLcoalRecyclerViewAdapter.notifyDataSetChanged();
                    onClickFAB(false);
                }else if(mMyLcoalRecyclerViewAdapter.isBack()){
                    // 可回到上一级
                    Log.i("是否可退?","true");
                    mMyLcoalRecyclerViewAdapter.backPath();
                }else{
                    Log.i("是否可退?","false");

                }
            }else{
                // 是否为次fragment
                getSecondRecyclerView();
                secondAdapter.backPath();
            }
        }
        if(fragment instanceof CategoryFileFragment){

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
        if(mLocalFileFragment!=null){
            getRecyclerView();
            if(mMyLcoalRecyclerViewAdapter.isMulited()){
                menu.findItem(R.id.action_mulited).setTitle(R.string.action_mulited_false);
            } else {
                menu.findItem(R.id.action_mulited).setTitle(R.string.action_mulited_true);
            }
            return true;
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.fragement_main);
        if(fragment instanceof LocalFileFragment){
            switch (id){
                case R.id.action_mulited:
                    // 多选菜单项
                    getRecyclerView();
                    if(!mMyLcoalRecyclerViewAdapter.isMulited()){
                        // 不是多选状态
                        mMyLcoalRecyclerViewAdapter.setMulited(true);
                        // 刷新数据
                        mMyLcoalRecyclerViewAdapter.notifyDataSetChanged();
                        onClickFAB(true);
                        Log.i("进入多选状态", "多选菜单");
                    } else {
                        mMyLcoalRecyclerViewAdapter.setMulited(false);
                        Log.i("取消多选状态", "取消多选菜单");
                        // 清空数据的多选状态
                        onClickFAB(false);
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
        }else if(fragment instanceof CategoryFileFragment){
        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_onedrive) {
            Log.i("onNavigationItemSelected","跳转至categoryFragment");
            setLocalFragment();
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

    @Override
    public void onClickFAB(boolean flag) {
        Log.i("长按","FAB...");
        mfab= findViewById(R.id.fab);
        if(flag){
            mfab.expand();
        }else{
            mfab.collapse();
        }

    }

    // 初始化FAB按钮
    private void initFAB(){
        mfab = findViewById(R.id.fab);
        mfabCopy = findViewById(R.id.fab_button_copy);
        mfabMove = findViewById(R.id.fab_button_move);
        mfabPaste = findViewById(R.id.fab_button_paste);
        mfabDelete = findViewById(R.id.fab_button_delete);
        // 设置监听
        mfabCopy.setOnClickListener((v)->copyClick(v));
        mfabMove.setOnClickListener((v)->moveClick(v));
        mfabPaste.setOnClickListener((v)->pasteClick(v));
        mfabDelete.setOnClickListener((v)->deleleClick(v));
    }

    // 复制
    public void copyClick(View v){
        if(listBean != null){
            Log.i("Copy","操作进行中...");
            Toast.makeText(MainActivity.this,"操作进行中...",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("FAB","Copy...");
        Toast.makeText(MainActivity.this,"已选择...",Toast.LENGTH_SHORT).show();
        // 设置操作路径集合
        listBean = mMyLcoalRecyclerViewAdapter.getSelectItem();
        if(listBean.size() == 0){
            Log.i("错误","请选择复制文件");
            Toast.makeText(MainActivity.this,"请选择复制文件...",Toast.LENGTH_SHORT).show();
            listBean = null;
            return;
        }
        // copy操作时创建次fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        secondFragment = new LocalFileFragment();
        secondFragment.setIsSecond(1);
        transaction.add(R.id.fragement_main,secondFragment);
        transaction.hide(mLocalFileFragment).show(secondFragment).commit();
    }

    // 移动
    private void moveClick(View v) {
        if(listBean != null){
            Log.i("文件","操作进行中...");
            Toast.makeText(MainActivity.this,"操作进行中...",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("FAB","Move...");
        Toast.makeText(MainActivity.this,"已选择...",Toast.LENGTH_SHORT).show();
        // 设置操作路径集合
        listBean = mMyLcoalRecyclerViewAdapter.getSelectItem();
        if(listBean.size() == 0){
            Log.i("错误","请选择移动文件");
            listBean = null;
            Toast.makeText(MainActivity.this,"请选择移动文件...",Toast.LENGTH_SHORT).show();
            return;
        }
        // move操作时创建次fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        secondFragment = new LocalFileFragment();
        secondFragment.setIsSecond(2);
        transaction.add(R.id.fragement_main,secondFragment);
        transaction.hide(mLocalFileFragment).show(secondFragment).commit();
    }

    // 粘贴
    private void pasteClick(View v) {
        Log.i("FAB","Paste...");
        if(listBean==null||listBean.size()==0){
            Log.i("文件","请选择文件...");
            Toast.makeText(MainActivity.this,"请选择文件...",Toast.LENGTH_SHORT).show();
            return;
        }
        // 判断是否为second
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragement_main);
        if(fragment instanceof LocalFileFragment){
            LocalFileFragment localFileFragment = (LocalFileFragment) fragment;
            if(localFileFragment.getIsSecond()!=0){
                // 为次fragment
                getSecondRecyclerView();
                String resPath = secondAdapter.getPathStack().peekLast();
                if(secondFragment.getIsSecond()==1){
                    for(FileBean srcFile:listBean){
                        Log.i("Paste","...");
                        FileUtils.copy(srcFile,resPath);
                    }
                    // 文件复制完成
                    Log.i("文件操作","复制完成...");
                    Toast.makeText(MainActivity.this,"已复制...",Toast.LENGTH_SHORT).show();
                }else if(secondFragment.getIsSecond()==2){
                    for(FileBean srcFile:listBean){
                        Log.i("Paste","...");
                        FileUtils.move(srcFile,resPath);
                    }
                    // 文件移动完成
                    Log.i("文件操作","移动完成...");
                    Toast.makeText(MainActivity.this,"已移动...",Toast.LENGTH_SHORT).show();
                }
                // 切回主fragment
                listBean = null;
                fragment.onDestroy();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(secondFragment).show(mLocalFileFragment).commit();
                mMyLcoalRecyclerViewAdapter.initPathStack(secondAdapter.getPathStack());
                secondFragment = null;
                secondRecycleView = null;
                secondAdapter = null;
                mMyLcoalRecyclerViewAdapter.update();
            }
        }
    }

    // 删除
    private void deleleClick(View v) {
        if(listBean != null){
            Log.i("文件","操作进行中...");
            Toast.makeText(MainActivity.this,"操作进行中...",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("FAB","Delete...");
        Toast.makeText(MainActivity.this,"已选择...",Toast.LENGTH_SHORT).show();
        //                mMyLcoalRecyclerViewAdapter.getSelectItem()
        //                        .forEach((FileBean fileBean)->Log.i("select item",fileBean.getName()));
        // 设置操作路径集合
        listBean = mMyLcoalRecyclerViewAdapter.getSelectItem();
        if(listBean.size() == 0){
            Log.i("错误","请选择删除文件");
            listBean = null;
            Toast.makeText(MainActivity.this,"请选择删除文件...",Toast.LENGTH_SHORT).show();
            return;
        }
        for(FileBean srcFile:listBean){
            Log.i("Paste","...");
            FileUtils.deleteFiles(srcFile.getPath());
        }
        // 文件复制完成
        Log.i("文件操作","删除...");
        Toast.makeText(MainActivity.this,"已删除...",Toast.LENGTH_SHORT).show();
        mMyLcoalRecyclerViewAdapter.update();
    }

    // 获取主viewAdapter
    private void getRecyclerView(){
        if(mRecyclerView == null){
            Log.i("加载recyleViewer",mLocalFileFragment.toString());
            mRecyclerView = (RecyclerView) mLocalFileFragment.getView();
            mMyLcoalRecyclerViewAdapter = (MyLcoalRecyclerViewAdapter) mRecyclerView.getAdapter();
            mMyLcoalRecyclerViewAdapter.setFABLinstener((MyLcoalRecyclerViewAdapter.FABListener) MainActivity.this);
            Log.i("替换LocalFileFragment","替换LocalFileFragment完成...");
        }
    }

    // 获取次viewAdapter
    private void getSecondRecyclerView(){
        if(secondRecycleView == null){
            secondRecycleView = (RecyclerView) secondFragment.getView();
            secondAdapter = (MyLcoalRecyclerViewAdapter) secondRecycleView.getAdapter();
        }
    }

    // 设置本地文件为当前fragment
    private void setLocalFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        @SuppressLint("ResourceType") Menu menu =  findViewById(R.menu.main);
//        menu.setGroupEnabled(R.id.app_bar_search,true);
        if(mLocalFileFragment == null){
            Log.i("创建LocalFileFragment","创建LocalFileFragment...");
            mLocalFileFragment = LocalFileFragment.newInstance(1);
            // 设置当前fragment为主。
            mLocalFileFragment.setIsSecond(0);
            if(mCategoryFileFragment!=null){
                transaction.hide(mCategoryFileFragment);
            }
            transaction.add(R.id.fragement_main, mLocalFileFragment);
            transaction.commit();
            if(mLocalFileFragment==null)
                Log.i("mLocalFileFragment","..........");
            return;
        }
        Log.i("替换LocalFileFragment","替换LocalFileFragment...");
        transaction.hide(mCategoryFileFragment)
                .show(mLocalFileFragment);
        transaction.commit();
    }

    // 设置分类为当前fragment
    @SuppressLint("ResourceType")
    private void setCategoryFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 让按钮失效
//        Menu menu =  findViewById(R.menu.main);
//        menu.setGroupEnabled(R.id.app_bar_search,false);
        if(mCategoryFileFragment==null){
            Log.i("创建categoryFragment","创建categoryFragment...");
            mCategoryFileFragment = new CategoryFileFragment();
            if(mLocalFileFragment==null){
                Log.i("mLocalFileFragment","null");
            }else{
                transaction.hide(mLocalFileFragment);
            }
            transaction.add(R.id.fragement_main, mCategoryFileFragment).addToBackStack(null).commit();
            Log.i("...","................");
            return;
        }
        Log.i("替换categoryFragment","跳转至categoryFragment...");
        transaction.hide(mLocalFileFragment)
                .show(mCategoryFileFragment).addToBackStack(null)
                .commit();
    }

}
