/*
 * revised from https://developer.android.com/training/scheduling/alarms.html
 */

package edu.gatech.mass;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Random;
import java.util.Calendar;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent
 * and then starts the IntentService {@code SchedulingService} to do some work.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    private Calendar calendar;
    private String medicationText;

    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        String medicationTextReceiver = intent.getStringExtra("medicationText");

        // Start the service, keeping the device awake while it is launching.
        Intent service = new Intent(context, SchedulingService.class);
        service.putExtra("medicationText", medicationTextReceiver);
        System.out.println(medicationTextReceiver);
        //PendingIntent sender = PendingIntent.getBroadcast(AlarmController.this,0, intent, 0);
        startWakefulService(context, service);
    }

    // BEGIN_INCLUDE(set_alarm)
    /**
     * Sets a repeating alarm that runs once a day at approximately 8:30 a.m. When the
     * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     * @param context
     */
    public void setAlarm(Context context) {
        Random rand = new Random();
        int uniqueID = rand.nextInt(2000000000);

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("medicationText", medicationText);
        alarmIntent = PendingIntent.getBroadcast(context, uniqueID, intent, 0);

        // Set the alarm to repeat once a day.
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), alarmIntent);

        // Enable {@code BootReceiver} to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        System.out.println("DEBUG: alarm set");
    }
    // END_INCLUDE(set_alarm)

    /**
     * Cancels the alarm.
     * @param context
     */
    // BEGIN_INCLUDE(cancel_alarm)
    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }

        // Disable {@code BootReceiver} so that it doesn't automatically restart the
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
    // END_INCLUDE(cancel_alarm)

    public void setTime(Calendar calendar) {
        this.calendar = calendar;
    }

    public void setMedicationText(String medicationText) {
        this.medicationText = medicationText;
    }
}
