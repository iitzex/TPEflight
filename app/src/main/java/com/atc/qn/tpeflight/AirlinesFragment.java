package com.atc.qn.tpeflight;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AirlinesFragment extends Fragment {
    LinearLayout airlinesTable;
    LayoutInflater inflater;

    private TypedArray arrayLogo;
    private String [] airlinesTWList;
    private String [] airlinesPlaceList;
    private String [] airlinesPhoneList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrayLogo = getResources().obtainTypedArray(R.array.arrayLogo);
        airlinesTWList = getResources().getStringArray(R.array.arrayAirlinesTW);
        airlinesPlaceList = getResources().getStringArray(R.array.arrayPlace);
        airlinesPhoneList = getResources().getStringArray(R.array.airlinesPhone);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.airlines, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("航空公司資訊");
        showAirlines();
    }

    @Override
    public void onDestroy() {
        arrayLogo.recycle();
        super.onDestroy();
    }

    private void showAirlines() {
        LinearLayout rowView;

        airlinesTable = (LinearLayout) getActivity().findViewById(R.id.airlinestable);
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < airlinesTWList.length; i++){
            rowView = (LinearLayout) inflater.inflate(R.layout.airlines_row, null);

            ImageView logoImgView = (ImageView) rowView.findViewById(R.id.logo);
            TextView nameTxtView = (TextView) rowView.findViewById(R.id.name);
            TextView placeTxtView = (TextView) rowView.findViewById(R.id.place);
            Button phoneBtn = (Button) rowView.findViewById(R.id.phone);

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

            airlinesTable.addView(rowView);
        }
    }
}
