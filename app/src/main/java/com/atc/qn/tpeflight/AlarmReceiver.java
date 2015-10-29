package com.atc.qn.tpeflight;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("ALARMMSG");
        String alarmMsg = intent.getAction();
        Toast.makeText(context, alarmMsg, Toast.LENGTH_SHORT).show();
        QNLog.d("AlarmReceived, " + alarmMsg);


        if (MainActivity.instance() != null) {
            QNLog.d("hit");
        }else
            QNLog.d("fail");

        Notification.Builder builder = new Notification.Builder(context);
        builder.setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_airplane)
            .setContentTitle(context.getString(R.string.name_alarm))
            .setContentText(alarmMsg);

        long[] vibrate_effect = {1000, 500, 1000, 500, 1000, 500, 1000, 500, 1000, 500};
        builder.setVibrate(vibrate_effect)
            .setLights(Color.GREEN, 1000, 1000);

        int BASIC_ID = 1;
        NotificationManager nNotificationMgr = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        nNotificationMgr.notify(BASIC_ID, builder.build());
    }
}

