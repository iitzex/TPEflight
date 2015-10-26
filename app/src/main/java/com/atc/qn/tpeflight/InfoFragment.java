package com.atc.qn.tpeflight;

import android.app.Activity;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InfoFragment extends Fragment {
    private Activity mContext;
    private Flight mInfo;
    private ImageView mStatusLogo;
    private ImageView mAirlinesLogo;    
    private TextView mAirlinesTW, mNO, mTerminal;
    private TextView mCounter, mCounterTxt;
    private TextView mGate, mStatus;
    private TextView mExpectTime, mActualTime;
    private TextView mDestinationTxt, mDestinationTW;
    private TextView mType;
    private Button mPhone;

    private TypedArray arrayLogo;
    private String [] arrayIATA;
    private String [] arrayPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mContext = getActivity();
        mContext.setTitle(mContext.getString(R.string.name_info));

        View view = inflater.inflate(R.layout.info, container, false);
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
        mType = (TextView) view.findViewById(R.id.infoType);
        mPhone = (Button) view.findViewById(R.id.infoPhone);

        arrayLogo = getResources().obtainTypedArray(R.array.arrayLogo);
        arrayIATA = getResources().getStringArray(R.array.arrayIATA);
        arrayPhone = getResources().getStringArray(R.array.airlinesPhone);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //dismiss input keyboard
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(), 0);

        mInfo = getArguments().getParcelable("Flight");
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
    }

    @Override
    public void onStop() {
//        arrayLogo.recycle();
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.info_alarm) {
            AlarmDialog dialog = new AlarmDialog();
            Bundle infoArgs = new Bundle();
            infoArgs.putParcelable("Flight", mInfo);
            dialog.setArguments(infoArgs);

            dialog.show(getFragmentManager(), "AlarmDialog");

            return true;
        }else if (id == R.id.info_star) {
            String msg = mInfo.getAirlinesTW() + " " + mInfo.getFlightNO() + "已加入追蹤名單";
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            ((FlightInterface)mContext).onStarClick(mInfo);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        mContext.getMenuInflater().inflate(R.menu.info_menu, menu);
    }
}
