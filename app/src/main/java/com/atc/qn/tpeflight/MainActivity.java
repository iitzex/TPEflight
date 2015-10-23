package com.atc.qn.tpeflight;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements DrawerCallback, FlightInterface
{
    private DrawerFragment mDrawerFragment;
    FragmentManager fragMgr = getSupportFragmentManager();
    AlarmManager alarmMgr;
    AirlinesFragment infoFrag = new AirlinesFragment();
    ArrayList<Flight> mTracking = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        alarmSetting();
    }

    public void alarmSetting(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 9, 21, 10, 18);
        SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
        QNLog.d(day.format(calendar.getTime()));
        QNLog.d(calendar.getTimeZone().toString());

        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("name", "TPEflight");

        PendingIntent pending = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        //Save the fragment's instance
//        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
//    }

    @Override
    public void onDrawerItemSelected(int position) {
        // update the main_menu content by replacing fragments
        FlightFragment flightFrag = new FlightFragment();

        Bundle args = new Bundle();
        args.putBoolean("Reloaded", true);

        //clear stack
        for(int i = 0; i < fragMgr.getBackStackEntryCount(); ++i) {
            fragMgr.popBackStack();
        }

        if (position == 0) {
            args.putString("FlightAction", "D");
            flightFrag.setArguments(args);

            fragMgr.beginTransaction()
                    .replace(R.id.container, flightFrag)
                    .commit();

        }else if (position == 1) {
            args.putString("FlightAction", "A");
            flightFrag.setArguments(args);

            fragMgr.beginTransaction()
                    .replace(R.id.container, flightFrag)
                    .commit();
        }else if (position == 2) {
            WxFragment wxFrag = new WxFragment();
            fragMgr.beginTransaction()
                    .replace(R.id.container, wxFrag)
                    .commit();

        }else if (position == 3) {
            WxFragment wxFrag = new WxFragment();
            fragMgr.beginTransaction()
                    .replace(R.id.container, wxFrag)
                    .commit();
        }else if (position == 4) {
            fragMgr.beginTransaction()
                .replace(R.id.container, infoFrag)
                .commit();
        }
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
    public void onFlightItemClick(Flight info) {
        FragmentManager fragMgr = getSupportFragmentManager();

        InfoFragment infoFrag = new InfoFragment();
        Bundle infoArgs = new Bundle();
        infoArgs.putString("AirlinesTW", info.getAirlinesTW());
        infoArgs.putString("Airlines", info.getAirlines());
        infoArgs.putString("NO", info.getFlightNO());
        infoArgs.putString("Action", info.getAction());
        infoArgs.putString("ExpectDay", info.getExpectDay());
        infoArgs.putString("ExpectTime", info.getExpectTime());
        infoArgs.putString("ActualDay", info.getActualDay());
        infoArgs.putString("ActualTime", info.getActualTime());
        infoArgs.putString("Terminal", info.getTerminal());
        infoArgs.putString("Counter", info.getCounter());
        infoArgs.putString("Baggage", info.getBaggage());
        infoArgs.putString("Gate", info.getGate());
        infoArgs.putString("Status", info.getStatus());
        infoArgs.putString("Destination", info.getDestination());
        infoArgs.putString("DestinationTW", info.getDestinationTW());
        infoArgs.putString("Type", info.getType());

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
}

