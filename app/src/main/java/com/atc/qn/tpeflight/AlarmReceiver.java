package com.atc.qn.tpeflight;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("GETNAME");
        Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
        QNLog.d("AlarmReceived");

        Notification.Builder builder = new Notification.Builder(context);
        builder.setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("Basic Notification")
            .setContentText("Demo for basic notification control.")
            .setContentInfo("test");

        long[] vibrate_effect = {1000, 500, 1000, 500, 1000, 500, 1000, 500, 1000, 500};
        builder.setVibrate(vibrate_effect)
            .setLights(Color.GREEN, 1000, 1000);

        int BASIC_ID = 1;
        NotificationManager mManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.notify(BASIC_ID, builder.build());
    }
}

