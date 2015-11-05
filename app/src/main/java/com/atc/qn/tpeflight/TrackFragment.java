package com.atc.qn.tpeflight;

import android.app.Activity;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;

public class TrackFragment extends Fragment
    implements FlightAsyncTask.FetchListener{
    private Activity mContext;
    private RecyclerView mRecyclerView;
    private FlightAdapter mAdapter;
    private ProgressBar mLoading;
    private ArrayList<Flight> mTrackList;
    private FlightAsyncTask fetchTasker;
    private TextView mUpdateTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContext.setTitle(mContext.getString(R.string.name_track));
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.flight, container, false);
        mLoading = (ProgressBar) view.findViewById(R.id.flight_loading);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.flight_content);
        mUpdateTextView = (TextView) view.findViewById(R.id.flight_updatetime);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mTrackList = getArguments().getParcelableArrayList("TRACKING");

        LinearLayoutManager mManager = new LinearLayoutManager(mContext);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new Divider(mContext, Divider.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);

        if (mTrackList.size() != 0)
            fetchTasker = (FlightAsyncTask) new FlightAsyncTask(this).execute(null, null, null);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStop() {
        if (fetchTasker != null)
            fetchTasker.cancel(true);

        super.onStop();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (!((FlightInterface)mContext).isDrawerOpen())
            mContext.getMenuInflater().inflate(R.menu.track_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.track_refresh) {
            fetchTasker = (FlightAsyncTask) new FlightAsyncTask(this).execute(null, null, null);
            return true;
        }else if (id == R.id.track_deleteall) {
            mTrackList.clear();
            mAdapter = new FlightAdapter(mTrackList, "TRACKING", mContext);
            mRecyclerView.setAdapter(mAdapter);

            String countMsg = "(" + mTrackList.size() + ")";
            mContext.setTitle(mContext.getString(R.string.name_track) + countMsg);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void filter() {
        ArrayList<String> flightRaw = FlightAsyncTask.getFlightData();

        for (String flight : flightRaw) {
            String[] info = flight.split(",");

            for (Flight item : mTrackList) {
                if (item.getFlightNO().equals(info[2].trim() + info[4].trim())
                        && item.getAction().equals(info[1].trim())) {
                    item.setTerminal("第" + info[0].trim() + "航廈");
                    item.setGate(info[5].trim());
                    item.setExpectDay(info[6].trim());
                    item.setExpectTime(info[7].trim().substring(0, 5));
                    item.setActualDay(info[8].trim());
                    item.setActualTime(info[9].trim().substring(0, 5));
                    item.setDestination(info[11].trim());
                    item.setDestinationTW(info[12].trim());
                    item.setStatus(info[13].trim());
                    item.setBaggage(info[18].trim());
                    item.setCounter(info[19].trim());
                }
            }
        }
    }

    @Override
    public void onPreExecute() {
        mLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute() {
        mUpdateTextView.setText("最近更新：" + FlightAsyncTask.getUpdateTime());
        mLoading.setVisibility(View.INVISIBLE);

        mAdapter = new FlightAdapter(mTrackList, "TRACKING", mContext);
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemCallback(mAdapter));
        touchHelper.attachToRecyclerView(mRecyclerView);
    }
}
