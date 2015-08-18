package com.atc.qn.tpeflight;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AirlinesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.airlines, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        getActivity().setTitle("航空公司資訊");

        LinearLayout airlinesTable = (LinearLayout) getActivity().findViewById(R.id.airlinestable);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout rowView;

        TypedArray logo = getResources().obtainTypedArray(R.array.airlines_logo);
        String [] airlines_TW = getResources().getStringArray(R.array.airlines_TW);
        String [] airlines_place = getResources().getStringArray(R.array.airlines_place);
        String [] airlines_phone = getResources().getStringArray(R.array.airlines_phone);

        for (int i = 0; i < airlines_TW.length; i++){
            rowView = (LinearLayout) inflater.inflate(R.layout.airlines_row, null);

            String name = airlines_TW[i];
            String place = airlines_place[i];
            String phone = airlines_phone[i];

            ImageView logoImgView = (ImageView) rowView.findViewById(R.id.logo);
            TextView nameTxtView = (TextView) rowView.findViewById(R.id.name);
            TextView placeTxtView = (TextView) rowView.findViewById(R.id.place);
            TextView phoneTxtView = (TextView) rowView.findViewById(R.id.phone);

            logoImgView.setImageResource(logo.getResourceId(i, 0));
            nameTxtView.setText(name);
            placeTxtView.setText(place);
            phoneTxtView.setText(phone);

            airlinesTable.addView(rowView);
        }
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
