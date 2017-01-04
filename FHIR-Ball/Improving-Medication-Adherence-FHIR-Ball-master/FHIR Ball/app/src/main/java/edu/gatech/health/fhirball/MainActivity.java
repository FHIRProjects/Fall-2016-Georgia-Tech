package edu.gatech.health.fhirball;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun){
            MainActivity.this.startActivity(new Intent(MainActivity.this, FirstActivity.class));
        } else {
            startService(new Intent(this, AlarmService.class));
        }
        Resources ressources = getResources();
        TabHost tabHost = getTabHost();

        // Android tab
        Intent intentLog = new Intent().setClass(this, LogActivity.class);
        TabHost.TabSpec tabSpecLog = tabHost
                .newTabSpec("Log")
                .setIndicator("Log")
                .setContent(intentLog);

        // Apple tab
        Intent intentSchedule = new Intent().setClass(this, ScheduleActivity.class);
        TabHost.TabSpec tabSpecSchedule = tabHost
                .newTabSpec("Schedule")
                .setIndicator("Schedule")
                .setContent(intentSchedule);

        // Windows tab
        Intent intentMedication = new Intent().setClass(this, MedicationActivity.class);
        TabHost.TabSpec tabSpecMedication = tabHost
                .newTabSpec("Medication")
                .setIndicator("Medication")
                .setContent(intentMedication);

        // add all tabs
        tabHost.addTab(tabSpecMedication);
        tabHost.addTab(tabSpecSchedule);
        tabHost.addTab(tabSpecLog);

        //set Windows tab as default (zero based)
        tabHost.setCurrentTab(1);

    }
}
