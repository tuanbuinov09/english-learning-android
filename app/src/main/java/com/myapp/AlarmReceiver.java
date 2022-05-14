package com.myapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, NotificationActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        String id = "my_channel_id_01";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, id)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentTitle("Hello")
                .setContentText("Hi there!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{100, 1000, 200, 340})
                .setSmallIcon(R.drawable.ic_baseline_arrow_right_24)
                .setAutoCancel(false)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setTicker("Notification");

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());
    }
}
