package com.atc.qn.tpeflight;


import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        getActivity().setTitle("航班資訊");

        return inflater.inflate(R.layout.info, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String Airlines = getArguments().getString("Airlines", "Airlines");
        String AirlinesTW = getArguments().getString("AirlinesTW", "AirlinesTW");
        String NO = getArguments().getString("NO", "NO");
        String Action = getArguments().getString("Action", "Action");
        String Terminal = getArguments().getString("Terminal", "Terminal");
        String Counter = getArguments().getString("Counter", "Counter");
        String Baggage = getArguments().getString("Baggage", "Baggage");
        String Gate = getArguments().getString("Gate", "Gate");

        String Status = getArguments().getString("Status", "Status");
        String ExpectDay = getArguments().getString("ExpectDay", "ExpectDay");
        String ExpectTime = getArguments().getString("ExpectTime", "ExpectTime");
        String ActualDay = getArguments().getString("ActualDay", "ActualDay");
        String ActualTime = getArguments().getString("ActualTime", "ActualTime");

        String DestinationTW = getArguments().getString("DestinationTW", "DestinationTW");
        String Type = getArguments().getString("Type", "Type");

        ImageView mStatusLogo = (ImageView) getActivity().findViewById(R.id.infoStatusLogo);
        ImageView mAirlinesLogo = (ImageView) getActivity().findViewById(R.id.infoAirlinesLogo);
        TextView mAirlinesTW = (TextView) getActivity().findViewById(R.id.infoAirlinesTW);
        TextView mAirlines = (TextView) getActivity().findViewById(R.id.infoAirlines);
        TextView mNO = (TextView) getActivity().findViewById(R.id.infoNO);

        TextView mTerminal = (TextView) getActivity().findViewById(R.id.infoTerminal);
        TextView mCounter = (TextView) getActivity().findViewById(R.id.infoCounter);
        TextView mCounterTxt = (TextView) getActivity().findViewById(R.id.infoCounterTxt);
        TextView mGate = (TextView) getActivity().findViewById(R.id.infoGate);

        TextView mStatus = (TextView) getActivity().findViewById(R.id.infoStatus);
        TextView mExpectTime = (TextView) getActivity().findViewById(R.id.infoExpectTime);
        TextView mActualTime = (TextView) getActivity().findViewById(R.id.infoActualTime);
        TextView mDestinationTxt = (TextView) getActivity().findViewById(R.id.infoDestinationTxt);
        TextView mDestinationTW = (TextView) getActivity().findViewById(R.id.infoDestinationTW);
        TextView mType = (TextView) getActivity().findViewById(R.id.infoType);
        Button mPhone = (Button) getActivity().findViewById(R.id.infoPhone);

        mAirlinesTW.setText(AirlinesTW);
        mAirlines.setText(Airlines);
        mNO.setText(NO);
        mTerminal.setText(Terminal);
        mCounter.setText(Counter);
        mGate.setText(Gate);

        mStatus.setText(Status);
        mExpectTime.setText(ExpectDay + " " + ExpectTime);
        mActualTime.setText(ActualDay + " " + ActualTime);
        mDestinationTW.setText(DestinationTW);
        mType.setText(Type);

        if (Action.equals("A")) {
            mStatusLogo.setImageResource(R.drawable.v_landing);

            mCounterTxt.setText("行李轉盤");
            mCounter.setText(Baggage);

            mDestinationTxt.setText("出發地");
        }

        final TypedArray arrayLogo = getResources().obtainTypedArray(R.array.arrayLogo);
        final String [] arrayIATA = getResources().getStringArray(R.array.arrayIATA);
        final String [] arrayPhone = getResources().getStringArray(R.array.airlinesPhone);

        for (int i = 0; i < arrayIATA.length; i++){
            if (arrayIATA[i].toUpperCase().equals(Airlines)){
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
        arrayLogo.recycle();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem refreshItem = menu.findItem(R.id.action_refresh);
        MenuItem locateItem = menu.findItem(R.id.action_locate);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        if (refreshItem != null) {
            refreshItem.setVisible(false);
        }
        if (locateItem != null) {
            locateItem.setVisible(false);
        }
        if (searchItem != null) {
            searchItem.setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

}
