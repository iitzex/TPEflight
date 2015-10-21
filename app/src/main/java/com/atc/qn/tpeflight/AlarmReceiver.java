package com.atc.qn.tpeflight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("name");
        Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
        QNLog.d("AlarmReceived");
    }
}

