package com.app808.fileapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app808.fileapp.adapter.LcoalRecyclerViewAdapter;
import com.app808.fileapp.R;
import com.app808.fileapp.callBack.CurrentRecyclerViewListener;
import com.app808.fileapp.utils.FileFilterUtils;

import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class LocalFileFragment extends Fragment implements CurrentRecyclerViewListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = "LocalFile Fragment";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private CurrentRecyclerViewListener mCurrentRecyclerViewListener;
    View mView;
    private int mIsSecond;

    public int getIsSecond() {
        return mIsSecond;
    }

    public void setIsSecond(int isSecond) {
        mIsSecond = isSecond;
    }

    private static final String rootPath = "/storage/emulated/0";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LocalFileFragment() {
        mPath = rootPath;
    }

    /**
     * @param columnCount
     * @return
     */
    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LocalFileFragment newInstance(int columnCount) {
        LocalFileFragment fragment = new LocalFileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        fragment.mPath = rootPath;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    private String mPath;

    public void setRootPath(String path){
        mPath=path;
    }

    private FilenameFilter mFilter;

    public FilenameFilter getFilterUtils() {
        return mFilter;
    }

    public void setFilterUtils(FilenameFilter filter) {
        mFilter = filter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_list_local,container,false);
        Log.i(TAG,"create...");
        if (mView instanceof RecyclerView) {
            Log.i(TAG,"RecyclerView...");
            Context context = mView.getContext();
            RecyclerView recyclerView = (RecyclerView) mView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if(mListener==null)
                Log.i(TAG,"mlistener is null...");
            recyclerView.setAdapter(new LcoalRecyclerViewAdapter(mPath, mListener, mFilter));
        }
        Log.i(TAG,"success...");
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"start fragment...");
        mCurrentRecyclerViewListener.getCurrentRecyclerView();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,"pause fragment...");
        // mCurrentRecyclerViewListener.getCurrentRecyclerView();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"stop fragment...");
    }

    public void setRecyclerViewLinstener(CurrentRecyclerViewListener currentRecyclerViewListener){
        mCurrentRecyclerViewListener = currentRecyclerViewListener;
    }

    @Override
    public void getCurrentRecyclerView() {

    }

    public interface CurrentRecyclerViewListener{
        public void getCurrentRecyclerView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG,"on attach");
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(com.app808.fileapp.dummy.LocalFileDummy item);
    }
}
