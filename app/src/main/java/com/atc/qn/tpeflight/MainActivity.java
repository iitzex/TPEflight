package com.atc.qn.tpeflight;

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

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity
        implements DrawerCallback, FlightInterface
{
    private FragmentManager fragMgr = getSupportFragmentManager();
    private DrawerFragment mDrawerFragment;
    private ArrayList<Flight> mTrackList = new ArrayList<>();
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mTrackList = savedInstanceState.getParcelableArrayList("TRACKING");
            QNLog.d(mTrackList.toString());
        }

        setContentView(R.layout.activity_main);
//        if (savedInstanceState != null) {
//            //Restore the fragment's instance
//            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
//        }else
//            QNLog.d("000null state");
//
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mDrawerFragment = (DrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

//        alarmSetting();
    }

    public void alarmSetting(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 9, 21, 10, 18);
        SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
        QNLog.d(day.format(calendar.getTime()));
        QNLog.d(calendar.getTimeZone().toString());

        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("name", "TPEflight");

        PendingIntent pending = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("TRACKING", mTrackList);
    }

    @Override
    public void onDrawerItemSelected(int position) {
        //clear stack
        for(int i = 0; i < fragMgr.getBackStackEntryCount(); ++i) {
            fragMgr.popBackStack();
        }

        if (position == 0) {
            FlightFragment flightFrag = new FlightFragment();
            Bundle args = new Bundle();
            args.putString("Action", "D");
            flightFrag.setArguments(args);

            fragMgr.beginTransaction()
                    .replace(R.id.container, flightFrag)
                    .commit();
        }else if (position == 1) {
            FlightFragment flightFrag = new FlightFragment();
            Bundle args = new Bundle();
            args.putString("Action", "A");
            flightFrag.setArguments(args);

            fragMgr.beginTransaction()
                    .replace(R.id.container, flightFrag)
                    .commit();
        }else if (position == 2) {
            TrackFragment trackFragment = new TrackFragment();
            Bundle args = new Bundle();
            args.putParcelableArrayList("TRACKING", mTrackList);
            trackFragment.setArguments(args);

            fragMgr.beginTransaction()
                    .replace(R.id.container, trackFragment)
                    .commit();
        }else if (position == 3) {
            WxFragment wxFrag = new WxFragment();
            fragMgr.beginTransaction()
                    .replace(R.id.container, wxFrag)
                    .commit();
        }else if (position == 4) {
            AirlinesFragment airlinesFragment = new AirlinesFragment();
            fragMgr.beginTransaction()
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
    protected void onStop() {
        super.onStop();

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
////            setupSearchView(menu);
//            return true;
//        }

        return super.onCreateOptionsMenu(menu);
    }

//    private void setupSearchView(Menu menu) {
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//
//        searchView.setOnQueryTextListener(flightFrag);
//    }

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
    public void onAlarmClick() {
        QNLog.d("alarm click");
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

    public void removeTracklist(int position) {
        mTrackList.remove(position);
    }
}

