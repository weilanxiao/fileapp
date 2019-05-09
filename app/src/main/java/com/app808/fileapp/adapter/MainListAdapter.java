package com.app808.fileapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app808.fileapp.R;
import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class MainListAdapter extends BaseAdapter {

    private Context mcontext;

    private LayoutInflater mlayoutInflater;

    private List<FileBean> data;

    private void dataInit(String path){
//        List<FileBean> fileBeans = new ArrayList(15);
//        for(int i = 0; i < 15; i++){
//            FileBean fileBean = new FileBean();
//            fileBean.setName(i + "");
//            fileBeans.add(fileBean);
//        }
//        data = fileBeans;
        data = FileUtils.getFile(path);
    }

    public MainListAdapter(Context context, String path) {
        this.mcontext = context;
        this.mlayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataInit(path);
        System.out.println("读取文件中....");
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mlayoutInflater.inflate(R.layout.item_list_main, null);
        TextView textName = convertView.findViewById(R.id.item_list_main_name);
        TextView textPath = convertView.findViewById(R.id.item_list_main_path);
        FileBean file = data.get(position);
        textName.setText(file.getName());
        textPath.setText(file.getPath());
        return convertView;
    }

    public void reflushList(String path){
        data = FileUtils.getFile(path);
        notifyDataSetChanged();
    }
}
