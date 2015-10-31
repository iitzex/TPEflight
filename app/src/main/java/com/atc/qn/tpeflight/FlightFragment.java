package com.atc.qn.tpeflight;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.os.Handler;

public class FlightFragment extends Fragment
        implements SearchView.OnQueryTextListener
{
    private Activity mContext;
    private static ArrayList<String> sFlightAll = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private FlightAdapter mAdapter;
    private LinearLayoutManager mManager;
    private ProgressBar mLoading;
    private TextView mUpdateTextView;
    private static String sAction, sUpdateTime;
    private static int sPositionDeparture = -1, sPositionArrival = -1, sPosition = -1;
    private FlightAsyncTask mTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        setHasOptionsMenu(true);

        sAction = getArguments().getString("Action", "D");
        if (sAction.equals("D")) {
            sPosition = sPositionDeparture;
        }else {
            sPosition = sPositionArrival;
        }

        View view =  inflater.inflate(R.layout.flight, container, false);
        mLoading = (ProgressBar) view.findViewById(R.id.flight_loading);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.flight_content);
        mUpdateTextView = (TextView) view.findViewById(R.id.flight_updatetime);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        mManager = new LinearLayoutManager(mContext);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new Divider(mContext, Divider.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setAdapter(mAdapter);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (sFlightAll.size() == 0) { //fetch flight information while empty
            fetchFlight();
        } else { //with information
            onFinishView(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sAction.equals("D"))
            mContext.setTitle(mContext.getString(R.string.name_departure));
        else
            mContext.setTitle(mContext.getString(R.string.name_arrival));
    }

    private void fetchFlight() {
        getArguments().putBoolean("Reloaded", false);
        mTask = (FlightAsyncTask) new FlightAsyncTask().execute(null, null, null);

        Calendar timeInst = Calendar.getInstance();
        SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
        sUpdateTime = day.format(timeInst.getTime());
    }

    private void onFinishView(final boolean updated) {
        mLoading.setVisibility(View.INVISIBLE);

        mUpdateTextView.setText("最近更新：" + sUpdateTime);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                mAdapter = new FlightAdapter(sFlightAll, updated, sAction, mContext);

                if (sPosition == -1) {
                    mManager.scrollToPositionWithOffset(mAdapter.getTimePosition(), 0);
                }else {
                    mManager.scrollToPositionWithOffset(sPosition, 0);
                }

                mRecyclerView.setAdapter(mAdapter);
            }
        };

        Handler mHandler = new Handler();
        mHandler.postDelayed(r, 300);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        mContext.getMenuInflater().inflate(R.menu.main_menu, menu);
        setupSearchView(menu);
    }

    private void setupSearchView(Menu menu) {
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(mContext.getComponentName()));

        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        getArguments().putBoolean("Reloaded", true);
        if (id == R.id.action_locate) {
            mManager.scrollToPositionWithOffset(mAdapter.getTimePosition(), 0);
            return true;
        }else if (id == R.id.action_refresh) {
            sPosition = mManager.findFirstVisibleItemPosition();

            fetchFlight();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        mAdapter.getFilter().filter(query);
        return true;
    }

    @Override
    public void onStop() {
        sPosition = mManager.findFirstVisibleItemPosition();
        getArguments().putBoolean("Reloaded", false);

        if (sPosition >= 0) {
            if (sAction.equals("D")) {
                sPositionDeparture = sPosition;
            } else {
                sPositionArrival = sPosition;
            }
        }

        if (mTask != null)
            mTask.cancel(true);

        super.onStop();
    }

    private class FlightAsyncTask extends AsyncTask<Void, Void, Integer> {
        final static String addr = "http://www.taoyuan-airport.com/uploads/flightx/a_flight_v4.txt";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            sFlightAll.clear();
            mLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                downloadData(addr);
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
            return 1;
        }

        private void downloadData (String HttpAddr) throws IOException {
            Calendar timeInst = Calendar.getInstance();
            SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd");
            String dayStr = day.format(timeInst.getTime());

            URL textUrl = new URL(HttpAddr);
            BufferedReader bufferReader
                = new BufferedReader(new InputStreamReader(textUrl.openStream(), "Big5"));

            String StringBuffer;
            while ((StringBuffer = bufferReader.readLine()) != null) {
                String[] info = StringBuffer.split(",");

                if (info[6].trim().equals(dayStr)) { //filtered by day
                    sFlightAll.add(StringBuffer);
                }
            }

            bufferReader.close();
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == 1)
                onFinishView(true);
        }
    }
}

