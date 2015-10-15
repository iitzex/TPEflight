package com.atc.qn.tpeflight;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WxFragment extends Fragment{
    String content = "";
    TextView wx_content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.wx, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        wx_content = (TextView)getView().findViewById(R.id.wx_content);
        reload();

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getString(R.string.name_wx));
    }

    private void reload() {
        String addr = "http://aoaws.caa.gov.tw/cgi-bin/wmds/aoaws_metars?metar_ids=" +
                "RCTP" + "&NHOURS=Lastest&std_trans=";
        new WxAsyncTask().execute(addr, null, null);
    }

    private void loaded(){
        decode(content);
    }

    public void decode(String rawdata) {
        String info = parse(rawdata, "<PRE>.*(" + "RCTP" + " .*)</PRE>");
        if (info.equals("EMPTY!!"))
            return;

        wx_content.setText(info);

        String time = info.substring(5, 9).trim();
        String degree = info.substring(10, 13).trim();
        String scale = info.substring(14, 17).trim();
        String gst = info.substring(18, 21).trim();
        String vis = info.substring(22, 26).trim();
        String weather = info.substring(31, 38).trim();
        String ceil = info.substring(39, 43).trim();
        String tmp = info.substring(44, 47).trim();
        String dew = info.substring(48, 51).trim();
        String QNH = info.substring(52, 56).trim();

        String mTime = time + " UTC";
        String mWind = degree + "° " + scale + " kt";
        mWind += gst.equals("") ? "" : ", gusting " + gst + "kt";
        String mVis = vis.equals("") ? "" : translateVis(vis);
        String mWX = weather.equals("") ? "" : translateWX(weather);
        String mCeil = ceil.equals("") ? "" : ceil.replaceFirst("^0+(?!$)", "") + "00 ft";
        String mTemp = tmp + " °C";
        String mDew = dew + " °C";
        String mQNH = QNH + " hPa";
    }
    private String translateVis(String vis){
        if (vis.equals("10k+")){
            return "10KM or more";
        }else
            return vis + " m";
    }

    private String translateWX(String wx){
        //INTENSITY OR PROXIMITY 1
        String modifiedWX = wx.replace("-", "Light ")
                .replace("+", "Heavy ")
                .replace("VC", "In the vicinity, ")
                        //DESCRIPTOR 2
                .replace("MI", "Shallow ")
                .replace("PR", "Partial ")
                .replace("BC", "Patches ")
                .replace("DR", "Low Drifting ")
                .replace("BL", "Blowing ")
                .replace("SH", "Shower ")
                .replace("TS", "Thunderstorm ")
                .replace("FZ", "Freezing ")
                        //PRECIPITATION 3
                .replace("DZ", "Drizzle, ")
                .replace("RA", "Rain, ")
                .replace("SN", "Snow, ")
                .replace("SG", "Snow Grains, ")
                .replace("IC", "Ice Crystals, ")
                .replace("PL", "Ice Pellets, ")
                .replace("GR", "Hail, ")
                .replace("GS", "Small Hail and/or Snow Pellets, ")
                .replace("UP", "Unknown Precipitation, ")
                        //OBSCURATION 4
                .replace("BR", "Mist, ")
                .replace("FG", "Fog, ")
                .replace("FU", "Smoke, ")
                .replace("VA", "Volcanic Ash, ")
                .replace("DU", "Widespread Dust, ")
                .replace("SA", "Sand, ")
                .replace("HZ", "Haze, ")
                .replace("PY", "Spray, ")
                        //OTHER 5
                .replace("PO", "Well-Developed Dust/Sand Whirls, ")
                .replace("SQ", "Squalls, ")
                .replace("FC", "Funnel Cloud Tornado Waterspout, ")
                .replace("SS", "Sandstorm, ")
                .replace("DS", "Duststorm, ");

        return modifiedWX;
    }

    private String parse(String rawData, String expression){
        if(rawData == null){
            return "";
        }

        Matcher m =  Pattern.compile(expression).matcher(rawData);
        String result = "";
        int i = 0;
        while (m.find()) {
            //LogD.out(String.valueOf(i++) + "_" + m.group(1));
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
            reload();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class WxAsyncTask extends AsyncTask<String, Integer, Integer> {
        ProgressBar loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = (ProgressBar)getView().findViewById(R.id.wx_loading);
            loading.setVisibility(View.VISIBLE);
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
                //String[] info = StringBuffer.split(",");
                content += StringBuffer;
            }

            bufferReader.close();
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == 1) {
                loading.setVisibility(View.INVISIBLE);
                loaded();
            }
        }
    }

}
