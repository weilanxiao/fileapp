package com.app808.fileapp;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.app808.fileapp.adapter.CategoryRecyclerViewAdapter;
import com.app808.fileapp.adapter.CloudSyncRecyclerViewAdapter;
import com.app808.fileapp.adapter.LcoalRecyclerViewAdapter;
import com.app808.fileapp.adapter.QuickFileRecyclerViewAdapter;
import com.app808.fileapp.dummy.CloudDummy;
import com.app808.fileapp.dummy.LocalFileDummy;
import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.fragment.CategoryFileFragment;
import com.app808.fileapp.fragment.CloudSyncFragment;
import com.app808.fileapp.fragment.LocalFileFragment;
import com.app808.fileapp.service.FragmentService;
import com.app808.fileapp.utils.FileFilterUtils;
import com.app808.fileapp.utils.InstallAPK;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        LocalFileFragment.OnListFragmentInteractionListener,
        CategoryFileFragment.OnFragmentInteractionListener,
        CloudSyncFragment.OnListFragmentInteractionListener,
        LcoalRecyclerViewAdapter.FABListener,
        CloudSyncRecyclerViewAdapter.FABListener,
        LocalFileFragment.CurrentRecyclerViewListener,
        CategoryFileFragment.CurrentRecyclerViewListener,
        CloudSyncFragment.CurrentRecyclerViewListener,
        CategoryRecyclerViewAdapter.EnterLocalFragmentListener,
        QuickFileRecyclerViewAdapter.EnterLocalFragmentListener {

    private static final String TAG = "Main Activity";

    private Fragment currentFragment;
    private FragmentService mFragmentService;
    private SearchView mSearchView;

    // 浮动按钮
    FloatingActionsMenu mfab;
    FloatingActionButton mfabCopy;
    FloatingActionButton mfabMove;
    FloatingActionButton mfabPaste;
    FloatingActionButton mfabDelete;
    FloatingActionButton mfabUpload;
    FloatingActionButton mfabSync;
    FloatingActionButton mfabQuick;
    // 右上角菜单
    Menu mMenu;
    private MainActivity mContext;
    private ProgressBar mProgressBar;
    public int flag = 0;
    // CategoryFileFragment
    public void showCategoryFileFragment(){
        flag = 0;
        mFragmentService.showCategoryFileFragment(getSupportFragmentManager().beginTransaction());
        setToolBar("文件分类");
        mfab.setVisibility(View.GONE);
        if(mMenu !=null ){
            mMenu.setGroupVisible(0,false);
        }
    }

    // LocalFileFragment
    public void showLocalFileFragment(){
        flag = 0;
        mFragmentService.showLocalFileFragment(getSupportFragmentManager().beginTransaction());
        mfab.setVisibility(View.VISIBLE);
        mfabCopy.setVisibility(View.VISIBLE);
        mfabMove.setVisibility(View.VISIBLE);
        mfabDelete.setVisibility(View.VISIBLE);
        mfabUpload.setVisibility(View.VISIBLE);
        mfabSync.setVisibility(View.GONE);
        mfabQuick.setVisibility(View.VISIBLE);
        mMenu.setGroupVisible(0,true);
        // 设置标题
        setToolBar("本地文件");
    }

    // CloudSyncFragment
    public void showCloudSyncFragment(){
        flag = 0;
        mFragmentService.showCloudSyncFragment(getSupportFragmentManager().beginTransaction());
        mfab.setVisibility(View.VISIBLE);
        mfabCopy.setVisibility(View.GONE);
        mfabMove.setVisibility(View.GONE);
        mfabDelete.setVisibility(View.GONE);
        mfabUpload.setVisibility(View.GONE);
        mfabSync.setVisibility(View.VISIBLE);
        mfabQuick.setVisibility(View.GONE);
        mMenu.setGroupVisible(0,true);
        setToolBar("云端文件");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!new InstallAPK(this).checkIsAndroidO()){
            Toast.makeText(this, "请设置相应的权限", Toast.LENGTH_SHORT).show();
            System.exit(0);
        }
        setContentView(R.layout.activity_main);
        mFragmentService = new FragmentService(this);
        Log.i(TAG,"init toolbar...");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFAB();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        showCategoryFileFragment();
        initAlert();
    }

    // 后退
    @Override
    public void onBackPressed() {
        Log.i(TAG,"on back pressed...");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        currentFragment = mFragmentService.getCurrentFragment();
        mFragmentService.onBack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.i("create menu","...");
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        mMenu.setGroupVisible(0,false);
//        mSearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
//        mSearchView.setVisibility(View.GONE);
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                mFragmentService.searchFile(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if( mFragmentService.getCurrentFragment() instanceof LocalFileFragment){
            if( mFragmentService.getLocalFileViewModel().getRecyclerView() != null){
                if(mFragmentService.getLocalFileViewModel().getRecyclerViewAdapter().isMulited()){
                    menu.findItem(R.id.action_mulited).setTitle(R.string.action_mulited_false);
                } else {
                    menu.findItem(R.id.action_mulited).setTitle(R.string.action_mulited_true);
                }
                return true;
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
                    if(!mFragmentService.getLocalFileViewModel().getRecyclerViewAdapter().isMulited()){
                        // 不是多选状态
                        mFragmentService.getLocalFileViewModel().getRecyclerViewAdapter().setMulited(true);
                        // 刷新数据
                        mFragmentService.getLocalFileViewModel().getRecyclerViewAdapter().notifyDataSetChanged();
                        onClickFAB(true);
                        Log.i("进入多选状态", "多选菜单");
                    } else {
                        mFragmentService.getLocalFileViewModel().getRecyclerViewAdapter().setMulited(false);
                        Log.i("取消多选状态", "取消多选菜单");
                        // 清空数据的多选状态
                        onClickFAB(false);
                        mFragmentService.getLocalFileViewModel().getRecyclerViewAdapter().setAllChecked(false);
                        mFragmentService.getLocalFileViewModel().getRecyclerViewAdapter().notifyDataSetChanged();
                    }
                    return true;
                case R.id.action_sort_date_asc:
                    // 排序菜单项
                    Log.i("排序菜单项", "日期升序排序...");
                    mFragmentService.getLocalFileViewModel().getRecyclerViewAdapter().sortDate(true);
                    return true;
                case R.id.action_sort_date_des:
                    // 排序菜单项
                    Log.i("排序菜单项", "日期降序排序...");
                    mFragmentService.getLocalFileViewModel().getRecyclerViewAdapter().sortDate(false);
                    return true;
                case R.id.action_sort_name_asc:
                    // 日期升序排序菜单项
                    Log.i("排序菜单项", "名称升序排序...");
                    mFragmentService.getLocalFileViewModel().getRecyclerViewAdapter().sortName(true);
                    return true;
                case R.id.action_sort_name_des:
                    // 日期升序排序菜单项
                    Log.i("排序菜单项", "名称降序排序...");
                    mFragmentService.getLocalFileViewModel().getRecyclerViewAdapter().sortName(false);
                    return true;
                case R.id.action_reverse:
                    // 反选菜单项
                    Log.i("反选菜单项", "反选菜单项...");
                    mFragmentService.getLocalFileViewModel().getRecyclerViewAdapter().setMulited(true);
                    mFragmentService.getLocalFileViewModel().getRecyclerViewAdapter().reverseChecked();
                    return true;
                default:
            }
        }else if(fragment instanceof CloudSyncFragment){
            switch (id){
                case R.id.action_mulited:
                    // 多选菜单项
                    if(!mFragmentService.getCloudSyncViewModel().getRecyclerViewAdapter().isMulited()){
                        // 不是多选状态
                        mFragmentService.getCloudSyncViewModel().getRecyclerViewAdapter().setMulited(true);
                        // 刷新数据
                        mFragmentService.getCloudSyncViewModel().getRecyclerViewAdapter().notifyDataSetChanged();
                        onClickFAB(true);
                        Log.i("进入多选状态", "多选菜单");
                    } else {
                        mFragmentService.getCloudSyncViewModel().getRecyclerViewAdapter().setMulited(false);
                        Log.i("取消多选状态", "取消多选菜单");
                        // 清空数据的多选状态
                        onClickFAB(false);
                        mFragmentService.getCloudSyncViewModel().getRecyclerViewAdapter().setAllChecked(false);
                        mFragmentService.getCloudSyncViewModel().getRecyclerViewAdapter().notifyDataSetChanged();
                    }
                    return true;
                case R.id.action_sort_date_asc:
                    // 排序菜单项
                    Log.i("排序菜单项", "日期升序排序...");
                    mFragmentService.getCloudSyncViewModel().getRecyclerViewAdapter().sortDate(true);
                    return true;
                case R.id.action_sort_date_des:
                    // 排序菜单项
                    Log.i("排序菜单项", "日期降序排序...");
                    mFragmentService.getCloudSyncViewModel().getRecyclerViewAdapter().sortDate(false);
                    return true;
                case R.id.action_sort_name_asc:
                    // 日期升序排序菜单项
                    Log.i("排序菜单项", "名称升序排序...");
                    mFragmentService.getCloudSyncViewModel().getRecyclerViewAdapter().sortName(true);
                    return true;
                case R.id.action_sort_name_des:
                    // 日期升序排序菜单项
                    Log.i("排序菜单项", "名称降序排序...");
                    mFragmentService.getCloudSyncViewModel().getRecyclerViewAdapter().sortName(false);
                    return true;
                case R.id.action_reverse:
                    // 反选菜单项
                    Log.i("反选菜单项", "反选菜单项...");
                    mFragmentService.getCloudSyncViewModel().getRecyclerViewAdapter().setMulited(true);
                    mFragmentService.getCloudSyncViewModel().getRecyclerViewAdapter().reverseChecked();
                    return true;
                default:
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.i(TAG,"onNavigationItemSelected...");
        int id = item.getItemId();
        if(id == R.id.nav_onedrive){
            showCloudSyncFragment();
            Toast.makeText(this,"尝试连接至网络...",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_localfile) {
            showLocalFileFragment();
            Toast.makeText(this,"尝试搜索本地文件...",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_file_category) {
            showCategoryFileFragment();
            Toast.makeText(this,"导航至主页...",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_other) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("This is our APP");
            dialogBuilder.setMessage("UI：袁志豪\n后端：魏蓝骁\n后端：孔明\n云同步：黄宣霖\n搜索分类：彭宇聪\n666：左忠霖");
            dialogBuilder.setCancelable(false);  //设置为false，则点击back键或者弹窗外区域，弹窗不消去
            dialogBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener(){ //使用了匿名内部类
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            //加入逻辑代码
                            //对话框消失的方法
                            dialog.dismiss();
                        }
                    }
            );
            //使用对话框创建器来创建一个对话框对象
            AlertDialog alertDialog = dialogBuilder.create();
            //将对话框显示出来
            alertDialog.show();
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
        Log.i(TAG,"init fab...");
        mfab = findViewById(R.id.fab);
        mfabCopy = findViewById(R.id.fab_button_copy);
        mfabMove = findViewById(R.id.fab_button_move);
        mfabPaste = findViewById(R.id.fab_button_paste);
        mfabDelete = findViewById(R.id.fab_button_delete);
        mfabUpload = findViewById(R.id.fab_button_upload);
        mfabSync = findViewById(R.id.fab_button_tongbu);
        mfabQuick = findViewById(R.id.fab_button_addquick);
        // 设置监听
        mfabCopy.setOnClickListener((v)->mFragmentService.copy(v));
        mfabMove.setOnClickListener((v)->mFragmentService.move(v));
        mfabPaste.setOnClickListener((v)->mFragmentService.paste(v));
        mfabDelete.setOnClickListener((v)->mFragmentService.delete(v));
        mfabQuick.setOnClickListener((v)->mFragmentService.addQuick(v));
        mfabUpload.setOnClickListener((v)->mFragmentService.upload(v));
        mfabSync.setOnClickListener((v)->mFragmentService.download(v));
    }

    // 设置页面加载数据回调
    @Override
    public void getCurrentRecyclerView(){
        Log.i(TAG,"get current RecyclerView...");
        currentFragment = mFragmentService.getCurrentFragment();
        if(currentFragment instanceof LocalFileFragment){
            LocalFileFragment localFileFragment = (LocalFileFragment) currentFragment;
            if(localFileFragment.getIsSecond() != 0){
                // 操作界面
                mFragmentService.getSecondRecyclerView();
            }else{
                mFragmentService.getLocalFileRecyclerView();
            }
        }else if(currentFragment instanceof CategoryFileFragment){
            mFragmentService.getQuickRecyclerView();
            mFragmentService.getCategoryRecyclerView();
        }else if(currentFragment instanceof CloudSyncFragment){
            mFragmentService.getCloudSyncRecyclerView();
        }
        Log.i(TAG,"get current RecyclerView success...");
    }

    private Toolbar mToolbar;
    public void setToolBar(String name){
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(name);
    }

    @Override
    public void toLocalFragment(FileBean fileBean) {
        if(fileBean.getPath().equals("Category")){
            Log.i(TAG,"分类入口");
            showBar(true);
            showLocalFileFragment();
            mFragmentService.enterCategory(fileBean);
        }else{
            Log.i(TAG,"快速入口");
            Log.i("数据",fileBean.getPath());
            showLocalFileFragment();
            mFragmentService.enterQuick(fileBean);
        }
    }

    AlertDialog.Builder dialogBuilder;

    private void initAlert(){
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);  //设置为false，则点击back键或者弹窗外区域，弹窗不消去
        dialogBuilder.setPositiveButton("", new DialogInterface.OnClickListener(){ //使用了匿名内部类
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        //加入逻辑代码
                        //对话框消失的方法
                        dialog.dismiss();
                    }
                }
        );
        dialogBuilder.setTitle("This is our APP");
        dialogBuilder.setMessage("加载中...");
        alertDialog = dialogBuilder.create();
    }

    AlertDialog alertDialog;

    public void showBar(boolean flag){
        //使用对话框创建器来创建一个对话框对象
        //将对话框显示出来
        if(flag){
            alertDialog.show();
        }else{
            alertDialog.hide();
        }
    }

    private void createProgressBar(){
        mContext=this;
        //整个Activity布局的最终父布局,参见参考资料
        FrameLayout rootFrameLayout=(FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams=
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity= Gravity.CENTER;
        mProgressBar=new ProgressBar(mContext);
        mProgressBar.setLayoutParams(layoutParams);
        mProgressBar.setVisibility(View.VISIBLE);
        rootFrameLayout.addView(mProgressBar);
    }

    @Override
    public void onListFragmentInteraction(String msg) {
        if(msg!=null){
            Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
        }
    }
}
