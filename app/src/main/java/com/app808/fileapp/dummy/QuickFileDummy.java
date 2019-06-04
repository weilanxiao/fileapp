package com.app808.fileapp.dummy;

import android.util.Log;

import com.app808.fileapp.entity.CategoryBean;
import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class QuickFileDummy {

    private FileBean data;
    public QuickFileDummy(FileBean bean) {
        setData(bean);
    }

    public FileBean getData() {
        return data;
    }

    public void setData(FileBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "QuickFileDummy{" + "data name=" + data.getName() + " data path="+ data.getPath() +'}';
    }

    // 返回选择的快速入口集合
    public static List<QuickFileDummy> loadData(List<String> paths){
        List<QuickFileDummy> list = new ArrayList<>();
        List<FileBean> datas = new ArrayList<>(paths.size());
        for (String path:paths){
            datas.add(FileUtils.setQuick(path));
        }
        for(FileBean bean:datas){
            list.add(new QuickFileDummy(bean));
        }
        list.stream().forEach(q->Log.i("data",q.toString()));
        return list;
    }
}
