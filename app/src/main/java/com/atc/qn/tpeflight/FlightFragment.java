package com.atc.qn.tpeflight;

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
import android.widget.SearchView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FlightFragment extends Fragment
        implements SearchView.OnQueryTextListener

{
    static private ArrayList<String> mFlightAll = new ArrayList<>();
    static RecyclerView mRecyclerView;
    static FlightAdapter mAdapter;
    static LinearLayoutManager mManager;
    static String mClass, mUpdateTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.flight, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mClass = getArguments().getString("FlightAction", "D");

        mManager = new LinearLayoutManager(getActivity());
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycleview);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new Divider(getActivity(), Divider.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);

        if (mFlightAll.size() == 0) { //fetch flight infomation while empty
            fetchFlight();
        } else { //with information
            onFinishRecyclerView();
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        String mTitle;
        if (mClass.equals("D"))
            mTitle = "出境航班";
        else
            mTitle = "入境航班";

        getActivity().setTitle(mTitle);
    }

    private void fetchFlight() {
        String addr = "http://www.taoyuan-airport.com/uploads/flightx/a_flight_v4.txt";
        new FlightAsyncTask().execute(addr, null, null);

        Calendar timeInst = Calendar.getInstance();
        SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
        mUpdateTime = day.format(timeInst.getTime());
    }

    private void onFinishRecyclerView() {
        TextView mUpdateTextView = (TextView) getActivity().findViewById(R.id.updatetime);
        mUpdateTextView.setText("最近更新：" + mUpdateTime);

        mAdapter = new FlightAdapter(mFlightAll, mClass, getActivity());
        mManager.scrollToPositionWithOffset(mAdapter.getPosition(), 0);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.main_menu, menu);
        setupSearchView(menu);
    }

    private void setupSearchView(Menu menu) {
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_locate) {
            mManager.scrollToPositionWithOffset(mAdapter.getPosition(), 0);
            return true;
        }else if (id == R.id.action_refresh) {
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
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return true;
    }

    private class FlightAsyncTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mFlightAll.clear();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String addr = params[0];
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
            LogD.out(dayStr);

            URL textUrl = new URL(HttpAddr);
            BufferedReader bufferReader
                = new BufferedReader(new InputStreamReader(textUrl.openStream(), "Big5"));

            String StringBuffer;
            while ((StringBuffer = bufferReader.readLine()) != null) {
                String[] info = StringBuffer.split(",");

                if (info[6].trim().equals(dayStr)) { //filter day
                    mFlightAll.add(StringBuffer);
                }
            }

            bufferReader.close();
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == 1)
                onFinishRecyclerView();
        }
    }
}

