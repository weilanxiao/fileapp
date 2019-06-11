package com.app808.fileapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app808.fileapp.R;
import com.app808.fileapp.adapter.CloudSyncRecyclerViewAdapter;
import com.app808.fileapp.dummy.CloudDummy;
import com.app808.fileapp.utils.FileSyncUtils;
import com.app808.fileapp.utils.JsonToBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CloudSyncFragment extends Fragment {

    private static final String TAG = "CloudSync Fragment";

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private View mView;
    private CurrentRecyclerViewListener mCurrentRecyclerViewListener;

    public interface CurrentRecyclerViewListener{
        public void getCurrentRecyclerView();
    }

    public void setRecyclerViewLinstener(CurrentRecyclerViewListener currentRecyclerViewListener){
        mCurrentRecyclerViewListener = currentRecyclerViewListener;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CloudSyncFragment() {
    }


    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CloudSyncFragment newInstance(int columnCount) {
        CloudSyncFragment fragment = new CloudSyncFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // View view = inflater.inflate(R.layout.fragment_list_cloud, container, false);
        mView = inflater.inflate(R.layout.fragment_list_cloud, container, false);
        Log.i(TAG,"create...");
        // Set the adapter
        if (mView instanceof RecyclerView) {
            Log.i(TAG,"RecyclerView...");
            Context context = mView.getContext();
            RecyclerView recyclerView = (RecyclerView) mView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if(mListener == null){
                Log.i(TAG,"mListener is null...");
            }
            recyclerView.setAdapter(new CloudSyncRecyclerViewAdapter(null, mListener));
        }
        Log.i(TAG,"success...");
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"start...");
        mCurrentRecyclerViewListener.getCurrentRecyclerView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        void onListFragmentInteraction(String msg);
    }
}
