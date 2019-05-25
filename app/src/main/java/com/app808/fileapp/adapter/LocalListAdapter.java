package com.app808.fileapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app808.fileapp.R;
import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class LocalListAdapter extends BaseAdapter {

    private Context mcontext;
    private LayoutInflater mlayoutInflater;
    private static final int NAME_LENGTH = 20;
    private boolean isMulited;
    private List<ViewHolder> mViewHolders;
    private TextView mTextName;
    private TextView mTextPath;
    private CheckBox mCheckBox;

    public class ViewHolder{
        public FileBean data;
        public boolean isChecked;
        ViewHolder(FileBean data){
            this.data = data;
            this.isChecked = false;
        }
    }

    public LocalListAdapter(Context context, String path) {
        this.mcontext = context;
        this.mlayoutInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isMulited = false;
        loadData(path);
        System.out.println("读取文件中....");
        System.out.println(getCount());
    }

    public LocalListAdapter(String path) {
        this.isMulited = false;
        loadData(path);
        System.out.println("读取文件中....");
        System.out.println(getCount());
    }

    @Override
    public int getCount() {
        return mViewHolders.size();
    }

    @Override
    public Object getItem(int position) {
        return mViewHolders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mlayoutInflater.inflate(R.layout.fragment_list_item_local, null);
        //获取item控件
        mTextName = convertView.findViewById(R.id.list_item_txt_file_name);
        mTextPath = convertView.findViewById(R.id.list_item_txt_file_lastdate);
        mCheckBox = convertView.findViewById(R.id.list_item_ckeckBox_file_check);

        // 控件填充数据
        ViewHolder viewHolder = mViewHolders.get(position);
        FileBean file = viewHolder.data;
        mTextName.setText(displayFileName(file.getName()));
        mTextPath.setText(String.valueOf(file.getLastData()));

        if(isMulited){
            //多选框可见
            mCheckBox.setVisibility(View.VISIBLE);
            //选中当前项
            mCheckBox.setChecked(viewHolder.isChecked == true? true : false);
            Log.i(position + "是否选中", String.valueOf(mCheckBox.isChecked()));
        }else{
            mCheckBox.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    // 重置vh的isCkecked
    public void reflushVHisChecked(){
        for(ViewHolder vh : mViewHolders){
            vh.isChecked = false;
        }
    }

    /**
     * 是否为多选状态
     * */
    public boolean isMulited() {
        return isMulited;
    }

    /**
     * 设置是否为多选状态
     * */
    public void setMulited(boolean mulited) {
        isMulited = mulited;
    }

    /**
     * 设置item的选中状态
     * */
    public void setItemState(int position, boolean state){
        mViewHolders.get(position).isChecked = state;
    }

    /**
     * 获取list存放的数据
     * */
    public List<ViewHolder> getViewHolders() {
        return mViewHolders;
    }

    public void setViewHolders(List<ViewHolder> viewHolders) {
        mViewHolders = viewHolders;
    }

    /**
     * 加载数据并刷新显示
     * */
    public void loadData(String path){
        List<FileBean> fileBeans = FileUtils.getFile(path);
        mViewHolders = new ArrayList<>(fileBeans.size());
        for(FileBean fileBean : fileBeans){
            mViewHolders.add(new ViewHolder(fileBean));
        }
        reflushAdapter();
    }

    /**
     * 刷新数据
     * */
    public void reflushAdapter(){
        notifyDataSetChanged();
    }

    // 控制文件名在控件上的显示长度
    private String displayFileName(String name){
        if(name.length() > NAME_LENGTH){
            return name.substring(0,NAME_LENGTH) + "...";
        }else{
            return name;
        }
    }
}
