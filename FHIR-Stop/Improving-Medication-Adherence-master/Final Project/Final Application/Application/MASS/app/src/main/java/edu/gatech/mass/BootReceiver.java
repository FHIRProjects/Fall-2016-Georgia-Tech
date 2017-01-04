/*
 * revised from https://developer.android.com/training/scheduling/alarms.html
 */

package edu.gatech.mass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This BroadcastReceiver automatically (re)starts the medication alarm when the device is
 * rebooted. This receiver is set to be disabled (android:enabled="false") in the
 * application's manifest file. When the app sets the alarm, the receiver is enabled.
 * When the app cancels the alarm, the receiver is disabled, so that rebooting the
 * device will not trigger this receiver.
 */
public class BootReceiver extends BroadcastReceiver {
    AlarmReceiver alarm = new AlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarm.setAlarm(context);
        }
    }
}