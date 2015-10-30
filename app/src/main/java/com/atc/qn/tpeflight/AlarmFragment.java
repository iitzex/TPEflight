package com.atc.qn.tpeflight;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;

import java.util.ArrayList;

public class AlarmFragment extends Fragment {
    private Activity mContext;
    private RecyclerView mRecyclerView;
    private ArrayList<Flight> mAlarmList;
    private AlarmAdapter mAdapter;
    private SharedPreferences mPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mContext = getActivity();
        mContext.setTitle(mContext.getString(R.string.name_alarm));
        mPrefs = mContext.getSharedPreferences("PREFERENCES", Activity.MODE_PRIVATE);

        View view = inflater.inflate(R.layout.alarm, container, false);
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
            }
        };

        mAdapter = new AlarmAdapter(mAlarmList, mContext, this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemCallback(mAdapter));
        touchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        Handler mHandler = new Handler();
        mHandler.postDelayed(r, 300);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((FlightInterface)mContext).getAlarmListSize();
    }

    @Override
    public void onPause() {
        saveList();
        super.onPause();

    }

    private void saveList() {
        SharedPreferences.Editor Pref = mPrefs.edit();

        Gson mAlarmGson = new Gson();
        String mAlarmJson = mAlarmGson.toJson(mAlarmList);
        Pref.putString("ALARMLIST", mAlarmJson);

        Pref.apply();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        mContext.getMenuInflater().inflate(R.menu.alarm_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.alarm_deleteall) {
            removeAlarmListAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeAlarmListAll() {
        int size = mAlarmList.size();
        for (int i = 0; i < size; i++){
            cancelAlarm(i);
        }
        mAlarmList.clear();

        mAdapter = new AlarmAdapter(mAlarmList, mContext, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void removeAlarmList(int position) {
        cancelAlarm(position);
        mAlarmList.remove(position);
    }

    public void cancelAlarm(int position) {
        Flight target = mAlarmList.get(position);
        String alarmMsg = target.getAirlinesTW() + " " + target.getFlightNO() + ", " + target.getAlarmTag();

        Intent intent = new Intent(mContext, AlarmReceiver.class);
//        intent.putExtra("ALARMMSG", alarmMsg);
        intent.setAction(alarmMsg);
        AlarmManager mAlarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pending = PendingIntent.getBroadcast(mContext, target.getKey(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmMgr.cancel(pending);
    }

}