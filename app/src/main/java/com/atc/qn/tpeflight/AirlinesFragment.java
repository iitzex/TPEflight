package com.atc.qn.tpeflight;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;

public class AirlinesFragment extends Fragment {
    LayoutInflater inflater;
    static private ArrayList<LinearLayout> airlines_array;
    static private LinearLayout top_layout;

    private static TypedArray arrayLogo;
    private static String [] airlinesTWList;
    private static String [] airlinesPlaceList;
    private static String [] airlinesPhoneList;
    private ProgressBar loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (arrayLogo == null)
            arrayLogo = getResources().obtainTypedArray(R.array.arrayLogo);
        if (airlinesTWList == null)
            airlinesTWList = getResources().getStringArray(R.array.arrayAirlinesTW);
        if (airlinesPlaceList == null)
            airlinesPlaceList = getResources().getStringArray(R.array.arrayPlace);
        if (airlinesPhoneList == null)
            airlinesPhoneList = getResources().getStringArray(R.array.airlinesPhone);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.airlines, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(getActivity().getString(R.string.name_airlines));

        loading = (ProgressBar)getView().findViewById(R.id.airlines_loading);
        loading.setVisibility(View.VISIBLE);
        top_layout = (LinearLayout) getActivity().findViewById(R.id.airlinestable);

        new AirlinesAsyncTask().execute("Airlines", null, null);
    }

    @Override
    public void onDestroy() {
//        arrayLogo.recycle();
        top_layout.removeAllViewsInLayout();
        super.onDestroy();
    }

    private void getAirlines() {
        airlines_array = new ArrayList<>();
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < airlinesTWList.length; i++){
            LinearLayout airlines_row = (LinearLayout) inflater.inflate(R.layout.airlines_row, null);

            ImageView logoImgView = (ImageView) airlines_row.findViewById(R.id.logo);
            TextView nameTxtView = (TextView) airlines_row.findViewById(R.id.name);
            TextView placeTxtView = (TextView) airlines_row.findViewById(R.id.place);
            Button phoneBtn = (Button) airlines_row.findViewById(R.id.phone);

            final String name = airlinesTWList[i];
            final String place = airlinesPlaceList[i];
            final String phone = airlinesPhoneList[i];

            logoImgView.setImageResource(arrayLogo.getResourceId(i, 0));
            nameTxtView.setText(name);
            placeTxtView.setText(place);
            phoneBtn.setText(phone);
            phoneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                }
            });

            airlines_array.add(airlines_row);
        }
    }

    private class AirlinesAsyncTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            top_layout.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            getAirlines();
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            for(LinearLayout item : airlines_array) {
                top_layout.addView(item);
            }

            top_layout.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
        }
    }
}
