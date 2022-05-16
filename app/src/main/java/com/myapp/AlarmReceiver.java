package com.myapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.myapp.dictionary.EnWordDetailActivity2;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.learnenglish.LearnEnglishActivity;
import com.myapp.model.EnWord;
import com.myapp.model.Meaning;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_1 = "channel_1";
    private static final String CHANNEL_2 = "channel_2";

    private static final String GROUP_1 = "group_1";
    private static final int REQUEST_TIMER1 = 1;
    private static final String PARAM_NAME = "name";

    private int random(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public EnWord getWord(Integer enWordId, Context context) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context.getApplicationContext());
        databaseAccess.open();
        EnWord enWord = databaseAccess.getOneEnWord(enWordId);
        databaseAccess.close();
        return enWord;
    }

    private void createNotificationChannel(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel(CHANNEL_1);
            if (channel == null) {
                channel = new NotificationChannel(CHANNEL_1, "Từ của hôm nay", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Khám phá một từ bất kỳ nào đó mà bạn có thể chưa biết");
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                manager.createNotificationChannel(channel);
            }

            NotificationChannel channel2 = manager.getNotificationChannel(CHANNEL_2);
            if (channel2 == null) {
                channel2 = new NotificationChannel(CHANNEL_2, "Học tiếng Anh", NotificationManager.IMPORTANCE_HIGH);
                channel2.setDescription("Luyện tập tiếng Anh ngay để luôn cải thiện khả năng các bạn nhé!");
                channel2.enableVibration(true);
                channel2.setVibrationPattern(new long[]{100, 1000, 200, 340});
                channel2.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                manager.createNotificationChannel(channel2);
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);
//        int random = random(1, 2);
//        if (random == 1) {
//            notificationWord(context);
//        } else {
//            notificationStudy(context);
//        }
        notificationWord(context);
    }

    private void notificationStudy(Context context) {
        Intent notifyIntent = new Intent(context, LearnEnglishActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                context, getNotificationId(), notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_2)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setColor(context.getColor(R.color.space_cadet))
                .setContentTitle("Học tiếng Anh thôi!")
                .setContentText("Tiếp tục cải thiện thành tích và rèn luyên của chính mình")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{100, 1000, 200, 340})
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setGroup(GROUP_1)
                .setContentIntent(notifyPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setTicker("Notification")
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(getNotificationId(), notification);
    }

    private void notificationWord(Context context) {
        int max = 79662;
        int min = 21567;
        int enWordId = random(min, max);
        EnWord enWord = getWord(enWordId, context);

        String content = "";
        for (Meaning meaning : enWord.getListMeaning()) {
            content += "(" + meaning.getPartOfSpeechName() + ") " + meaning.getMeaning() + "\n";
        }

        Intent notifyIntent = new Intent(context, EnWordDetailActivity2.class);
        notifyIntent.putExtra("enWordId", enWordId);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                context, getNotificationId(), notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1)
                .setColor(context.getColor(R.color.space_cadet))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentTitle(enWord.getWord() + " " + enWord.getPronunciation())
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{100, 1000, 200, 340})
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setGroup(GROUP_1)
                .setContentIntent(notifyPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setTicker("Notification")
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(getNotificationId(), notification);
    }

    private void notificationSummary(Context context) {
//        RemoteViews notificationLayout =
//                new RemoteViews(context.getPackageName(), R.layout.notification);

        Notification notification2 = new NotificationCompat.Builder(context, CHANNEL_1)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{100, 1000, 200, 340})
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setGroup(GROUP_1)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setGroupSummary(true)
                .setTicker("Notification")
                .build();
    }

    private int getNotificationId() {
        return (int) new Date().getTime();
    }
}
