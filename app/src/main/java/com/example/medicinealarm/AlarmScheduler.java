package com.example.medicinealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

public class AlarmScheduler {
    public void setAlarm(Context context, long alarmTime) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getReminderPendingIntent(context);


        if (Build.VERSION.SDK_INT >= 23) {

            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        } else if (Build.VERSION.SDK_INT >= 19) {

            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        } else {

            manager.set(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        }
    }

    public void setRepeatAlarm(Context context, long alarmTime, long RepeatTime) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getReminderPendingIntent(context);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, RepeatTime, operation);


    }

    public void cancelAlarm(Context context) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getReminderPendingIntent(context);

        manager.cancel(operation);

    }
}
