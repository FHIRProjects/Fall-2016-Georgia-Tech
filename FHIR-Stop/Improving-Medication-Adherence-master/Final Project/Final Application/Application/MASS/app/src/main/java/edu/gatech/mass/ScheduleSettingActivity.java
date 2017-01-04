package edu.gatech.mass;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import java.util.Calendar;

import edu.gatech.mass.database.SurveyTable;

public class ScheduleSettingActivity extends AppCompatActivity {

    AlarmReceiver alarm = new AlarmReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_setting);

        // set medication name text
        String medicationText = getIntent().getStringExtra("medicationText");
        TextView tvSet = (TextView) findViewById(R.id.medicationNames);
        tvSet.setText(medicationText);

        TextView cancel = (TextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView save = (TextView) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tvGet = (TextView) findViewById(R.id.medicationNames);
                String medicationText = tvGet.getText().toString();

                EditText etGet = (EditText) findViewById(R.id.dosageTime);
                String time = etGet.getText().toString();
                int hours = Integer.parseInt(time.substring(0,2));
                int minutes = Integer.parseInt(time.substring(3,5));

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hours);
                calendar.set(Calendar.MINUTE, minutes);
                calendar.set(Calendar.SECOND, 0);

                alarm.setTime(calendar);
                alarm.setMedicationText(medicationText);
                alarm.setAlarm(view.getContext());

                finish();
            }
        });
    }
}
