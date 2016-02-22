package com.atc.qn.tpeflight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InfoFragment extends Fragment {
    private Activity mContext;
    private ArrayList<Flight> mAlarmList;
    private Flight mInfo;
    private View mView;
    private LayoutInflater mInflater;
    private ImageView mStatusLogo;
    private ImageView mAirlinesLogo;    
    private TextView mAirlinesTW, mNO, mTerminal;
    private TextView mCounter, mCounterTxt;
    private TextView mGate, mStatus;
    private TextView mExpectTime, mActualTime;
    private TextView mDestinationTxt, mDestinationTW, mWXAirport;

    private ImageView mInfoWX_img0;
    private TextView mInfoWX_text0;

    private Button mPhone;
    private ProgressBar mLoading;

    private TypedArray arrayLogo;
    private String [] arrayIATA;
    private String [] arrayPhone;

    private String mAirport;
    private String mJSONdata = "";
    private String mWXAddr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mContext = getActivity();
        mContext.setTitle(mContext.getString(R.string.name_info));

        View view = inflater.inflate(R.layout.info, container, false);
        mInflater = inflater;
        mStatusLogo = (ImageView) view.findViewById(R.id.infoStatusLogo);
        mAirlinesLogo = (ImageView) view.findViewById(R.id.infoAirlinesLogo);
        mAirlinesTW = (TextView) view.findViewById(R.id.infoAirlinesTW);
        mNO = (TextView) view.findViewById(R.id.infoNO);

        mTerminal = (TextView) view.findViewById(R.id.infoTerminal);
        mCounter = (TextView) view.findViewById(R.id.infoCounter);
        mCounterTxt = (TextView) view.findViewById(R.id.infoCounterTxt);
        mGate = (TextView) view.findViewById(R.id.infoGate);

        mStatus = (TextView) view.findViewById(R.id.infoStatus);
        mExpectTime = (TextView) view.findViewById(R.id.infoExpectTime);
        mActualTime = (TextView) view.findViewById(R.id.infoActualTime);
        mDestinationTxt = (TextView) view.findViewById(R.id.infoDestinationTxt);
        mDestinationTW = (TextView) view.findViewById(R.id.infoDestinationTW);
        mPhone = (Button) view.findViewById(R.id.infoPhone);

        mLoading = (ProgressBar) view.findViewById(R.id.infoWX_loading);
        mWXAirport = (TextView) view.findViewById(R.id.infoWX_airport);

        arrayLogo = getResources().obtainTypedArray(R.array.arrayLogo);
        arrayIATA = getResources().getStringArray(R.array.arrayIATA);
        arrayPhone = getResources().getStringArray(R.array.airlinesPhone);

        mView = view;
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //dismiss input keyboard
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(), 0);

        mInfo = getArguments().getParcelable("Flight");
        mAlarmList = getArguments().getParcelableArrayList("ALARMLIST");

        if (mInfo == null)
            return;

        String Airlines = mInfo.getAirlines();
        String AirlinesTW = mInfo.getAirlinesTW();
        String NO = mInfo.getFlightNO();
        String Action = mInfo.getAction();
        String Terminal = mInfo.getTerminal();
        String Counter = mInfo.getCounter();
        String Baggage = mInfo.getBaggage();
        String Gate = mInfo.getGate();

        String Status = mInfo.getStatus();
        String ExpectDay = mInfo.getExpectDay();
        String ExpectTime = mInfo.getExpectTime();
        String ActualDay = mInfo.getActualDay();
        String ActualTime = mInfo.getActualTime();
        String DestinationTW = mInfo.getDestinationTW();

        mAirlinesTW.setText(AirlinesTW);
        mNO.setText(NO);
        mTerminal.setText(Terminal);
        mCounter.setText(Counter);
        mGate.setText(Gate);

        mStatus.setText(Status);
        mExpectTime.setText(ExpectDay + " " + ExpectTime);
        mActualTime.setText(ActualDay + " " + ActualTime);
        mDestinationTW.setText(DestinationTW);

        if (Action.equals("A")) {
            mStatusLogo.setImageResource(R.drawable.v_landing);

            mCounterTxt.setText("行李轉盤");
            mCounter.setText(Baggage);

            mDestinationTxt.setText("出發地");
        }

        for (int i = 0; i < arrayIATA.length; i++){
            if (Airlines.equals(arrayIATA[i].toUpperCase())){
                mAirlinesLogo.setImageResource(arrayLogo.getResourceId(i, 0));
                mPhone.setText(arrayPhone[i]);
                final String phoneNo = arrayPhone[i];
                mPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo));
                        startActivity(intent);
                    }
                });
                break;
            }
        }

        //check WX
        mWXAirport.setText(DestinationTW);
        mAirport = mInfo.getDestination() + "," + DestinationTW;
        mAirport = mAirport.replace(" ", "%20");
        mWXAddr = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" +
                mAirport +
                "&mode=json&units=metric" +
//                "&lang=zh_tw" +
                "&cnt=5&" +
                "appid=44db6a862fba0b067b1930da0d769e98";
        
        new WxAsyncTask().execute(null, null, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.info_alarm) {
            AlarmDialog dialog = new AlarmDialog();
            Bundle infoArgs = new Bundle();
            infoArgs.putParcelable("FLIGHT", mInfo);
            infoArgs.putParcelableArrayList("ALARMLIST", mAlarmList);
            dialog.setArguments(infoArgs);

            dialog.show(getFragmentManager(), "AlarmDialog");

            return true;
        }else if (id == R.id.info_star) {
            String msg = mInfo.getAirlinesTW() + " " + mInfo.getFlightNO() + " 已加入追蹤名單";
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            ((FlightInterface)mContext).onStarClick(mInfo);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (!((FlightInterface)mContext).isDrawerOpen())
            mContext.getMenuInflater().inflate(R.menu.info_menu, menu);
    }

    private class WxAsyncTask extends AsyncTask<Void , Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            try {
                downloadData(mWXAddr);
            } catch (Exception e) {
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
                mJSONdata += StringBuffer;
            }
            bufferReader.close();
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == 1) {
                mLoading.setVisibility(View.GONE);
                try {
                    decodeJSON(mJSONdata);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void decodeJSON(String mContent) throws JSONException {
//        QNLog.d(mAirport + " " + mJSONdata);
        LinearLayout mLayout = (LinearLayout) mView.findViewById(R.id.infoWX_layout);

        JSONArray list = new JSONObject(mContent).getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject daily = (JSONObject) list.get(i);

            Long dt = daily.getLong("dt");
            Date date = new Date(dt * 1000);
            DateFormat df = new SimpleDateFormat("MM/dd");
            String reportDate = df.format(date);

            JSONObject temp = daily.getJSONObject("temp");
            Integer temp_day = temp.getInt("day");
            Integer temp_min = temp.getInt("min");
            Integer temp_max = temp.getInt("max");

            JSONObject weather = (JSONObject) daily.getJSONArray("weather").get(0);
            String icon = weather.getString("icon");
            String raw = weather.getString("description");

            String iconName;
            switch(icon.substring(0, 2)) {
                case "01":
                    iconName = "wx_sunny";
                    break;
                case "02":
                    iconName = "wx_cloudy2";
                    break;
                case "03":
                    iconName = "wx_cloudy3";
                    break;
                case "04":
                    iconName = "wx_cloudy4";
                    break;
                case "09":
                    iconName = "wx_shower1";
                    break;
                case "10":
                    iconName = "wx_shower2";
                    break;
                case "11":
                    iconName = "wx_tstorm1";
                    break;
                case "13":
                    iconName = "wx_snow3";
                    break;
                case "50":
                    iconName = "wx_mist";
                    break;
                default:
                    iconName = "wx_dunno";
                    break;
            }

            if(icon.substring(2, 3).equals("n")){
                iconName += "_night";
            }
//            QNLog.d(mAirport + icon + " " + iconName);

            String infoString = "";
//            infoString = reportDate + "  ";
//            infoString += des;
            if (i == 0) {
                infoString += temp_day + "°";
            }else {
                infoString += temp_min.toString() + "/";
                infoString += temp_max.toString() + "°";
            }

            LinearLayout wx_row = (LinearLayout) mInflater.inflate(R.layout.info_wx, null);
            ImageView iconImg = (ImageView) wx_row.findViewById(R.id.infoWX_img);
            TextView text = (TextView) wx_row.findViewById(R.id.infoWX_text);

            int resId = getResources().getIdentifier(iconName, "drawable", mContext.getPackageName());
            iconImg.setImageResource(resId);
            text.setText(infoString);

            mLayout.addView(wx_row);
        }
    }
}
