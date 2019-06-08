package com.app808.fileapp.dummy;

import com.app808.fileapp.entity.CategoryBean;
import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.utils.FileFilterUtils;
import com.app808.fileapp.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class CategoryDummy {

    private CategoryBean data;
    public CategoryDummy(CategoryBean bean) {
        setData(bean);
    }

    public CategoryBean getData() {
        return data;
    }

    public void setData(CategoryBean data) {
        this.data = data;
    }

    static FileFilterUtils filterUtils = new FileFilterUtils();

    // 返回视图所需的实体属性集合
    public static List<CategoryDummy> loadData(List<CategoryBean> datas){

//        List<FileBean> mp3s = filterUtils.getMp3("/storage/emulated/0");
        ////        mp3s.stream().forEach(fileBean -> fileBean.getPath());
        List<CategoryDummy> list = new ArrayList<>();
        for(CategoryBean bean:datas){
            list.add(new CategoryDummy(bean));
        }
        return list;
    }
}
