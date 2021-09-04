package com.example.medicinealarm;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class ReminderAlarmService extends IntentService {
    private static final String TAG = ReminderAlarmService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;
    public static PendingIntent getReminderPendingIntent(Context context) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        return PendingIntent.getService(context, (int) Calendar.getInstance().getTimeInMillis(), action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public ReminderAlarmService() {
        super(TAG);
    }

    @SuppressLint("Range")
    @Override
    protected void onHandleIntent(Intent intent) {

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent action = new Intent(this, MainActivity.class);
        myDbAdapter helper;
        helper = new myDbAdapter(getApplicationContext());
        Cursor cursor = helper.getLast();
        cursor.moveToLast();
        int index = cursor.getColumnIndex("medicine_num");
        int x = cursor.getInt(index);
        cursor = helper.getNotify(x);
        cursor.moveToFirst();
        @SuppressLint("Range")
        String medicine = cursor.getString(cursor.getColumnIndex("medicine_name"));
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent((int) Calendar.getInstance().getTimeInMillis(), PendingIntent.FLAG_UPDATE_CURRENT);


        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("please take medicine");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            manager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.medicine)
                .setContentIntent(operation)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setTicker("Hearty365")
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(medicine)
                .setContentText("Please take medicine")
                .setContentInfo("Info");

        manager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}
