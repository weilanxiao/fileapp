package com.app808.fileapp.entity;

import android.net.Uri;

public class CategoryBean {
    private String categoryName;
    private Uri categoryImg;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Uri getCategoryImg() {
        return categoryImg;
    }

    public void setCategoryImg(Uri categoryImg) {
        this.categoryImg = categoryImg;
    }

    public CategoryBean(String categoryName, Uri categoryImg) {
        this.categoryName = categoryName;
        this.categoryImg = categoryImg;

    }
}
