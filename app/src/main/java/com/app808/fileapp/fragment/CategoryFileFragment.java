package com.app808.fileapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app808.fileapp.R;
import com.app808.fileapp.adapter.CategoryRecyclerViewAdapter;
import com.app808.fileapp.adapter.QuickFileRecyclerViewAdapter;
import com.app808.fileapp.callBack.CurrentRecyclerViewListener;
import com.app808.fileapp.entity.CategoryBean;
import com.app808.fileapp.service.QuickService;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryFileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryFileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFileFragment extends Fragment implements CurrentRecyclerViewListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = "CategoryFile Fragment";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int mColumnCount = 3;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private View mView;

    private RecyclerView mCategoryView;
    private RecyclerView mQuickView;

    private static final String rootPath = "/storage/emulated/0";

    public CategoryFileFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFileFragment newInstance(String param1, String param2) {
        CategoryFileFragment fragment = new CategoryFileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private List<CategoryBean> list = new ArrayList<>(6);

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    private List<String> paths = new ArrayList<>();

    private void initCategory(){
        list.add(new CategoryBean("视频",Uri.parse("android.resource://" + mView.getContext().getApplicationContext().getPackageName() + "/" +R.drawable.shipin)));
        list.add(new CategoryBean("音乐",Uri.parse("android.resource://" + mView.getContext().getApplicationContext().getPackageName() + "/" +R.drawable.yinle)));
        list.add(new CategoryBean("文档",Uri.parse("android.resource://" + mView.getContext().getApplicationContext().getPackageName() + "/" +R.drawable.wendang)));
        list.add(new CategoryBean("图片",Uri.parse("android.resource://" + mView.getContext().getApplicationContext().getPackageName() + "/" +R.drawable.tupian)));
        list.add(new CategoryBean("压缩包",Uri.parse("android.resource://" + mView.getContext().getApplicationContext().getPackageName() + "/" +R.drawable.yasuobao)));
        list.add(new CategoryBean("安装包",Uri.parse("android.resource://" + mView.getContext().getApplicationContext().getPackageName() + "/" +R.drawable.anzhuangbao)));
    }


    private CurrentRecyclerViewListener mCurrentRecyclerViewListener;

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"start fragment...");
        mCurrentRecyclerViewListener.getCurrentRecyclerView();
    }

    public interface CurrentRecyclerViewListener{
        public void getCurrentRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG,"create...");
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        mView = view;
        mCategoryView = view.findViewById(R.id.list_category);
        mQuickView = view.findViewById(R.id.list_quick);
        if (mCategoryView instanceof RecyclerView) {
            Log.i(TAG,"Category RecyclerView...");
            Context context = mCategoryView.getContext();
            RecyclerView recyclerView = (RecyclerView) mCategoryView;
            initCategory();
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
            recyclerView.setAdapter(new CategoryRecyclerViewAdapter(list, mListener));
        }

        if (mQuickView instanceof RecyclerView) {
            Log.i(TAG,"Quick RecyclerView...");
            Context context = mQuickView.getContext();
            RecyclerView recyclerView = (RecyclerView) mQuickView;
            // initQuick();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new QuickFileRecyclerViewAdapter(paths, mListener));
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setRecyclerViewLinstener(CurrentRecyclerViewListener currentRecyclerViewListener){
        mCurrentRecyclerViewListener = currentRecyclerViewListener;
    }

    @Override
    public void getCurrentRecyclerView() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
