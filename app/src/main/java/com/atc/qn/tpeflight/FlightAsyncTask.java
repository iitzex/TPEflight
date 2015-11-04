package com.atc.qn.tpeflight;

import android.support.v4.app.Fragment;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FlightAsyncTask extends AsyncTask<Void, Void, Integer> {
    final static private String addr = "http://www.taoyuan-airport.com/uploads/flightx/a_flight_v4.txt";
    static private ArrayList<String> sFlightRaw = new ArrayList<>();
    static private String sUpdateTime = "";
    private Fragment mContext;

    public interface FetchListener {
        void filter();
        void onPreExecute();
        void onPostExecute();
    }

    public FlightAsyncTask(Fragment mContext) {
        this.mContext = mContext;
    }

    public static ArrayList<String> getFlightData(){
        return sFlightRaw;
    }

    public static String getUpdateTime(){
        return sUpdateTime;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        QNLog.d("AyncTask Reloaded data");
        sFlightRaw.clear();

        if (mContext instanceof TrackFragment) {
            ((TrackFragment) mContext).onPreExecute();
        }
        if (mContext instanceof FlightFragment) {
            ((FlightFragment) mContext).onPreExecute();
        }
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

        Calendar timeInst = Calendar.getInstance();
        SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd");
        String dayStr = day.format(timeInst.getTime());

        String StringBuffer;
        while ((StringBuffer = bufferReader.readLine()) != null) {
            String[] info = StringBuffer.split(",");

            if (info[6].trim().equals(dayStr)) { //filtered by day
                sFlightRaw.add(StringBuffer);
            }
        }

        bufferReader.close();
    }

    @Override
    protected void onPostExecute(Integer result) {
        if(result == 1) {
            Calendar timeInst = Calendar.getInstance();
            SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
            sUpdateTime = day.format(timeInst.getTime());

            QNLog.d(sUpdateTime);
            QNLog.d(sFlightRaw.size());

            if (mContext instanceof TrackFragment) {
                ((TrackFragment) mContext).filter();
                ((TrackFragment) mContext).onPostExecute();
            }
            if (mContext instanceof FlightFragment) {
                ((FlightFragment) mContext).filter();
                ((FlightFragment) mContext).onPostExecute();
            }
        }
    }
}
