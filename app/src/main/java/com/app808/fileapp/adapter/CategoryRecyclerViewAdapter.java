package com.app808.fileapp.adapter;

import android.content.Context;
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
import com.app808.fileapp.dummy.LocalFileDummy;
import com.app808.fileapp.entity.CategoryBean;
import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.fragment.CategoryFileFragment;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder>{


    private final List<CategoryDummy> mValues;
    private final CategoryFileFragment.OnFragmentInteractionListener mListener;

    private static final int NAME_LENGTH = 20;
    private BottomSheetDialog mBottomDialog;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private TextView mTextName;
        private ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTextName = (TextView) mView.findViewById(R.id.grid_item_img_file_name);
            mImageView = (ImageView) mView.findViewById(R.id.grid_item_img_file_icon);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextName.getText() + "'";
        }
    }

    public CategoryRecyclerViewAdapter(List<CategoryBean> data,CategoryFileFragment.OnFragmentInteractionListener listener){
        mValues = CategoryDummy.loadData(data);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_grid_item_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoryRecyclerViewAdapter.ViewHolder holder, final int position) {
        // 绑定页面数据
        CategoryBean categoryBean = mValues.get(position).getData();
        Log.i("value",categoryBean.getCategoryName());
        if(holder.mTextName == null ){
            Log.e("mTextName", "null");
            return;
        }
        holder.mTextName.setText(categoryBean.getCategoryName());
        holder.mImageView.setImageURI(categoryBean.getCategoryImg());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

}
