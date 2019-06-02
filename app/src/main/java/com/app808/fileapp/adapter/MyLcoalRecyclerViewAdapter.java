package com.app808.fileapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.app808.fileapp.R;
import com.app808.fileapp.dummy.LocalFileDummy;
import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.fragment.LocalFileFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FileBean} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLcoalRecyclerViewAdapter extends RecyclerView.Adapter<MyLcoalRecyclerViewAdapter.ViewHolder> {

    private final List<LocalFileDummy> mValues;
    private final OnListFragmentInteractionListener mListener;

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

    public MyLcoalRecyclerViewAdapter(String rootPath, OnListFragmentInteractionListener listener) {
        mValues = LocalFileDummy.loadData(rootPath);
        mListener = listener;
        mIsMulited = false;
        pushPath(rootPath);
    }

    public void reverseChecked(){
        for(LocalFileDummy fileDummy: mValues){
            fileDummy.setChecked(!fileDummy.isChecked());
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_item_local, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // 绑定页面数据
        FileBean fileBean = mValues.get(position).getData();
        holder.mItem = mValues.get(position);
        holder.mTextName.setText(displayFileName(fileBean.getName()));
        holder.mTextLastDate.setText(String.valueOf(fileBean.getLastData()));
        if(fileBean.getDir()){
            holder.mImageView.setImageResource(R.drawable.folder2);
        }else{
            holder.mImageView.setImageResource(R.drawable.file2);
        }
        if(mIsMulited){
            //多选框可见
            holder.mCheckBox.setVisibility(View.VISIBLE);
            if(mAllChecked){
                // 全选
                holder.mCheckBox.setChecked(true);
                mValues.get(position).setChecked(false);
            }else {
//                if(holder.mCheckBox.isSelected()){
//                    // 当前项被选中
//                    mValues.get(position).setChecked(true);
//                }

                 holder.mCheckBox.setChecked(mValues.get(position).isChecked());
            }
            Log.i(position + "是否选中", String.valueOf(holder.mCheckBox.isChecked()));
        }else{
            // 清空多选标志
            if(!mAllChecked){
                // 全选
                holder.mCheckBox.setChecked(false);
                mValues.get(position).setChecked(false);
            }
            holder.mCheckBox.setVisibility(View.INVISIBLE);
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
                        // notifyItemChanged(position);
                        if(mValues.get(position).getData().getDir()){
                            Log.i("...item点击事件...",mValues.get(position).getData().getPath());
                            String path = mValues.get(position).getData().getPath();
                            mValues.clear();
                            pushPath(path);
                            mValues.addAll(loadPath(path));
                        }else{
                            Log.i("...item点击事件...","不是文件夹...");
                        }
                    }else{
                        // View ck = v.findViewById(R.id.list_item_ckeckBox_file_check);
                        // 手动触发点击事件
                        // ck.performClick();
                        // Log.i("进入多选状态", String.valueOf(mValues.get(position).getData().getName()));
                        // 反选当前项
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
                        mValues.get(position).setChecked(!mValues.get(position).isChecked());
                        // v.refreshDrawableState();
                        // notifyItemChanged(position);
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

    public boolean isBack(){
        return pathStack.size() > 0;
    }

    public void backPath(){
        if(pathStack.size() > 0){
            pathStack.pollLast();
            mValues.clear();
            mValues.addAll(loadPath(pathStack.peekLast()));
            notifyDataSetChanged();
        }
    }

    public void pushPath(String rootPath){
        pathStack.addLast(rootPath);
    }

    public List<LocalFileDummy> loadPath(String rootPath){
        Log.i("rootPath",pathStack.getLast());
        return LocalFileDummy.loadData(rootPath);
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
                // Log.i("添加item",file.getData().getName());
                list.add(file.getData());
            }
        }
        Log.i("list size",String.valueOf(list.size()));
        return list;
    }
}
