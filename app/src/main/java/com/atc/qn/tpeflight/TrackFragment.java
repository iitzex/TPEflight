package com.atc.qn.tpeflight;

import android.app.Activity;
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TrackFragment extends Fragment {
    private Activity mContext;
    private RecyclerView mRecyclerView;
    private FlightAdapter mAdapter;
    private LinearLayoutManager mManager;
    private ProgressBar mLoading;
    private ArrayList<Flight> mTrackList;
    private FlightAsyncTask fetchTasker;
    private static String mUpdateTime;
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

        mManager = new LinearLayoutManager(mContext);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new Divider(mContext, Divider.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);

        mAdapter = new FlightAdapter(mTrackList, "TRACKING", mContext);

        QNLog.d(mTrackList.toString());
        mRecyclerView.setAdapter(mAdapter);

        if (mTrackList.size() != 0)
            fetchTasker = (FlightAsyncTask) new FlightAsyncTask().execute(null, null, null);

        super.onActivityCreated(savedInstanceState);
    }

    private void onFinishView() {
        Calendar timeInst = Calendar.getInstance();
        SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
        mUpdateTime = day.format(timeInst.getTime());
        mUpdateTextView.setText("最近更新：" + mUpdateTime);
        mLoading.setVisibility(View.INVISIBLE);

        mAdapter = new FlightAdapter(mTrackList, "TRACKING", mContext);
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemCallback(mAdapter));
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onStop() {
        if (fetchTasker != null)
            fetchTasker.cancel(true);
        super.onStop();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        mContext.getMenuInflater().inflate(R.menu.track_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.track_refresh) {

            fetchTasker = (FlightAsyncTask) new FlightAsyncTask().execute(null, null, null);

            return true;
        }else if (id == R.id.track_deleteall) {
            mTrackList.clear();

            mAdapter = new FlightAdapter(mTrackList, "TRACKING", mContext);
            mRecyclerView.setAdapter(mAdapter);
        }

        return super.onOptionsItemSelected(item);
    }

    private class FlightAsyncTask extends AsyncTask<Void, Void, Integer> {
        final static String addr = "http://www.taoyuan-airport.com/uploads/flightx/a_flight_v4.txt";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            URL textUrl = new URL(HttpAddr);
            BufferedReader bufferReader
                    = new BufferedReader(new InputStreamReader(textUrl.openStream(), "Big5"));

            String StringBuffer;
            while ((StringBuffer = bufferReader.readLine()) != null) {
                String[] info = StringBuffer.split(",");

                filter(info);
            }

            bufferReader.close();
        }

        private void filter(String[] info){
            Calendar timeInst = Calendar.getInstance();
            SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd");
            String dayStr = day.format(timeInst.getTime());

            if (!info[6].trim().equals(dayStr)) { //filtered by day
                return;
            }

            for (Flight item : mTrackList){
                if (item.getFlightNO().equals(info[2].trim() + info[4].trim())
                        && item.getAction().equals(info[1].trim())){
                    item.setTerminal("第" + info[0].trim() + "航廈");
                    item.setGate(info[5].trim());
                    item.setExpectDay(info[6].trim());
                    item.setExpectTime(info[7].trim().substring(0, 5));
                    item.setActualDay(info[8].trim());
                    item.setActualTime(info[9].trim().substring(0, 5));
                    item.setDestination(info[11].trim());
                    item.setDestinationTW(info[12].trim());
                    item.setStatus(info[13].trim());
                    item.setType(info[14].trim());
                    item.setBaggage(info[18].trim());
                    item.setCounter(info[19].trim());
                }
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == 1)
                onFinishView();
        }
    }
}
