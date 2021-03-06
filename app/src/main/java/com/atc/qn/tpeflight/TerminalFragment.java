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

public class TerminalFragment extends Fragment{
    private LinearLayout mLayout;
    private ProgressBar mLoading;
    private String mTerminal;

    private ArrayList<LinearLayout> mViews;
    private static TypedArray mLogos;
    private static String [] mAirlinesTWs, mIATA, mPlace, mPhones;

    public static TerminalFragment newInstance(String terminal) {
        TerminalFragment termFrag = new TerminalFragment();
        Bundle args = new Bundle();
        args.putString("TERMINAL", terminal);
        termFrag.setArguments(args);
        return termFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTerminal = getArguments().getString("TERMINAL", "NULL");

        if (mLogos == null)
            mLogos = getResources().obtainTypedArray(R.array.arrayLogo);
        if (mAirlinesTWs == null)
            mAirlinesTWs = getResources().getStringArray(R.array.arrayAirlinesTW);
        if (mIATA == null)
            mIATA = getResources().getStringArray(R.array.arrayIATA);
        if (mPlace == null)
            mPlace = getResources().getStringArray(R.array.arrayPlace);
        if (mPhones == null)
            mPhones = getResources().getStringArray(R.array.airlinesPhone);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.terminal, container, false);
        mLoading = (ProgressBar) view.findViewById(R.id.terminal_loading);
        mLayout = (LinearLayout) view.findViewById(R.id.terminal_content);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(getActivity().getString(R.string.name_airlines));

        mLoading.setVisibility(View.VISIBLE);
        new AirlinesAsyncTask().execute(null, null, null);
    }

    @Override
    public void onDestroy() {
//        mLogos.recycle();
        mLayout.removeAllViewsInLayout();
        super.onDestroy();
    }

    private void getAirlines() {
        mViews = new ArrayList<>();
        LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < mAirlinesTWs.length; i++){
            final String name = mAirlinesTWs[i];
            final String place = mPlace[i];
            final String phone = mPhones[i];
            final String IATA = mIATA[i];

            if (!place.equals(mTerminal))
                continue;

            LinearLayout airlines_row = (LinearLayout) mInflater.inflate(R.layout.airlines_row, null);
            ImageView logoImgView = (ImageView) airlines_row.findViewById(R.id.airlines_logo);
            TextView nameTxtView = (TextView) airlines_row.findViewById(R.id.airlines_name);
            TextView IATATxtView = (TextView) airlines_row.findViewById(R.id.airlines_IATA);
            Button phoneBtn = (Button) airlines_row.findViewById(R.id.airlines_phone);

            logoImgView.setImageResource(mLogos.getResourceId(i, 0));
            nameTxtView.setText(name);
            IATATxtView.setText("("+IATA.toUpperCase()+")");
            phoneBtn.setText(phone);
            phoneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                }
            });

            mViews.add(airlines_row);
        }
    }

    private class AirlinesAsyncTask extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            getAirlines();
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            for(LinearLayout item : mViews) {
                mLayout.addView(item);
            }

            mLoading.setVisibility(View.GONE);
        }
    }
}
