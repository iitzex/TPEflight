package com.atc.qn.tpeflight;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WxFragment extends Fragment{
    private String mContent = "";
    private TextView wx_vis, wx_wind, wx_cloud, wx_temp, wx_dew;
    private ImageView wx_icon;
    private WxAsyncTask mTask;
    private ProgressBar mLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.wx, container, false);
        wx_vis = (TextView) view.findViewById(R.id.wx_vis);
        wx_wind = (TextView) view.findViewById(R.id.wx_wind);
        wx_cloud = (TextView) view.findViewById(R.id.wx_cloud);
        wx_temp = (TextView) view.findViewById(R.id.wx_temperature);
        wx_dew = (TextView) view.findViewById(R.id.wx_dewpoint);
        wx_icon = (ImageView) view.findViewById(R.id.wx_icon);
        mLoading = (ProgressBar) view.findViewById(R.id.wx_loading);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        fetchData();

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getString(R.string.name_wx));
    }

    private void fetchData() {
        mTask = (WxAsyncTask) new WxAsyncTask().execute(null, null, null);
    }

    private void decode(String rawdata) {
        String info = parse(rawdata, "<PRE>.*(" + "RCTP" + " .*)</PRE>");
        if (info.equals("EMPTY!!"))
            return;

        String degree = info.substring(10, 13).trim();
        String scale = info.substring(14, 17).trim();
//        String gst = info.substring(18, 21).trim();
        String vis = info.substring(22, 26).trim();
        String weather = info.substring(31, 38).trim();
        String ceil = info.substring(39, 43).trim();
        String temp = info.substring(44, 47).trim();
        String dew = info.substring(48, 51).trim();

        double kmh = Integer.valueOf(scale) * 1.852;
        String mWind = degree + "° " + String.valueOf((int) kmh) + " 公里/時";
//        mWind += gst.equals("") ? "" : ", 陣風 " + String.valueOf(Integer.valueOf(gst) * 1.852) + " 公里/時";
        String mVis = vis.equals("") ? "" : translateVis(vis);
//        String mWX = weather.equals("") ? "" : translateWX(weather);
        String mCloud = ceil.equals("") ? "" : ceil.replaceFirst("^0+(?!$)", "") + "00 呎";
        String mTemp = temp + " °C";
        String mDew = dew + " °C";

        setIcon(weather, ceil);

        wx_vis.setText("能見度：\t" + mVis);
        wx_wind.setText("風向/速：\t" + mWind);
        wx_temp.setText("溫度：\t" + mTemp);
        wx_dew.setText(("露點：\t" + mDew));

        if (mCloud.equals(""))
            wx_cloud.setVisibility(View.GONE);
        else
            wx_cloud.setText("雲幕高：\t" + mCloud);

    }

    private void setIcon(String wx, String ceil)
    {
        Calendar timeInst = Calendar.getInstance();
        SimpleDateFormat day = new SimpleDateFormat("HH");
        String hour = day.format(timeInst.getTime());
        String nightTag = "";
        String iconName = "wx_";
        Integer ceilValue;
        if (ceil.equals("")) {
            ceilValue = Integer.valueOf("0");
        }else {
            ceilValue = Integer.valueOf(ceil);
        }

        if(Integer.valueOf(hour) >= 18 || Integer.valueOf(hour) < 6) {
            nightTag = "_night";
        }

        if (wx.contains("TS")) {
            iconName += "tstorm1" + nightTag;
        }else if (wx.contains("RA")) {
            iconName += "light_rain";
        }else if (wx.contains("SH")) {
            iconName += "shower2" + nightTag;
        }else if (wx.contains("DZ")) {
            iconName += "shower1" + nightTag;
        }else if (wx.contains("BR")) {
            iconName += "mist" + nightTag;
        }else if (wx.contains("FG")) {
            iconName += "fog" + nightTag;
        }else if (ceilValue > 0 && ceilValue <= 10) {
            iconName += "cloudy5";
        }else if (ceilValue > 0 && ceilValue <= 20) {
            iconName += "cloudy4" + nightTag;
        }else if (ceilValue > 0 && ceilValue <= 40) {
            iconName += "cloudy3" + nightTag;
        }else if (ceilValue > 0 && ceilValue <= 60) {
            iconName += "cloudy2" + nightTag;
        }else if (ceilValue > 0 && ceilValue <= 80) {
            iconName += "cloudy1" + nightTag;
        }else {
            iconName += "sunny" + nightTag;
        }

        int resId = getResources().getIdentifier(iconName, "drawable", getActivity().getPackageName());
        wx_icon.setImageResource(resId);
    }

    private String translateVis(String vis) {
        if (vis.equals("10k+")) {
            return "10公里以上";
        } else {
            return vis + " 公尺";
        }
    }
//    private String translateWX(String wx){
//                //INTENSITY OR PROXIMITY 1
//        return wx.replace("-", "Light ")
//                .replace("+", "Heavy ")
//                .replace("VC", "In the vicinity, ")
//                //DESCRIPTOR 2
//                .replace("MI", "Shallow ")
//                .replace("PR", "Partial ")
//                .replace("BC", "Patches ")
//                .replace("DR", "Low Drifting ")
//                .replace("BL", "Blowing ")
//                .replace("SH", "Shower ")
//                .replace("TS", "Thunderstorm ")
//                .replace("FZ", "Freezing ")
//                //PRECIPITATION 3
//                .replace("DZ", "Drizzle, ")
//                .replace("RA", "Rain, ")
//                .replace("SN", "Snow, ")
//                .replace("SG", "Snow Grains, ")
//                .replace("IC", "Ice Crystals, ")
//                .replace("PL", "Ice Pellets, ")
//                .replace("GR", "Hail, ")
//                .replace("GS", "Small Hail and/or Snow Pellets, ")
//                .replace("UP", "Unknown Precipitation, ")
//                //OBSCURATION 4
//                .replace("BR", "Mist, ")
//                .replace("FG", "Fog, ")
//                .replace("FU", "Smoke, ")
//                .replace("VA", "Volcanic Ash, ")
//                .replace("DU", "Widespread Dust, ")
//                .replace("SA", "Sand, ")
//                .replace("HZ", "Haze, ")
//                .replace("PY", "Spray, ")
//                //OTHER 5
//                .replace("PO", "Well-Developed Dust/Sand Whirls, ")
//                .replace("SQ", "Squalls, ")
//                .replace("FC", "Funnel Cloud Tornado Waterspout, ")
//                .replace("SS", "Sandstorm, ")
//                .replace("DS", "Duststorm, ");
//    }

    private String parse(String rawData, String expression){
        if(rawData == null){
            return "";
        }

        Matcher m =  Pattern.compile(expression).matcher(rawData);
        String result = "";
        while (m.find()) {
            result += m.group(1) + "\n\n";
        }
        if (result.equals(""))
            return "EMPTY!!";
        else
            return  result;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.wx_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.wx_refresh) {
            fetchData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        mTask.cancel(true);
        super.onStop();
    }

    private class WxAsyncTask extends AsyncTask<Void , Void, Integer> {
        final String addr = "http://aoaws.caa.gov.tw/cgi-bin/wmds/aoaws_metars?metar_ids=" +
                "RCTP" + "&NHOURS=Lastest&std_trans=";

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
                mContent += StringBuffer;
            }
            bufferReader.close();
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == 1) {
                mLoading.setVisibility(View.INVISIBLE);
                decode(mContent);
            }
        }
    }
}
