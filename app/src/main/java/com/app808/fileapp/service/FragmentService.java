package com.app808.fileapp.service;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app808.fileapp.MainActivity;
import com.app808.fileapp.R;
import com.app808.fileapp.adapter.CategoryRecyclerViewAdapter;
import com.app808.fileapp.adapter.CloudSyncRecyclerViewAdapter;
import com.app808.fileapp.adapter.LcoalRecyclerViewAdapter;
import com.app808.fileapp.adapter.QuickFileRecyclerViewAdapter;
import com.app808.fileapp.entity.ConstVaule;
import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.fragment.CategoryFileFragment;
import com.app808.fileapp.fragment.CloudSyncFragment;
import com.app808.fileapp.fragment.LocalFileFragment;
import com.app808.fileapp.utils.FileFilterUtils;
import com.app808.fileapp.utils.FileUtils;
import com.app808.fileapp.viewmodel.CategoryFileViewModel;
import com.app808.fileapp.viewmodel.CloudSyncViewModel;
import com.app808.fileapp.viewmodel.LocalFileViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FragmentService {

    private static final String TAG = "Fragment Service";
    private FragmentTransaction mTransaction;
    private Fragment currentFragment;

    private LocalFileViewModel mLocalFileViewModel;
    private LocalFileViewModel mSecondFileViewModel;
    private CategoryFileViewModel mCategoryFileViewModel;
    private CloudSyncViewModel mCloudSyncViewModel;

    private MainActivity mActivity;

    public FragmentService(MainActivity mainActivity){
        mActivity = mainActivity;
        mLocalFileViewModel = new LocalFileViewModel();
        mCategoryFileViewModel = new CategoryFileViewModel();
        mSecondFileViewModel = new LocalFileViewModel();
        mCloudSyncViewModel = new CloudSyncViewModel();
        quickService = new QuickService(mActivity);
    }

    public LocalFileViewModel getLocalFileViewModel() {
        return mLocalFileViewModel;
    }

    public CloudSyncViewModel getCloudSyncViewModel(){
        return mCloudSyncViewModel;
    }

    QuickService quickService ;


    public CategoryFileViewModel getCategoryFileViewModel() {
        return mCategoryFileViewModel;
    }


    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    // 隐藏所有fragment
    private void hideAllFragment(FragmentTransaction transaction){
        Log.i(TAG,"hide all fragment...");
        if(mLocalFileViewModel.getFragment() != null){
            Log.i(TAG,"hide category fragment...");
            transaction.hide(mLocalFileViewModel.getFragment());
        }
        if(mCategoryFileViewModel.getFragment() != null){
            Log.i(TAG,"hide localFile fragment...");
            transaction.hide(mCategoryFileViewModel.getFragment());
        }
        if(mSecondFileViewModel.getFragment() !=  null){
            Log.i(TAG,"hide second fragment...");
            transaction.hide(mSecondFileViewModel.getFragment());
        }
        if(mCloudSyncViewModel.getFragment() != null){
            Log.i(TAG,"hide cloudSync fragment...");
            transaction.hide(mCloudSyncViewModel.getFragment());
        }
    }

    // 展示本地文件fragment
    public void showLocalFileFragment(FragmentTransaction transaction){
        mTransaction = transaction;
        Log.i(TAG,"==> localFile fragment...");
        if(mLocalFileViewModel.getFragment() == null){
            mLocalFileViewModel.setFragment(LocalFileFragment.newInstance(1));
            mTransaction.add(R.id.fragement_main, mLocalFileViewModel.getFragment());
            mLocalFileViewModel.getFragment().setRecyclerViewLinstener(mActivity);
            Log.i(TAG,"==>init localFile fragment success...");
        }
        if( mLocalFileViewModel.getRecyclerView() !=null && mLocalFileViewModel.getRecyclerViewAdapter().getPathStack().size()==0){
            loadLocalFileRoot();
        }
        hideAllFragment(mTransaction);
        mTransaction.show(mLocalFileViewModel.getFragment());
        mTransaction.commit();
        currentFragment = mLocalFileViewModel.getFragment();
        mActivity.setToolBar("本地文件");
    }

    // 展示操作fragment
    public void showSecondFragment(FragmentTransaction transaction, int flag) {
        mTransaction = transaction;
        Log.i(TAG,"==> second fragment...");
        if(mSecondFileViewModel.getFragment() == null){
            LocalFileFragment fragment = new LocalFileFragment();
            fragment.setIsSecond(flag);
            mSecondFileViewModel.setFragment(fragment);
            mTransaction.add(R.id.fragement_main, mSecondFileViewModel.getFragment());
            mSecondFileViewModel.getFragment().setRecyclerViewLinstener(mActivity);
            Log.i(TAG,"==>init second fragment success...");
        }
        hideAllFragment(mTransaction);
        mTransaction.show(mSecondFileViewModel.getFragment());
        mTransaction.commit();
        currentFragment = mSecondFileViewModel.getFragment();
    }

    // 展示分类fragment
    public void showCategoryFileFragment(FragmentTransaction transaction){
        mTransaction = transaction;
        Log.i(TAG,"==> categoryFile fragment...");
        if(mCategoryFileViewModel.getFragment() == null){
            mCategoryFileViewModel.setFragment(CategoryFileFragment.newInstance("Main","null"));
            mTransaction.add(R.id.fragement_main, mCategoryFileViewModel.getFragment());
            mCategoryFileViewModel.getFragment().setRecyclerViewLinstener(mActivity);
        }
        mCategoryFileViewModel.getFragment().setPaths(quickService.getPaths());
        if(mCategoryFileViewModel.getQuickRecyclerViewAdapter() !=null){
            mCategoryFileViewModel.getQuickRecyclerViewAdapter().update(quickService.getPaths());
        }
        hideAllFragment(mTransaction);
        mTransaction.show(mCategoryFileViewModel.getFragment());
        mTransaction.commit();
        currentFragment = mCategoryFileViewModel.getFragment();
    }

    // 展示云同步fragment
    public void showCloudSyncFragment(FragmentTransaction transaction) {
        mTransaction = transaction;
        Log.i(TAG,"==> cloudSync fragment...");
        if(mCloudSyncViewModel.getFragment() == null){
            mCloudSyncViewModel.setFragment(CloudSyncFragment.newInstance(1));
            mTransaction.add(R.id.fragement_main, mCloudSyncViewModel.getFragment());
            mCloudSyncViewModel.getFragment().setRecyclerViewLinstener(mActivity);
        }
        if( mCloudSyncViewModel.getRecyclerView() !=null){
            loadSyncRoot();
        }
        hideAllFragment(mTransaction);
        mTransaction.show(mCloudSyncViewModel.getFragment());
        mTransaction.commit();
        currentFragment = mCloudSyncViewModel.getFragment();
    }

    // 获取本地viewAdapter
    public void getLocalFileRecyclerView(){
        Log.i(TAG,"load localFile RecycleView...");
        if(mLocalFileViewModel.getRecyclerView() == null){
            Log.i(TAG,"init localFile RecycleView...");
            mLocalFileViewModel.setRecyclerView((RecyclerView) mLocalFileViewModel.getFragment().getView());
            mLocalFileViewModel.setRecyclerViewAdapter((LcoalRecyclerViewAdapter) mLocalFileViewModel.getRecyclerView().getAdapter());
            mLocalFileViewModel.getRecyclerViewAdapter().setFABLinstener(mActivity);
        }
        // Toast.makeText(mActivity,"读取缓存",Toast.LENGTH_LONG).show();
        Log.i(TAG,"load localFile RecycleView success...");
    }

    // 获取快速viewAdapter
    public void getQuickRecyclerView(){
        Log.i(TAG,"load quickFile RecycleView...");
        if(mCategoryFileViewModel.getQuickRecyclerView()  == null){
            mCategoryFileViewModel.setQuickRecyclerView(
                    (RecyclerView) mCategoryFileViewModel.getFragment().getView().findViewById(R.id.list_quick));
            mCategoryFileViewModel.setQuickRecyclerViewAdapter(
                    (QuickFileRecyclerViewAdapter) mCategoryFileViewModel.getQuickRecyclerView().getAdapter());
            mCategoryFileViewModel.getQuickRecyclerViewAdapter().setEnterLocalFragmentListener(mActivity);
        }
        // Toast.makeText(mActivity,"读取缓存",Toast.LENGTH_LONG).show();
        Log.i(TAG,"load qucikFile RecycleView success...");
    }

    // 获取分类viewAdapter
    public void getCategoryRecyclerView(){
        Log.i(TAG,"load categoryFile RecycleView...");
        if( mCategoryFileViewModel.getCategoryRecyclerView() == null){
            mCategoryFileViewModel.setCategoryRecyclerView(
                    (RecyclerView) mCategoryFileViewModel.getFragment().getView().findViewById(R.id.list_category));
            mCategoryFileViewModel.setCategoryRecyclerViewAdapter(
                    (CategoryRecyclerViewAdapter) mCategoryFileViewModel.getCategoryRecyclerView().getAdapter());
            mCategoryFileViewModel.getCategoryRecyclerViewAdapter().setEnterLocalFragmentListener(mActivity);
        }
        // Toast.makeText(mActivity,"读取缓存",Toast.LENGTH_LONG).show();
        Log.i(TAG,"load categoryFile RecycleView success...");
    }

    // 获取操作viewAdapter
    public void getSecondRecyclerView(){
        Log.i(TAG,"load secondFile RecycleView...");
        if( mSecondFileViewModel.getRecyclerView() == null){
            mSecondFileViewModel.setRecyclerView((RecyclerView) mSecondFileViewModel.getFragment().getView());
            mSecondFileViewModel.setRecyclerViewAdapter(
                    (LcoalRecyclerViewAdapter) mSecondFileViewModel.getRecyclerView().getAdapter());
        }
        // Toast.makeText(mActivity,"读取缓存",Toast.LENGTH_LONG).show();
        Log.i(TAG,"load secondFile RecycleView success...");
    }

    // 获取同步viewAdapter
    public void getCloudSyncRecyclerView(){
        Log.i(TAG,"load cloudSync RecycleView...");
        if( mCloudSyncViewModel.getRecyclerView() == null){
            mCloudSyncViewModel.setRecyclerView((RecyclerView) mCloudSyncViewModel.getFragment().getView());
            mCloudSyncViewModel.setRecyclerViewAdapter(
                    (CloudSyncRecyclerViewAdapter) mCloudSyncViewModel.getRecyclerView().getAdapter());
            mCloudSyncViewModel.getRecyclerViewAdapter().setFABLinstener(mActivity);
        }
        // Toast.makeText(mActivity,"读取缓存",Toast.LENGTH_LONG).show();
        Log.i(TAG,"load cloudSync RecycleView success...");
    }

    public void onBack(){
        Log.i(TAG,"on back ...");
        if(currentFragment instanceof LocalFileFragment){
            // 是否为localFileFragment
            LocalFileFragment localFileFragment = (LocalFileFragment) currentFragment;
            if(localFileFragment.getIsSecond()==0){
                // 是否为主fragment
                Log.i(TAG,"localFile fragment back ...");
                getLocalFileRecyclerView();
                if(getLocalFileViewModel().getRecyclerViewAdapter().isMulited()){
                    // 数据是多选状态--退出多选状态
                    getLocalFileViewModel().getRecyclerViewAdapter().setMulited(false);
                    getLocalFileViewModel().getRecyclerViewAdapter().setAllChecked(false);
                    // 刷新数据
                    getLocalFileViewModel().getRecyclerViewAdapter().notifyDataSetChanged();
                    mActivity.onClickFAB(false);
                    return;
                }else if(getLocalFileViewModel().getRecyclerViewAdapter().isBack()){
                    // 可回到上一级
                    Log.i(TAG,"是否可退? true");
                    getLocalFileViewModel().getRecyclerViewAdapter().backPath();
                    return;
                }else{
                    Log.i(TAG,"是否可退 false");
                    // 返回分类
                    mActivity.showCategoryFileFragment();
                    return;
                }
            }else{
                // 是否为次fragment
                Log.i(TAG,"second fragment back ...");
                getSecondRecyclerView();
                if( mSecondFileViewModel.getRecyclerViewAdapter().isBack() ){
                    mSecondFileViewModel.getRecyclerViewAdapter().backPath();
                    return;
                }else{
                    // 退出复制或移动
                    destorySecond();
                    Toast.makeText(mActivity,"已退出复制或移动...",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        if(currentFragment instanceof CloudSyncFragment){
            Log.i(TAG,"cloudsync fragment back ...");
            getCloudSyncRecyclerView();
            if(getCloudSyncViewModel().getRecyclerViewAdapter().isMulited()){
                // 数据是多选状态--退出多选状态
                getCloudSyncViewModel().getRecyclerViewAdapter().setMulited(false);
                getCloudSyncViewModel().getRecyclerViewAdapter().setAllChecked(false);
                // 刷新数据
                getCloudSyncViewModel().getRecyclerViewAdapter().notifyDataSetChanged();
                mActivity.onClickFAB(false);
                return;
            }else if(getCloudSyncViewModel().getRecyclerViewAdapter().isBack()){
                // 可回到上一级
                Log.i(TAG,"是否可退? true");
                getCloudSyncViewModel().getRecyclerViewAdapter().backPath();
                return;
            }else{
                Log.i(TAG,"是否可退 false");
                // 返回分类
                mActivity.showCategoryFileFragment();
                return;
            }
        }
        if(currentFragment instanceof CategoryFileFragment){
            Log.i(TAG,"category fragment back ...");
            long mNowTime = System.currentTimeMillis();
            if((mNowTime - mPressedTime) > 2000){//比较两次按键时间差
                Log.i(TAG,"exit");
                Toast.makeText(mActivity, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mPressedTime = mNowTime;
            }else{//退出程序
                mActivity.finish();
                System.exit(0);
            }
            return;
        }
    }
    private long mPressedTime = 0;

    private List<FileBean> listBean;

    public void copy(View view){
        if(listBean != null){
            Log.i(TAG,"copy 操作进行中...");
            Toast.makeText(mActivity,"操作进行中...",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG,"copy start...");
        Toast.makeText(mActivity,"已选择...",Toast.LENGTH_SHORT).show();
        // 设置操作路径集合
        listBean = mLocalFileViewModel.getRecyclerViewAdapter().getSelectItem();
        if(listBean.size() == 0){
            Log.i(TAG,"请选择复制文件");
            Toast.makeText(mActivity,"请选择复制文件...",Toast.LENGTH_SHORT).show();
            listBean = null;
            return;
        }
        // copy操作时创建次fragment
        mActivity.setToolBar("复制中...");
        showSecondFragment(mActivity.getSupportFragmentManager().beginTransaction(),1);
    }

    public void move(View view) {
        if(listBean != null){
            Log.i(TAG,"move 进行中...");
            Toast.makeText(mActivity,"操作进行中...",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG,"move start...");
        Toast.makeText(mActivity,"已选择...",Toast.LENGTH_SHORT).show();
        // 设置操作路径集合
        listBean = mLocalFileViewModel.getRecyclerViewAdapter().getSelectItem();
        if(listBean.size() == 0){
            Log.i(TAG,"请选择移动文件");
            listBean = null;
            Toast.makeText(mActivity,"请选择移动文件...",Toast.LENGTH_SHORT).show();
            return;
        }
        // move操作时创建次fragment
        mActivity.setToolBar("移动中...");
        showSecondFragment(mActivity.getSupportFragmentManager().beginTransaction(),2);
    }

    public void delete(View view){
        if(listBean != null){
            Log.i(TAG,"进行中...");
            Toast.makeText(mActivity,"操作进行中...",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG,"delete...");
        Toast.makeText(mActivity,"已选择...",Toast.LENGTH_SHORT).show();
        // 设置操作路径集合
        listBean = mLocalFileViewModel.getRecyclerViewAdapter().getSelectItem();
        if(listBean.size() == 0){
            Log.i("错误","请选择删除文件");
            listBean = null;
            Toast.makeText(mActivity,"请选择删除文件...",Toast.LENGTH_SHORT).show();
            return;
        }
        for(FileBean srcFile:listBean){
            Log.i(TAG,"delete "+srcFile.getPath());
            FileUtils.delete(srcFile);
        }
        // 文件复制完成
        Log.i("文件操作","删除...");
        mActivity.onClickFAB(false);
        mLocalFileViewModel.getRecyclerViewAdapter().update(listBean,1);
        listBean = null;
        Toast.makeText(mActivity,"已删除...",Toast.LENGTH_SHORT).show();
    }

    public void paste(View view) {
        Log.i(TAG,"paste...");
        if(listBean==null||listBean.size()==0){
            Log.i(TAG,"file is none...");
            Toast.makeText(mActivity,"请选择文件...",Toast.LENGTH_SHORT).show();
            return;
        }
        // 判断是否为second
        if(currentFragment instanceof LocalFileFragment){
            LocalFileFragment localFileFragment = (LocalFileFragment) currentFragment;
            if(localFileFragment.getIsSecond()!=0){
                // 为次fragment
                String resPath = mSecondFileViewModel.getRecyclerViewAdapter().getPathStack().peekLast();
                Log.i(TAG,"patse path: " + resPath);
                if(mSecondFileViewModel.getFragment().getIsSecond()==1){
                    for(FileBean srcFile:listBean){
                        Log.i(TAG,"copy paste...");
                        FileUtils.copy(srcFile,resPath);
                    }
                    Log.i(TAG,"copy success...");
                    Toast.makeText(mActivity,"已复制...",Toast.LENGTH_SHORT).show();
                }else if(mSecondFileViewModel.getFragment().getIsSecond()==2){
                    for(FileBean srcFile:listBean){
                        Log.i(TAG,"move paste...");
                        FileUtils.move(srcFile,resPath);
                    }
                    Log.i(TAG,"move success...");
                    Toast.makeText(mActivity,"已移动...",Toast.LENGTH_SHORT).show();
                }
                mActivity.onClickFAB(false);
                destorySecond();
            }
        }
    }

    public void addQuick(View view){
        if(listBean != null){
            Log.i(TAG,"upload 操作进行中...");
            Toast.makeText(mActivity,"操作进行中...",Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(mActivity,"已选择...",Toast.LENGTH_SHORT).show();
        // 设置操作路径集合
        listBean = mLocalFileViewModel.getRecyclerViewAdapter().getSelectItem();
        if(listBean.size() == 0){
            Log.i(TAG,"请选择添加路径");
            Toast.makeText(mActivity,"请选择添加路径...",Toast.LENGTH_SHORT).show();
            listBean = null;
            return;
        }
        List<String> paths = new ArrayList<>(listBean.size());
        for(FileBean fileBean:listBean){
            if(fileBean.getDir()){
                paths.add(fileBean.getPath());
            }else{
                paths.add(fileBean.getPath()+"/"+fileBean.getName());
            }
        }
        quickService.setPath(paths);
        Toast.makeText(mActivity,"添加完成...",Toast.LENGTH_SHORT).show();
        mActivity.onClickFAB(false);
        listBean = null;
        getLocalFileViewModel().getRecyclerViewAdapter().setMulited(false);
        Log.i("取消多选状态", "取消多选菜单");
        // 清空数据的多选状态
        getLocalFileViewModel().getRecyclerViewAdapter().setAllChecked(false);
        getLocalFileViewModel().getRecyclerViewAdapter().notifyDataSetChanged();
    }

    public void upload(View view) {
        if(listBean != null){
            Log.i(TAG,"upload 操作进行中...");
            Toast.makeText(mActivity,"操作进行中...",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG,"upload start...");
        Toast.makeText(mActivity,"开始上传...",Toast.LENGTH_SHORT).show();
        // 设置操作路径集合
        listBean = mLocalFileViewModel.getRecyclerViewAdapter().getSelectItem();
        if(listBean.size() == 0){
            Log.i(TAG,"请选择上传文件");
            Toast.makeText(mActivity,"请选择上传文件...",Toast.LENGTH_SHORT).show();
            listBean = null;
            return;
        }
        try {
            for(FileBean fileBean:listBean){
                if(fileBean.getDir()){
                    Toast.makeText(mActivity,"本软件只支持同步文件...",Toast.LENGTH_SHORT).show();
                    listBean = null ;
                    // uploadSync(fileBean.getPath());
                }else{
                    uploadSync(fileBean.getPath()+"/"+fileBean.getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void download(View view){
        if(listBean != null){
            Log.i(TAG,"download 操作进行中...");
            Toast.makeText(mActivity,"操作进行中...",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG,"download start...");
        Toast.makeText(mActivity,"开始下载...",Toast.LENGTH_SHORT).show();
        // 设置操作路径集合
        listBean = mCloudSyncViewModel.getRecyclerViewAdapter().getSelectItem();
        if(listBean.size() == 0){
            Log.i(TAG,"请选择下载文件");
            Toast.makeText(mActivity,"请选择下载文件...",Toast.LENGTH_SHORT).show();
            listBean = null;
            return;
        }
        try {
            for(FileBean fileBean:listBean){
                if(fileBean.getDir()){
                    Toast.makeText(mActivity,"本软件只支持下载文件...",Toast.LENGTH_SHORT).show();
                }else{
                    Log.i(TAG,fileBean.getPath());
                    downloadSync(fileBean.getPath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadSync(String filepath){
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg)
            {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                // 4、接收消息并执行UI的更新操作
                if (msg.obj != null)
                {
                    if((Boolean) msg.obj==true){
                        Toast.makeText(mActivity,"下载完成...",Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "同步完成...");
                    }else{
                        Toast.makeText(mActivity,"下载失败...",Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "同步失败...");
                    }
                    listBean = null;
                    mActivity.onClickFAB(false);
                    getCloudSyncViewModel().getRecyclerViewAdapter().setMulited(false);
                    getCloudSyncViewModel().getRecyclerViewAdapter().setAllChecked(false);
                    getCloudSyncViewModel().getRecyclerViewAdapter().notifyDataSetChanged();
                } else
                {
                    Toast.makeText(mActivity,"网络异常...",Toast.LENGTH_SHORT).show();
                }
            }
        };
        SyncService.download(filepath, handler);
    }

    private void uploadSync(String filepath){
        Handler mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                // 4、接收消息并执行UI的更新操作
                if (msg.obj != null)
                {
                    // update(JsonToBean.jsonToFileBean((String) msg.obj));
                    if((Boolean) msg.obj==true){
                       Toast.makeText(mActivity,"上传完成...",Toast.LENGTH_SHORT).show();
                    }else{
                       Toast.makeText(mActivity,"上传失败...",Toast.LENGTH_SHORT).show();
                    }
                    mActivity.onClickFAB(false);
                    listBean = null;
                    getLocalFileViewModel().getRecyclerViewAdapter().setMulited(false);
                    Log.i("取消多选状态", "取消多选菜单");
                    getLocalFileViewModel().getRecyclerViewAdapter().setAllChecked(false);
                    getLocalFileViewModel().getRecyclerViewAdapter().notifyDataSetChanged();
                } else
                {
                    // Log.i("sync", "不能读取到网络信息!");
                    Toast.makeText(mActivity,"不能连接到网络...",Toast.LENGTH_SHORT).show();
                }
            }

        };
        SyncService.upload(filepath, mHandler);
    }

    public void searchFile(String key){
        mLocalFileViewModel.getFragment().setRootPath(null);
        if(mLocalFileViewModel.getRecyclerViewAdapter() != null)
            mLocalFileViewModel.getRecyclerViewAdapter().clearValue();
        Toast.makeText(mActivity,"搜索中...",Toast.LENGTH_SHORT).show();
        // mActivity.showBar(true);
        Handler mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                // 4、接收消息并执行UI的更新操作
                if (msg.obj != null)
                {
                    // update(JsonToBean.jsonToFileBean((String) msg.obj));
                    // 搜索完成后
                    mLocalFileViewModel.getRecyclerViewAdapter().update((List<FileBean>) msg.obj);
                    mActivity.setToolBar("搜索");
                    // mActivity.showBar(false);
                    Toast.makeText(mActivity,"搜索完成...",Toast.LENGTH_SHORT).show();

                } else
                {
                    Log.i("sync", "不能读取到网络信息!");
                }
            }
        };
        // 搜索方法
        FileFilterUtils.searchFile(key, mHandler);
    }

    private void destorySecond(){
        // 切回主fragment
        listBean = null;
        mSecondFileViewModel.getFragment().onDestroy();
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        showLocalFileFragment(transaction);
        mLocalFileViewModel.getRecyclerViewAdapter().initPathStack(mSecondFileViewModel.getRecyclerViewAdapter().getPathStack());
        mSecondFileViewModel.setFragment(null);
        mSecondFileViewModel.setRecyclerView(null);
        mSecondFileViewModel.setRecyclerViewAdapter(null);
        mLocalFileViewModel.getRecyclerViewAdapter().update(null,1);
    }

    public void enterQuick(FileBean fileBean){
        Log.i(TAG,"enter quick...");
        mLocalFileViewModel.getFragment().setRootPath(fileBean.getPath());
        if(mLocalFileViewModel.getRecyclerViewAdapter() !=null){
            mLocalFileViewModel.getRecyclerViewAdapter().update(fileBean.getPath());
        }
    }

    private void loadLocalFileRoot(){
        Log.i(TAG,"load localFile root...");
        mLocalFileViewModel.getFragment().setRootPath(ConstVaule.ROOT_PATH);
        if(mLocalFileViewModel.getRecyclerViewAdapter() !=null){
            mLocalFileViewModel.getRecyclerViewAdapter().update(ConstVaule.ROOT_PATH);
        }
    }

    private void loadSyncRoot(){
        Log.i(TAG,"load localFile root...");
        mCloudSyncViewModel.getFragment().setRootPath("/");
        if(mCloudSyncViewModel.getRecyclerViewAdapter() !=null){
            mCloudSyncViewModel.getRecyclerViewAdapter().update("/");
        }
    }

    public void enterCategory(FileBean fileBean){
        mLocalFileViewModel.getFragment().setRootPath(null);
        if(mLocalFileViewModel.getRecyclerViewAdapter() != null)
            mLocalFileViewModel.getRecyclerViewAdapter().clearValue();
        switch (fileBean.getName()){
            case "图片":
                Log.i(TAG,"图片入口");
                Toast.makeText(mActivity,"图片搜索中...",Toast.LENGTH_SHORT).show();
                categoryASYNC(FileFilterUtils.photoFilter, "图片");
                mActivity.flag = 1;
                break;
            case "视频":
                Log.i(TAG,"视频入口");
                Toast.makeText(mActivity,"视频搜索中...",Toast.LENGTH_SHORT).show();
                categoryASYNC(FileFilterUtils.videoFilter, "视频");
                mActivity.flag = 2;
                break;
            case "音乐":
                Log.i(TAG,"音乐入口");
                Toast.makeText(mActivity,"音乐搜索中...",Toast.LENGTH_SHORT).show();
                categoryASYNC(FileFilterUtils.musicFilter, "音乐");
                mActivity.flag = 3;
                break;
            case "文档":
                Log.i(TAG,"文档入口");
                Toast.makeText(mActivity,"文档搜索中...",Toast.LENGTH_SHORT).show();
                categoryASYNC(FileFilterUtils.txtFilter, "文档");
                mActivity.flag = 4;
                break;
            case "安装包":
                Log.i(TAG,"安装包入口");
                Toast.makeText(mActivity,"安装包搜索中...",Toast.LENGTH_SHORT).show();
                categoryASYNC(FileFilterUtils.installFilter, "安装包");
                mActivity.flag = 5;
                break;
            case "压缩包":
                Log.i(TAG,"压缩包入口");
                Toast.makeText(mActivity,"压缩包搜索中...",Toast.LENGTH_SHORT).show();
                categoryASYNC(FileFilterUtils.archiveFilter, "压缩包");
                mActivity.flag = 6;
                break;
        }
    }

    private void categoryASYNC(List<FilenameFilter> filters, String name){
        Handler mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                // 4、接收消息并执行UI的更新操作
                if (msg.obj != null)
                {
                    mLocalFileViewModel.getRecyclerViewAdapter().update((List<FileBean>) msg.obj);
                    mActivity.setToolBar(name);
                    Log.i(TAG,"async 分类失败");
                    mActivity.showBar(false);
                } else
                {
                    Log.i(TAG,"async 分类失败");
                }
            }

        };
        FileFilterUtils.getCategoryFile(ConstVaule.ROOT_PATH, filters, mHandler);
    }
}
