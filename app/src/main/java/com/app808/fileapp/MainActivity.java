package com.app808.fileapp;

import android.annotation.SuppressLint;
import android.content.ClipData;
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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app808.fileapp.adapter.MainListAdapter;
import com.app808.fileapp.entity.FileBean;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //顶部导航栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FAB按钮功能
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
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initMainList(R.id.list_main);
    }

    private MainListAdapter mainListAdapter;
    private static final String rootPath = "/storage/emulated/0";
    private final LinkedList<String> pathStack = new LinkedList<String>();

    private void initMainList(int id){
        ListView listView = findViewById(id);
        mainListAdapter = new MainListAdapter(MainActivity.this, rootPath);
        listView.setAdapter(mainListAdapter);
        pathStack.addLast(rootPath);
        //设置单击事件
        listView.setOnItemClickListener(MainActivity.this);
        //设置长按事件
        listView.setOnItemLongClickListener(MainActivity.this);
    }

    /**
     * */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mainListAdapter.isMulited()){
            //多选状态，点击选中当前项取反
            mainListAdapter.setItemState(position, !mainListAdapter.getViewHolders().get(position).isChecked);
            mainListAdapter.reflushAdapter();
        } else {
            // 非多选状态，单击进入目录
            FileBean fileBean = ((MainListAdapter.ViewHolder) mainListAdapter.getItem(position)).data;
            System.out.println("读取" + fileBean.getPath() + "中....");
            if (fileBean.getDir()){
                pathStack.addLast(fileBean.getPath());
                mainListAdapter.loadData(fileBean.getPath());
            }
        }
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){

        // 如果当前不为多选状态
        if(!mainListAdapter.isMulited()){
            // 将列表设置为多选状态
            invalidateOptionsMenu();
            setMulitedAndDisplay(mainListAdapter, true);
            Log.i("进入多选状态", String.valueOf(position));
            // 选中当前项
            mainListAdapter.setItemState(position, true);
        }else{
            //选中当前项
            mainListAdapter.setItemState(position, true);
        }
        mainListAdapter.reflushAdapter();
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(mainListAdapter.isMulited()) {
            // 多选状态退出
            setMulitedAndDisplay(mainListAdapter, false);
            Log.i("退出多选状态", ".........");
            // 刷新多选框状态
            mainListAdapter.reflushVHisChecked();
            mainListAdapter.reflushAdapter();
        } else {
            if(pathStack.size() != 1){
                pathStack.pollLast();
                mainListAdapter.loadData(pathStack.peekLast());
            }else{
                super.onBackPressed();
            }
        }
    }

    /**
     * 设置多选状态和显示
     * */
    private void setMulitedAndDisplay(MainListAdapter adapter, boolean state){
        adapter.setMulited(state);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mainListAdapter.isMulited()){
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
        if (id == R.id.action_mulited) {
            if(!mainListAdapter.isMulited()){
                setMulitedAndDisplay(mainListAdapter, true);
                Log.i("进入多选状态", "多选菜单");
                mainListAdapter.reflushVHisChecked();
                mainListAdapter.reflushAdapter();
            } else {
                setMulitedAndDisplay(mainListAdapter, false);
                Log.i("取消多选状态", "取消多选菜单");
                mainListAdapter.reflushVHisChecked();
                mainListAdapter.reflushAdapter();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_onedrive) {
            // Handle the camera action
        } else if (id == R.id.nav_file_category) {

        } else if (id == R.id.nav_other) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
