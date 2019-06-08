package com.app808.fileapp.viewmodel;

import android.support.v7.widget.RecyclerView;

import com.app808.fileapp.adapter.CategoryRecyclerViewAdapter;
import com.app808.fileapp.adapter.QuickFileRecyclerViewAdapter;
import com.app808.fileapp.fragment.CategoryFileFragment;

public class CategoryFileViewModel {

    public static final String TAG = "CategoryFile ViewModel";

    // 分类Filefragment相关
    private CategoryFileFragment fragment;
    // 分类数据相关
    private RecyclerView categoryRecyclerView;
    private CategoryRecyclerViewAdapter categoryRecyclerViewAdapter;
    // 快捷入口相关
    private RecyclerView quickRecyclerView;
    private QuickFileRecyclerViewAdapter quickRecyclerViewAdapter;

    public CategoryFileFragment getFragment() {
        return fragment;
    }

    public void setFragment(CategoryFileFragment fragment) {
        this.fragment = fragment;
    }

    public RecyclerView getCategoryRecyclerView() {
        return categoryRecyclerView;
    }

    public void setCategoryRecyclerView(RecyclerView categoryRecyclerView) {
        this.categoryRecyclerView = categoryRecyclerView;
    }

    public CategoryRecyclerViewAdapter getCategoryRecyclerViewAdapter() {
        return categoryRecyclerViewAdapter;
    }

    public void setCategoryRecyclerViewAdapter(CategoryRecyclerViewAdapter categoryRecyclerViewAdapter) {
        this.categoryRecyclerViewAdapter = categoryRecyclerViewAdapter;
    }

    public RecyclerView getQuickRecyclerView() {
        return quickRecyclerView;
    }

    public void setQuickRecyclerView(RecyclerView quickRecyclerView) {
        this.quickRecyclerView = quickRecyclerView;
    }

    public QuickFileRecyclerViewAdapter getQuickRecyclerViewAdapter() {
        return quickRecyclerViewAdapter;
    }

    public void setQuickRecyclerViewAdapter(QuickFileRecyclerViewAdapter quickRecyclerViewAdapter) {
        this.quickRecyclerViewAdapter = quickRecyclerViewAdapter;
    }
}
