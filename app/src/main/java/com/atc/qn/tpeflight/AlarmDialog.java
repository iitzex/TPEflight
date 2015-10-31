package com.atc.qn.tpeflight;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmDialog extends DialogFragment {
    private Flight mInfo;
    private Context mContext;
    private ArrayList<Flight> mAlarmList;
    private ArrayList<CheckBox> mCheckBoxList = new ArrayList<>();
    private ArrayList<Calendar> mCalendarList = new ArrayList<>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        mInfo = getArguments().getParcelable("FLIGHT");
        mAlarmList = getArguments().getParcelableArrayList("ALARMLIST");

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        View view = inflater.inflate(R.layout.alarm_dialog, null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.alarm_layout);

        String mActualTime = mInfo.getActualDay() + ", " + mInfo.getActualTime();
        for (int i = 0; i < 6; i++) {
            Calendar mCal = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
            try {
                mCal.setTime(format.parse(mActualTime));
            } catch (Exception e) {
                e.printStackTrace();
            }

            int timeDelta = 0;
            String msg = "準時呼叫";
            if (i == 1) {
                timeDelta = -15;
                msg = "15分鐘前呼叫";
            }else if (i == 2) {
                timeDelta = -30;
                msg = "30分鐘前呼叫";
            }else if (i == 3) {
                timeDelta = -60;
                msg = "1小時前呼叫";
            }else if (i == 4) {
                timeDelta = -120;
                msg = "2小時前呼叫";
            }else if (i == 5) {
                timeDelta = -240;
                msg = "4小時前呼叫";
            }

            mCal.add(Calendar.MINUTE, timeDelta);
            format = new SimpleDateFormat("HH:mm");
            String ringTime = format.format(mCal.getTime());
            mCalendarList.add(mCal);

            Calendar now = Calendar.getInstance();
            setCheckBox(layout, mCal.after(now), msg, ringTime);
        }

        builder.setView(view)
            .setPositiveButton("完成",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            addAlarmList();
                        }
                    });
        return builder.create();
    }

    private void setCheckBox(LinearLayout layout, boolean show, String msg, String ringTime) {
        CheckBox item = new CheckBox(mContext);
        item.setPadding(15, 15, 15, 15);
        item.setTextSize(20);
        item.setText(msg + ", " + ringTime);
        if (show) {
            item.setTextColor(mContext.getResources().getColor(R.color.myPrimaryDarkColor));
        }else {
            item.setTextColor(mContext.getResources().getColor(R.color.md_blue_grey_200));
            item.setClickable(false);
        }
        mCheckBoxList.add(item);
        layout.addView(item);
    }

    private void addAlarmList() {
        for (int i = 0; i < 6; i++) {
            if (mCheckBoxList.get(i).isChecked()) {
                Flight target = new Flight(mInfo);
                Calendar mCal = mCalendarList.get(i);
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                String ringTime = format.format(mCal.getTime());
                long triggerAtMillis = mCal.getTimeInMillis();
                target.setAlarmTag(ringTime);
                target.setKey((int)triggerAtMillis);

                mAlarmList.add(target);
                String alarmMsg = target.getAirlinesTW() + " " + target.getFlightNO() + ", " + ringTime;
                setAlarm(alarmMsg, triggerAtMillis);
            }
        }
    }

    private void setAlarm(String alarmMsg, long triggerAtMillis){
        AlarmManager mAlarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
//        intent.putExtra("ALARMMSG", alarmMsg);
        intent.setAction(alarmMsg);

        Calendar timeInst = Calendar.getInstance();
        intent.putExtra("KEY", (int)timeInst.getTimeInMillis());

        PendingIntent pending = PendingIntent.getBroadcast(mContext, (int)triggerAtMillis,
                                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmMgr.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pending);
    }
}
