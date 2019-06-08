package com.app808.fileapp.viewmodel;

import android.support.v7.widget.RecyclerView;

import com.app808.fileapp.adapter.CloudSyncRecyclerViewAdapter;
import com.app808.fileapp.fragment.CloudSyncFragment;

public class CloudSyncViewModel {

    public static final String TAG = "CloudSync ViewModel";

    // 本地文件fragment相关
    private CloudSyncFragment fragment;
    private RecyclerView recyclerView;
    private CloudSyncRecyclerViewAdapter recyclerViewAdapter;

    public CloudSyncViewModel() {
    }

    public CloudSyncFragment getFragment() {
        return fragment;
    }

    public void setFragment(CloudSyncFragment fragment) {
        this.fragment = fragment;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public CloudSyncRecyclerViewAdapter getRecyclerViewAdapter() {
        return recyclerViewAdapter;
    }

    public void setRecyclerViewAdapter(CloudSyncRecyclerViewAdapter recyclerViewAdapter) {
        this.recyclerViewAdapter = recyclerViewAdapter;
    }
}
