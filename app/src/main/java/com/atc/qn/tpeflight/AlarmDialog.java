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
    private Flight mInfo;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alarm_dialog, null);
        final boolean[] timeTable = new boolean[6];
        mInfo = getArguments().getParcelable("Flight");

        final CheckBox alarm_ontime = (CheckBox)view.findViewById(R.id.alarm_ontime);
        final CheckBox alarm_15m = (CheckBox)view.findViewById(R.id.alarm_15m);
        final CheckBox alarm_30m = (CheckBox)view.findViewById(R.id.alarm_30m);
        final CheckBox alarm_1h = (CheckBox)view.findViewById(R.id.alarm_1h);
        final CheckBox alarm_2h = (CheckBox)view.findViewById(R.id.alarm_2h);
        final CheckBox alarm_4h = (CheckBox)view.findViewById(R.id.alarm_4h);

        builder.setView(view)
            // Add action buttons
            .setPositiveButton("完成",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        timeTable[0] = alarm_ontime.isChecked();
                        timeTable[1] = alarm_15m.isChecked();
                        timeTable[2] = alarm_30m.isChecked();
                        timeTable[3] = alarm_1h.isChecked();
                        timeTable[4] = alarm_2h.isChecked();
                        timeTable[5] = alarm_4h.isChecked();

                        ((FlightInterface) getActivity()).onAlarmClick(mInfo, timeTable);
                    }
                });

        return builder.create();
    }
}
