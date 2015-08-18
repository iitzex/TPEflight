package com.atc.qn.tpeflight;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
