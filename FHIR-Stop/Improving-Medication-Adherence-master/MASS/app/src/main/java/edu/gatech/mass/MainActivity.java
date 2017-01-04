package edu.gatech.mass;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;

import edu.gatech.mass.database.DataSource;
import edu.gatech.mass.model.MedicationOrder;

public class MainActivity extends AppCompatActivity {
    AlarmReceiver alarm = new AlarmReceiver();
    private CoordinatorLayout coordinatorLayout;

    DataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        initDb();

        // MEDICATION REMINDER TESTING CODE - DO NOT DELETE
        // this should also serve as a reference for setting up future alarms
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.add(Calendar.SECOND, 10);
        //alarm.setTime(calendar);
        //alarm.setMedicationText("medication 1\nmedication 2");
        //alarm.setAlarm(this);
    }

    protected void initDb() {
        mDataSource = new DataSource(this);
        mDataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDataSource.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_settings:
                Snackbar.make(coordinatorLayout,
                        "You selected settings", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            case R.id.action_medical_records:
                intent = new Intent(this, MedicalRecordConnectionActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void navMedicationOrdersView(View view) {
            Intent intent = new Intent(this, MedicationOrdersActivity.class);
            startActivity(intent);
    }

    public void navMedicalRecordConnectionView(View view) {
        Intent intent = new Intent(this, MedicalRecordConnectionActivity.class);
        startActivity(intent);
    }

    public void navQuestionnaireView(View view) {
        Intent intent = new Intent(this, DemographicsActivity.class);
        startActivity(intent);
    }
}
