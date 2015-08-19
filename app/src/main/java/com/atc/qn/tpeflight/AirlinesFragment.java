package com.atc.qn.tpeflight;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AirlinesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        getActivity().setTitle("航空公司資訊");

        return inflater.inflate(R.layout.airlines, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayout airlinesTable = (LinearLayout) getActivity().findViewById(R.id.airlinestable);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout rowView;

        TypedArray arrayLogo = getResources().obtainTypedArray(R.array.arrayLogo);
        String [] airlinesTWList = getResources().getStringArray(R.array.arrayAirlinesTW);
        String [] airlinesPlaceList = getResources().getStringArray(R.array.arrayPlace);
        String [] airlinesPhoneList = getResources().getStringArray(R.array.airlinesPhone);

        for (int i = 0; i < airlinesTWList.length; i++){
            rowView = (LinearLayout) inflater.inflate(R.layout.airlines_row, null);

            final String name = airlinesTWList[i];
            final String place = airlinesPlaceList[i];
            final String phone = airlinesPhoneList[i];

            ImageView logoImgView = (ImageView) rowView.findViewById(R.id.logo);
            TextView nameTxtView = (TextView) rowView.findViewById(R.id.name);
            TextView placeTxtView = (TextView) rowView.findViewById(R.id.place);
            Button phoneBtn = (Button) rowView.findViewById(R.id.phone);

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
