package edu.gatech.health.fhirball;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class AlarmService extends Service {
    Alarm alarm = new Alarm();
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarm.setAlarm(this);
        Toast.makeText(this, "Refreshing Meds from FHIR DB", Toast.LENGTH_SHORT).show(); // For example
        this.startService(new Intent(this, PollFhirService.class));
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        alarm.setAlarm(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}