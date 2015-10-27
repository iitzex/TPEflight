package com.atc.qn.tpeflight;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class AlarmFragment extends Fragment {
    private Activity mContext;
    private RecyclerView mRecyclerView;
    private ArrayList<Flight> mAlarmList;
    private AlarmAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mContext = getActivity();
        mContext.setTitle(mContext.getString(R.string.name_alarm));
        setHasOptionsMenu(true);

        View view =  inflater.inflate(R.layout.alarm, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.alarm_content);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAlarmList = getArguments().getParcelableArrayList("ALARMLIST");

        LinearLayoutManager mManager = new LinearLayoutManager(mContext);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new Divider(mContext, Divider.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                mAdapter = new AlarmAdapter(mAlarmList, mContext);
                mRecyclerView.setAdapter(mAdapter);
                ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemCallback(mAdapter));
                touchHelper.attachToRecyclerView(mRecyclerView);
            }
        };

        Handler mHandler = new Handler();
        mHandler.postDelayed(r, 300);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        mContext.getMenuInflater().inflate(R.menu.alarm_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.alarm_deleteall) {
            mAlarmList.clear();
            mAdapter = new AlarmAdapter(mAlarmList, mContext);
            mRecyclerView.setAdapter(mAdapter);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
