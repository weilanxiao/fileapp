package com.app808.fileapp.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.app808.fileapp.R;
import com.app808.fileapp.dummy.CloudDummy;
import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.fragment.CloudSyncFragment.OnListFragmentInteractionListener;
import com.app808.fileapp.utils.FileSyncUtils;
import com.app808.fileapp.utils.JsonToBean;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link CloudDummy} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CloudSyncRecyclerViewAdapter extends RecyclerView.Adapter<CloudSyncRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "CloudSync Recycler ViewAdapter";
    private final List<CloudDummy> mValues;
    private final OnListFragmentInteractionListener mListener;

    private static final int NAME_LENGTH = 20;

    private boolean mIsMulited;
    private boolean mAllChecked;
    private static final String baseUrl = "https://r.ame.gs/cgi-bin/sync?";


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mTextName;
        public final TextView mTextLastDate;
        public final CheckBox mCheckBox;
        public CloudDummy mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.list_item_image_folder_cloud);
            mTextName =  (TextView) view.findViewById(R.id.list_item_txt_file_name_cloud);
            mTextLastDate = (TextView) view.findViewById(R.id.list_item_txt_file_lastdate_cloud);
            mCheckBox = (CheckBox) view.findViewById(R.id.list_item_ckeckBox_file_check_cloud);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextName.getText() + "'";
        }
    }

    public CloudSyncRecyclerViewAdapter(List<CloudDummy> items, OnListFragmentInteractionListener listener) {
        if(items ==null ){
            loadASYNC(baseUrl);
            mValues = new ArrayList<>();
        }else{
            mValues = items;
        }
        mListener = listener;
    }

    private void loadASYNC(String url){
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
                    update(JsonToBean.jsonToFileBean((String) msg.obj));
                } else
                {
                    Log.i("sync", "不能读取到网络信息!");
                }
            }

        };
        FileSyncUtils.getSync(url, mHandler);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_list_item_cloud, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // 绑定页面数据
        FileBean fileBean = mValues.get(position).getData();
        Log.i(TAG,fileBean.getName());
        holder.mTextName.setText(displayFileName(fileBean.getName()));
        holder.mTextLastDate.setText(String.valueOf(fileBean.getLastData()));
        if(fileBean.getDir()){
            holder.mImageView.setImageResource(R.drawable.folder);
        }else{
            holder.mImageView.setImageResource(R.drawable.file);
        }

        //多选框
        if(mIsMulited){
            holder.mCheckBox.setVisibility(View.VISIBLE);
            if(mAllChecked){
                holder.mCheckBox.setChecked(true);
                mValues.get(position).setChecked(false);
            }else{
                holder.mCheckBox.setChecked(mValues.get(position).isChecked());
            }
        }else{
            if(!mAllChecked){
                holder.mCheckBox.setChecked(false);
                mValues.get(position).setChecked(false);
            }
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        }

        holder.mView.setOnClickListener(v->onItemClick(v,holder));
    }

    // 点击事件
    private void onItemClick(View view, final ViewHolder holder){
        Log.i("...item点击事件...","不是文件夹...");
        if (null != mListener) {
            mListener.onListFragmentInteraction(holder.mItem);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private String displayFileName(String name){
        if(name.length() > NAME_LENGTH){
            return name.substring(0,NAME_LENGTH) + "...";
        }else{
            return name;
        }
    }

    public void update(){
        // 取消多选状态
        mIsMulited = false;
        // 取消选择
        mAllChecked = false;
        // mValues.clear();
        // mValues.addAll(loadPath(pathStack.peekLast()));
        notifyDataSetChanged();
    }

    // 更新数据
    public void update(List<FileBean> fileBeans){
        mValues.clear();
        mValues.addAll(CloudDummy.update(fileBeans));
        notifyDataSetChanged();
    }

}
