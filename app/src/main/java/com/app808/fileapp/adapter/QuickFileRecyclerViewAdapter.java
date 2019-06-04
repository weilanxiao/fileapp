package com.app808.fileapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app808.fileapp.R;
import com.app808.fileapp.dummy.CategoryDummy;
import com.app808.fileapp.dummy.QuickFileDummy;
import com.app808.fileapp.entity.CategoryBean;
import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.fragment.CategoryFileFragment;

import java.util.List;

public class QuickFileRecyclerViewAdapter extends RecyclerView.Adapter<QuickFileRecyclerViewAdapter.ViewHolder>  {

    private final List<QuickFileDummy> mValues;
    private final CategoryFileFragment.OnFragmentInteractionListener mListener;
    private static final int NAME_LENGTH = 20;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private TextView mTextName;
        private TextView mTextLastDate;
        private ImageView mImageView;

        private QuickFileDummy mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTextName = (TextView) mView.findViewById(R.id.list_item_txt_file_name_quick);
            mTextLastDate = (TextView) mView.findViewById(R.id.list_item_txt_file_lastdate_quick);
            mImageView = (ImageView) mView.findViewById(R.id.list_item_image_folder_quick);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextName.getText() + "'";
        }
    }

    public QuickFileRecyclerViewAdapter(List<String> data, CategoryFileFragment.OnFragmentInteractionListener listener){
        mValues = QuickFileDummy.loadData(data);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_list_item_quick, parent, false);
        return new ViewHolder(view);
    }

    // 控制文件名在控件上的显示长度
    private String displayFileName(String name){
        if(name.length() > NAME_LENGTH){
            return name.substring(0,NAME_LENGTH) + "...";
        }else{
            return name;
        }
    }

    @Override
    public void onBindViewHolder(final QuickFileRecyclerViewAdapter.ViewHolder holder, final int position) {
        // 绑定页面数据
        FileBean fileBean = mValues.get(position).getData();
        holder.mItem = mValues.get(position);
        holder.mTextName.setText(displayFileName(fileBean.getName()));
        holder.mTextLastDate.setText(String.valueOf(fileBean.getLastData()));
        if(fileBean.getDir()){
            holder.mImageView.setImageResource(R.drawable.folder);
        }else{
            holder.mImageView.setImageResource(R.drawable.file);
        }
//        CategoryBean categoryBean = mValues.get(position).getData();
//        Log.i("value",categoryBean.getCategoryName());
//        if(holder.mTextName == null ){
//            Log.e("mTextName", "null");
//            return;
//        }
//        holder.mTextName.setText(categoryBean.getCategoryName());
//        holder.mImageView.setImageURI(categoryBean.getCategoryImg());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
