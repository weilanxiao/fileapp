package com.app808.fileapp.dummy;

import com.app808.fileapp.entity.CategoryBean;
import com.app808.fileapp.entity.FileBean;
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


    // 返回视图所需的实体属性集合
    public static List<CategoryDummy> loadData(List<CategoryBean> datas){
        List<CategoryDummy> list = new ArrayList<>();
        for(CategoryBean bean:datas){
            list.add(new CategoryDummy(bean));
        }
        return list;
    }
}
