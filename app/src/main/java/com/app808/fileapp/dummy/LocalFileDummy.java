package com.app808.fileapp.dummy;

import com.app808.fileapp.adapter.LocalListAdapter;
import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class LocalFileDummy {

    private FileBean data;
    private boolean isChecked;

    public LocalFileDummy(FileBean data, boolean isChecked) {
        this.data=data;
        this.isChecked = isChecked;
    }

    public FileBean getData() {
        return data;
    }

    public void setData(FileBean data) {
        this.data = data;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    // 返回视图所需的实体属性集合
    public static List<LocalFileDummy> loadData(String path){
        List<FileBean> fileBeans = FileUtils.getFile(path);
        List<LocalFileDummy> list = new ArrayList<>(fileBeans.size());
        for(FileBean fileBean : fileBeans){
            list.add(new LocalFileDummy(fileBean,false));
        }
        return list;
    }
}
