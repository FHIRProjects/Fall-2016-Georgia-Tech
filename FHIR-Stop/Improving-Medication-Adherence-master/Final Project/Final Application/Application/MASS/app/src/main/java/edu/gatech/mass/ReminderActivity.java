package edu.gatech.mass;
import edu.gatech.mass.database.SurveyTable;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.Window;
import android.view.WindowManager;

import java.util.Calendar;

import android.content.ContentValues;

import java.util.UUID;

public class ReminderActivity extends AppCompatActivity {

    AlarmReceiver alarm = new AlarmReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        System.out.println("DEBUG: added reminder window flags");

        setContentView(R.layout.ab_activity_reminder);

        String medicationText = getIntent().getStringExtra("medicationText");
        TextView tvSet = (TextView) findViewById(R.id.medicationNames);
        tvSet.setText(medicationText);

        System.out.println("DEBUG: set reminder content view");

        Button buttonSnooze = (Button) findViewById(R.id.buttonSnooze);
        buttonSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tvGet = (TextView) findViewById(R.id.medicationNames);
                String medicationText = tvGet.getText().toString();

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.MINUTE, 15);

                alarm.setTime(calendar);
                alarm.setMedicationText(medicationText);
                alarm.setAlarm(view.getContext());

                finish();
            }
        });

        Button buttonTake = (Button) findViewById(R.id.buttonTake);
        buttonTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                ContentValues values = new ContentValues(7);
                values.put(SurveyTable.COLUMN_TIME_STAMP, calendar.toString());
                values.put(SurveyTable.COLUMN_TAKEN, 1);
                values.put(SurveyTable.COLUMN_LOST_MEDICATION, 0);
                values.put(SurveyTable.COLUMN_INCONVENIENT_TIME, 0);
                values.put(SurveyTable.COLUMN_DIFFICULT_TO_SWALLOW, 0);
                values.put(SurveyTable.COLUMN_WAY_IT_FEELS, 0);
                values.put(SurveyTable.COLUMN_DONT_NEED_IT, 0);

                finish();
            }
        });

        Button buttonSkip = (Button) findViewById(R.id.buttonSkip);
        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SurveyActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }
}
