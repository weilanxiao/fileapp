package com.app808.fileapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app808.fileapp.R;
import com.app808.fileapp.dummy.LocalFileDummy;
import com.app808.fileapp.entity.ConstVaule;
import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.fragment.LocalFileFragment.OnListFragmentInteractionListener;
import com.app808.fileapp.service.OpenFileService;
import com.app808.fileapp.utils.FileFilterUtils;
import com.app808.fileapp.utils.FileSyncUtils;
import com.app808.fileapp.utils.JsonToBean;
import com.app808.fileapp.utils.OpenIntent;
import com.app808.fileapp.utils.SuffixUtils;

import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FileBean} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class LcoalRecyclerViewAdapter extends RecyclerView.Adapter<LcoalRecyclerViewAdapter.ViewHolder>
        implements com.app808.fileapp.callBack.FABListener {

    private static final String TAG = "LocalRecyclerViewAdapter";

    private final List<LocalFileDummy> mValues;
    private final OnListFragmentInteractionListener mListener;
    private FABListener mFABLinstener;

    private static final int NAME_LENGTH = 20;

    public boolean isMulited() {
        return mIsMulited;
    }

    public void setMulited(boolean mulited) {
        mIsMulited = mulited;
    }

    private boolean mIsMulited;
    private boolean mAllChecked;


    // 全选
    public void setAllChecked(boolean isChecked) {
        mAllChecked= isChecked;
    }

    @Override
    public void onClickFAB(boolean flag) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private TextView mTextName;
        private TextView mTextLastDate;
        private CheckBox mCheckBox;
        private ImageView mImageView;

        private LocalFileDummy mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTextName = (TextView) mView.findViewById(R.id.list_item_txt_file_name);
            mTextLastDate = (TextView) mView.findViewById(R.id.list_item_txt_file_lastdate);
            mCheckBox = (CheckBox) mView.findViewById(R.id.list_item_ckeckBox_file_check);
            mImageView = (ImageView) mView.findViewById(R.id.list_item_image_folder);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextName.getText() + "'";
        }
    }

    public LcoalRecyclerViewAdapter(String rootPath, OnListFragmentInteractionListener listener, FilenameFilter filter) {
        Log.i(TAG,"init...");
        if(rootPath != null){
            mListener = listener;
            mIsMulited = false;
            clearPathStack();
            mValues = LocalFileDummy.loadData(rootPath);
            setPath(rootPath);
        } else{
            mValues = new ArrayList<>();
            mListener = listener;
            clearPathStack();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_item_local, parent, false);
        return new ViewHolder(view);
    }

    public void showIcon(ViewHolder holder, String name){
        if(SuffixUtils.isMusic(name)) {
            holder.mImageView.setImageResource(R.drawable.ic_music);
        } else if(SuffixUtils.isVideo(name)){
            holder.mImageView.setImageResource(R.drawable.ic_video);
        } else if(SuffixUtils.isPhoto(name)){
            holder.mImageView.setImageResource(R.drawable.ic_photo);
        } else if(SuffixUtils.isInsatll(name)){
            holder.mImageView.setImageResource(R.drawable.ic_install);
        } else if(SuffixUtils.isArchive(name)){
            holder.mImageView.setImageResource(R.drawable.ic_archive);
        }else{
            holder.mImageView.setImageResource(R.drawable.file);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // 绑定页面数据
        FileBean fileBean = mValues.get(position).getData();
        holder.mItem = mValues.get(position);
        holder.mTextName.setText(displayFileName(fileBean.getName()));
        holder.mTextLastDate.setText(String.valueOf(fileBean.getLastData()));
        if(fileBean.getDir()){
            holder.mImageView.setImageResource(R.drawable.folder);
        }else{
            showIcon(holder, fileBean.getName());
        }
        if(mIsMulited){
            //多选框可见
            holder.mCheckBox.setVisibility(View.VISIBLE);
            if(mAllChecked){
                // 全选
                holder.mCheckBox.setChecked(true);
                mValues.get(position).setChecked(false);
            }else {
                holder.mCheckBox.setChecked(mValues.get(position).isChecked());
            }
        }else{
            // 清空多选标志
            if(!mAllChecked){
                // 为假
                holder.mCheckBox.setChecked(false);
                mValues.get(position).setChecked(false);
            }
            holder.mCheckBox.setVisibility(View.INVISIBLE);
            onClickFAB(false);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    System.out.println("...item点击事件...");
                    //如果当前不为多选状态
                    if(!mIsMulited){
                        // 进入下级目录
                        if(mValues.get(position).getData().getDir()){
                            Log.i("...item点击事件...",mValues.get(position).getData().getPath());
                            String path = mValues.get(position).getData().getPath();
                            mValues.clear();
                            pushPath(path);
                            mValues.addAll(loadPath(path));
                            Log.i("path stack",String.valueOf(pathStack.size()));
                        }else{
                            Log.i("...item点击事件...","不是文件夹...");
                            OpenFileService.openFile(holder.mView.getContext(),fileBean.getPath()+'/'+fileBean.getName());
                        }
                    }else{
                        mValues.get(position).setChecked(!mValues.get(position).isChecked());
                    }
                    notifyDataSetChanged();
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mListener) {
                    System.out.println("...item长按事件...");
                    //如果当前不为多选状态
                    if(!mIsMulited){
                        // 将列表设置为多选状态
                        mIsMulited = true;
                        Log.i("进入多选状态", String.valueOf(mValues.get(position).getData().getName()));
                        // 选中当前项
                        // mBottomDialog.show();
                        mValues.get(position).setChecked(!mValues.get(position).isChecked());
                        mFABLinstener.onClickFAB(true);
                    }else{
                        Log.i("进入多选状态", String.valueOf(mValues.get(position).getData().getName()));
                        // 选中当前项
                        mValues.get(position).setChecked(!mValues.get(position).isChecked());
                    }
                    notifyDataSetChanged();
                    mListener.onListFragmentInteraction(holder.mItem);
                    return true;
                }
                return false;
            }
        });

        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Check ",String.valueOf(position));
                mValues.get(position).setChecked(holder.mCheckBox.isChecked());
            }
        });
    }

    private final LinkedList<String> pathStack = new LinkedList<String>();

    public LinkedList<String> getPathStack() {
        for(String path:pathStack)
            Log.i("path stack",path);
        Log.i("path stack id... ",pathStack.toString());
        return pathStack;
    }

    public void clearPathStack(){
        pathStack.clear();
    }

    public void initPathStack(LinkedList<String> stack){
        clearPathStack();
        for(String path:stack){
            pathStack.addLast(path);
        }
        Log.i("初始化栈完成",String.valueOf(pathStack.size()));
    }

    public boolean isBack(){
        return pathStack.size() > 1;
    }

    public void backPath(){
        if(pathStack.size() > 0){
            pathStack.pollLast();
            mValues.clear();
            mValues.addAll(loadPath(pathStack.peekLast()));
            notifyDataSetChanged();
        }
    }

    public void clearValue(){
        // 取消多选状态
        mIsMulited = false;
        // 取消选择
        mAllChecked = false;
        mValues.clear();
        clearPathStack();
        notifyDataSetChanged();
    }

    private void pushPath(String rootPath){
        Log.i("path stack id",pathStack.toString());
        pathStack.addLast(rootPath);
    }

    private List<LocalFileDummy> loadPath(String rootPath){
        Log.i("rootPath",pathStack.getLast());
        return LocalFileDummy.loadData(rootPath);
    }

    public void update(List<FileBean> datas, int flag){
        // 取消多选状态
        mIsMulited = false;
        // 取消选择
        mAllChecked = false;
        if(pathStack.size()==0 && datas !=null){
            List<Integer> integers = new ArrayList<>();
            for(int i=0;i<mValues.size();i++){
                for(FileBean fileBean:datas) {
                    if(mValues.get(i).getData().getName().equals(fileBean.getName())){
                        integers.add(i);
                    }
                }
            }
            for(int i:integers){
                mValues.remove(i);
            }
        }else{
            mValues.clear();
            mValues.addAll(loadPath(pathStack.peekLast()));
        }
        notifyDataSetChanged();
    }

    public void update(String rootpath){
        mValues.clear();
        mValues.addAll(LocalFileDummy.loadData(rootpath));
        setPath(rootpath);
    }

    public void update(List<FileBean> fileBeans){
        clearPathStack();
        mValues.clear();
        List<LocalFileDummy> fileDummies = new ArrayList<>(fileBeans.size());
        for(FileBean fileBean:fileBeans){
            fileDummies.add(new LocalFileDummy(fileBean,false));
        }
        mValues.addAll(fileDummies);
        Log.i(TAG, String.valueOf(pathStack.size()));
        notifyDataSetChanged();
    }

    private void setPath(String rootpath){
        Log.i("rootpath", rootpath);
        int j = "/storage/emulated/0".length();
        String root = rootpath.substring(0,j);
        String sumarr = "";
        clearPathStack();
        pushPath(root);
        String[] sourceStrArray = rootpath.substring(j).split("/");
        for (int i = 0; i < sourceStrArray.length; i++) {
            if(i == 0)
                continue;
            sumarr = sumarr + "/" + sourceStrArray[i];
            Log.i("quick path",root+sumarr);
            pushPath(root+sumarr);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    // 控制文件名在控件上的显示长度
    private String displayFileName(String name){
        if(name.length() > NAME_LENGTH){
            return name.substring(0,NAME_LENGTH) + "...";
        }else{
            return name;
        }
    }


    public void sortName(boolean asc){
        // mValues.sort(Comparator.comparing((LocalFileDummy locaFileDummy)->locaFileDummy.getData().getName()));
        if(asc)
            mValues.sort(Comparator.comparing((LocalFileDummy locaFileDummy)->locaFileDummy.getData().getName()));
        else
            mValues.sort(Comparator.comparing((LocalFileDummy locaFileDummy)->locaFileDummy.getData().getName()).reversed());

        notifyDataSetChanged();
    }

    public void sortDate(boolean asc){
        // mValues.sort(Comparator.comparing((LocalFileDummy locaFileDummy)->locaFileDummy.getData().getName()));
        if(asc)
            mValues.sort(Comparator.comparing((LocalFileDummy locaFileDummy)->locaFileDummy.getData().getLastData()));
        else
            mValues.sort(Comparator.comparing((LocalFileDummy locaFileDummy)->locaFileDummy.getData().getLastData()).reversed());

        notifyDataSetChanged();
    }

    public List<FileBean> getSelectItem(){
        List<FileBean> list = new ArrayList<>();
        for (LocalFileDummy file: mValues) {
            if(file.isChecked()){
                list.add(file.getData());
            }
        }
        Log.i("list size",String.valueOf(list.size()));
        return list;
    }

    public List<String> getSelectPath(){
        List<String> list = new ArrayList<>();
        for (LocalFileDummy file: mValues) {
            if(file.isChecked()){
                // Log.i("添加item",file.getData().getName());
                list.add(file.getData().getPath());
            }
        }
        return list;
    }

    public void setFABLinstener(FABListener fabLinstener){
        mFABLinstener = fabLinstener;
    }

    public interface FABListener {
        void onClickFAB(boolean flag);
    }

    public void reverseChecked(){
        for(LocalFileDummy fileDummy: mValues){
            fileDummy.setChecked(!fileDummy.isChecked());
        }
        notifyDataSetChanged();
    }
}
