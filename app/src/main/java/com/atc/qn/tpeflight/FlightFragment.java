package com.atc.qn.tpeflight;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcelable;
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
    static private ArrayList<String> mFlightAll = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private FlightAdapter mAdapter;
    private LinearLayoutManager mManager;
    private static String mAction, mUpdateTime;
    private ProgressBar mLoading;
    private static int mFirstVisiblePosition = -1;
    private FlightAsyncTask fetchTasker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if(savedInstanceState != null)
        {
            QNLog.d("--restoring");
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }else
            QNLog.d("--null state");

        return inflater.inflate(R.layout.flight, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        if(savedInstanceState != null)
        {
            QNLog.d("restoring");
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }else
            QNLog.d("null state");

        mManager = new LinearLayoutManager(getActivity());
        mManager.setOrientation(LinearLayoutManager.VERTICAL);

        mLoading = (ProgressBar)getView().findViewById(R.id.flight_loading);
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.flight_content);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new Divider(getActivity(), Divider.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setAdapter(mAdapter);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        QNLog.d("frag start, " + mFirstVisiblePosition);
        if (mFlightAll.size() == 0) { //fetch flight infomation while empty
            fetchFlight();
        } else { //with information
            onFinishView(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mAction = getArguments().getString("FlightAction", "D");
        if (mAction.equals("D"))
            getActivity().setTitle(getActivity().getString(R.string.name_departure));
        else
            getActivity().setTitle(getActivity().getString(R.string.name_arrival));
    }

    private void fetchFlight() {
        fetchTasker = (FlightAsyncTask) new FlightAsyncTask().execute(null, null, null);

        Calendar timeInst = Calendar.getInstance();
        SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
        mUpdateTime = day.format(timeInst.getTime());
    }

    private void onFinishView(final boolean updated) {
        mLoading.setVisibility(View.INVISIBLE);

        TextView mUpdateTextView = (TextView) getActivity().findViewById(R.id.flight_updatetime);
        mUpdateTextView.setText("最近更新：" + mUpdateTime);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                mAdapter = new FlightAdapter(mFlightAll, updated, mAction, getActivity());

                // TODO: 2015/10/17
                // fix the restore problem
                boolean mReloaded = getArguments().getBoolean("Reloaded", false);
                if (mReloaded) {
                    mManager.scrollToPositionWithOffset(mAdapter.getTimePosition(), 0);
                }else
                    mManager.scrollToPositionWithOffset(mFirstVisiblePosition, 0);

                mRecyclerView.setAdapter(mAdapter);

            }
        };

        Handler mHandler = new Handler();
        mHandler.postDelayed(r, 300);
    }

    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        QNLog.d("restore layout");
        if(savedInstanceState != null)
        {
            QNLog.d("restoring");
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        QNLog.d("saving instance");
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());
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

        getArguments().putBoolean("Reloaded", true);
        if (id == R.id.action_locate) {
            mManager.scrollToPositionWithOffset(mAdapter.getTimePosition(), 0);
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

    @Override
    public void onStop() {
        mFirstVisiblePosition = mManager.findFirstVisibleItemPosition();
        getArguments().putBoolean("Reloaded", false);

        if (fetchTasker != null)
            fetchTasker.cancel(true);

        super.onStop();
    }

    private class FlightAsyncTask extends AsyncTask<Void, Void, Integer> {
        String addr = "http://www.taoyuan-airport.com/uploads/flightx/a_flight_v4.txt";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mFlightAll.clear();
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
                    mFlightAll.add(StringBuffer);
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

