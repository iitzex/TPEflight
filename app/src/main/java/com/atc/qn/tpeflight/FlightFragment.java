package com.atc.qn.tpeflight;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class FlightFragment extends Fragment
{
    static private ArrayList<String> mFlightAll = new ArrayList<>();
    RecyclerView mRecyclerView;
    FlightAdapter mAdapter;
    LinearLayoutManager mManager;
    String mAction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.flightlayout, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAction = getArguments().getString("FlightAction", "D");
//        LogD.out(mAction);

        mManager = new LinearLayoutManager(getActivity());
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycleview);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);

        if (mFlightAll.size() == 0) { //fetch flight infomation while empty
            String addr = "http://www.taoyuan-airport.com/uploads/flightx/a_flight_v4.txt";
            new FlightAsyncTask().execute(addr, null, null);
        } else { //with information
            onFinishRecyclerView();
        }

        setHasOptionsMenu(true);
    }

    private void onFinishRecyclerView() {
        mAdapter = new FlightAdapter(mFlightAll, mAction);
        mManager.scrollToPositionWithOffset(mAdapter.getPosition(), 0);
            mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem locateItem = menu.findItem(R.id.action_locate);
        if (locateItem != null) {
            locateItem.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_locate) {
            LogD.out("locating");
            mManager.scrollToPositionWithOffset(mAdapter.getPosition(), 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class FlightAsyncTask extends AsyncTask<String, Integer, Integer> {
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
            URL textUrl = new URL(HttpAddr);
            BufferedReader bufferReader
                = new BufferedReader(new InputStreamReader(textUrl.openStream(), "Big5"));

            String StringBuffer;
            while ((StringBuffer = bufferReader.readLine()) != null) {
//                Calendar timeInst = Calendar.getInstance();
//                SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd");
//                String dayStr = day.format(timeInst.getTime());

//                if (StringBuffer.contains(dayStr)) {
                    mFlightAll.add(StringBuffer);
//                }
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

