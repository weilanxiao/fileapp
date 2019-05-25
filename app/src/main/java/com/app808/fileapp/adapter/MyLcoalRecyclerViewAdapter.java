package com.app808.fileapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app808.fileapp.R;
import com.app808.fileapp.dummy.LocalFileDummy;
import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.fragment.LcoalFileFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FileBean} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLcoalRecyclerViewAdapter extends RecyclerView.Adapter<MyLcoalRecyclerViewAdapter.ViewHolder> {

    private final List<LocalFileDummy> mValues;
    private final OnListFragmentInteractionListener mListener;

    private static final int NAME_LENGTH = 20;
    private boolean mIsMulited;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private TextView mTextName;
        private TextView mTextLastDate;
        private CheckBox mCheckBox;

        private LocalFileDummy mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTextName = (TextView) mView.findViewById(R.id.list_item_txt_file_name);
            mTextLastDate = (TextView) mView.findViewById(R.id.list_item_txt_file_lastdate);
            mCheckBox = (CheckBox) mView.findViewById(R.id.list_item_ckeckBox_file_check);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextName.getText() + "'";
        }
    }

    public MyLcoalRecyclerViewAdapter(List<LocalFileDummy> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mIsMulited = false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_item_local, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // 绑定页面数据
        holder.mItem = mValues.get(position);
        holder.mTextName.setText(displayFileName(mValues.get(position).getData().getName()));
        holder.mTextLastDate.setText(String.valueOf(mValues.get(position).getData().getLastData()));
        if(mIsMulited){
            //多选框可见
            holder.mCheckBox.setVisibility(View.VISIBLE);
            //选中当前项
            holder.mCheckBox.setChecked(mValues.get(position).isChecked());
            Log.i(position + "是否选中", String.valueOf(holder.mCheckBox.isChecked()));
        }else{
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
                    }else{
                        Log.i("进入多选状态", String.valueOf(mValues.get(position).getData().getName()));
                        // 选中当前项
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
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    System.out.println("...item长按事件...");

                    //如果当前不为多选状态
                    if(!mIsMulited){
                        // 将列表设置为多选状态
                        mIsMulited = true;
                        // setMulitedAndDisplay(mainListAdapter, true);
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
}
