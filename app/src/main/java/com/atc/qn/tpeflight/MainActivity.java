package com.atc.qn.tpeflight;
import com.atc.qn.tpeflight.DrawerAdapter.DrawerInterface;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity
        implements DrawerInterface, FlightInterface
{
    private FragmentManager mFragMgr = getSupportFragmentManager();
    private DrawerFragment mDrawerFragment;
    private ArrayList<Flight> mTrackList = new ArrayList<>();
    private ArrayList<Flight> mAlarmList = new ArrayList<>();
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mTrackList = savedInstanceState.getParcelableArrayList("TRACKING");
//            //Restore the fragment's instance
//            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }

        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mDrawerFragment = (DrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("TRACKING", mTrackList);
    }

    @Override
    public void onDrawerItemSelected(int position) {
        //clear stack
        for(int i = 0; i < mFragMgr.getBackStackEntryCount(); ++i) {
            mFragMgr.popBackStack();
        }

        if (position == 0) {
            FlightFragment flightFrag = new FlightFragment();
            Bundle args = new Bundle();
            args.putString("Action", "D");
            flightFrag.setArguments(args);

            mFragMgr.beginTransaction()
                    .replace(R.id.container, flightFrag)
                    .commit();
        }else if (position == 1) {
            FlightFragment flightFrag = new FlightFragment();
            Bundle args = new Bundle();
            args.putString("Action", "A");
            flightFrag.setArguments(args);

            mFragMgr.beginTransaction()
                    .replace(R.id.container, flightFrag)
                    .commit();
        }else if (position == 2) {
            TrackFragment trackFragment = new TrackFragment();
            Bundle args = new Bundle();
            args.putParcelableArrayList("TRACKING", mTrackList);
            trackFragment.setArguments(args);

            mFragMgr.beginTransaction()
                    .replace(R.id.container, trackFragment)
                    .commit();
        }else if (position == 3) {
            AlarmFragment alarmFrag = new AlarmFragment();
            Bundle args = new Bundle();
            args.putParcelableArrayList("ALARMLIST", mAlarmList);
            alarmFrag.setArguments(args);

            mFragMgr.beginTransaction()
                    .replace(R.id.container, alarmFrag)
                    .commit();
        }else if (position == 4) {
            WxFragment wxFrag = new WxFragment();
            mFragMgr.beginTransaction()
                    .replace(R.id.container, wxFrag)
                    .commit();
        }else if (position == 5) {
            AirlinesFragment airlinesFragment = new AirlinesFragment();
            mFragMgr.beginTransaction()
                .replace(R.id.container, airlinesFragment)
                .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        Gson gson = new Gson();
        mPrefs = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        String mTrackJson = mPrefs.getString("TRACKING", null);

        Type listType = new TypeToken<ArrayList<Flight>>() {}.getType();
        mTrackList = gson.fromJson(mTrackJson, listType);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor Pref = mPrefs.edit();
        Gson data = new Gson();
        String mTrackJson = data.toJson(mTrackList);
        Pref.putString("TRACKING", mTrackJson);
        Pref.apply();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerFragment.isDrawerOpen())
            mDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mDrawerFragment.isDrawerOpen()) {
////            getMenuInflater().inflate(R.menu.main_menu, menu);
//            return true;
//        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFlightItemClick(Flight mInfo) {
        FragmentManager fragMgr = getSupportFragmentManager();

        InfoFragment infoFrag = new InfoFragment();
        Bundle infoArgs = new Bundle();
        infoArgs.putParcelable("Flight", mInfo);
        infoFrag.setArguments(infoArgs);

        fragMgr.beginTransaction()
                .replace(R.id.container, infoFrag)
                .addToBackStack("infoFrag")
                .commit();
    }

    @Override
    public void onAlarmClick(Flight mInfo, boolean[] timeTable) {
        String mActualTime = mInfo.getActualDay() + ", " + mInfo.getActualTime();

        for (int i = 0; i < 6; i++) {
            Calendar mCal = Calendar.getInstance();
            SimpleDateFormat mDayFromat = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
            try {
                mCal.setTime(mDayFromat.parse(mActualTime));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (timeTable[i]) {
                Flight target = new Flight(mInfo);
                int timeDelta = 0;
                if (i == 1)
                    timeDelta = -15;
                if (i == 2)
                    timeDelta = -30;
                if (i == 3)
                    timeDelta = -60;
                if (i == 4)
                    timeDelta = -120;
                if (i == 5)
                    timeDelta = -240;

                mCal.add(Calendar.MINUTE, timeDelta);
                target.setAlarm(mCal);
                mAlarmList.add(target);
            }
        }

        Calendar timeInst = Calendar.getInstance();
        timeInst.add(Calendar.SECOND, 10);
        QNLog.d(timeInst.toString());

//        setAlarm(timeInst);
    }

    public void addAlarm(Calendar mCal){
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(2015, 9, 21, 10, 18);
//        SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
//        QNLog.d(day.format(calendar.getTime()));

        AlarmManager mAlarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("GETNAME", "TPEflight");

        PendingIntent pending = PendingIntent.getBroadcast(this, 0, intent, 0);
        mAlarmMgr.set(AlarmManager.RTC_WAKEUP, mCal.getTimeInMillis(), pending);
    }

    @Override
    public void onStarClick(Flight mInfo) {
        for (Flight item : mTrackList){
            if (item.getFlightNO().equals(mInfo.getFlightNO()) &&
                    item.getAction().equals(mInfo.getAction())) {
                return;
            }
        }

        mTrackList.add(mInfo);
    }

    public int getTrackListSize(){
        return mTrackList.size();
    }

    public int getAlarmListSize(){
        return mAlarmList.size();
    }

    public void removeTrackList(int position) {
        mTrackList.remove(position);
    }
}

