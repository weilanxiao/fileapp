package com.app808.fileapp.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.app808.fileapp.adapter.LcoalRecyclerViewAdapter;
import com.app808.fileapp.fragment.LocalFileFragment;

public class LocalFileViewModel {

    public static final String TAG = "LocalFile ViewModel";

    // 本地文件fragment相关
    private LocalFileFragment fragment;
    private RecyclerView recyclerView;
    private LcoalRecyclerViewAdapter recyclerViewAdapter;

    public LocalFileViewModel(){
    }

    public LocalFileFragment getFragment() {
        return fragment;
    }

    public void setFragment(LocalFileFragment fragment) {
        this.fragment = fragment;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public LcoalRecyclerViewAdapter getRecyclerViewAdapter() {
        return recyclerViewAdapter;
    }

    public void setRecyclerViewAdapter(LcoalRecyclerViewAdapter recyclerViewAdapter) {
        this.recyclerViewAdapter = recyclerViewAdapter;
    }
}
