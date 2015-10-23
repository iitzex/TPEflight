package com.atc.qn.tpeflight;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

public class AlarmDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alarm, null);

        CheckBox alarm_15m = (CheckBox)view.findViewById(R.id.alarm_15m);
        CheckBox alarm_30m = (CheckBox)view.findViewById(R.id.alarm_30m);
        CheckBox alarm_1h = (CheckBox)view.findViewById(R.id.alarm_1h);
        CheckBox alarm_2h = (CheckBox)view.findViewById(R.id.alarm_2h);
        CheckBox alarm_3h = (CheckBox)view.findViewById(R.id.alarm_3h);
        CheckBox alarm_4h = (CheckBox)view.findViewById(R.id.alarm_4h);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("完成",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                FlightInterface mCallback = (FlightInterface) getActivity();
                                mCallback.onAlarmClick();
                            }
                        });

        return builder.create();
    }
}