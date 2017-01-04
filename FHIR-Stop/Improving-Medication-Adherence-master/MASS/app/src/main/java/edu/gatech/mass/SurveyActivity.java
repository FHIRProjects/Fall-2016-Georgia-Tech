package edu.gatech.mass;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import edu.gatech.mass.database.SurveyTable;

public class SurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_survey);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                ContentValues values = new ContentValues(7);
                values.put(SurveyTable.COLUMN_TIME_STAMP, calendar.toString());
                values.put(SurveyTable.COLUMN_TAKEN, 0);
                values.put(SurveyTable.COLUMN_LOST_MEDICATION, 1);
                values.put(SurveyTable.COLUMN_INCONVENIENT_TIME, 0);
                values.put(SurveyTable.COLUMN_DIFFICULT_TO_SWALLOW, 0);
                values.put(SurveyTable.COLUMN_WAY_IT_FEELS, 0);
                values.put(SurveyTable.COLUMN_DONT_NEED_IT, 0);

                finish();
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                ContentValues values = new ContentValues(7);
                values.put(SurveyTable.COLUMN_TIME_STAMP, calendar.toString());
                values.put(SurveyTable.COLUMN_TAKEN, 0);
                values.put(SurveyTable.COLUMN_LOST_MEDICATION, 0);
                values.put(SurveyTable.COLUMN_INCONVENIENT_TIME, 1);
                values.put(SurveyTable.COLUMN_DIFFICULT_TO_SWALLOW, 0);
                values.put(SurveyTable.COLUMN_WAY_IT_FEELS, 0);
                values.put(SurveyTable.COLUMN_DONT_NEED_IT, 0);

                finish();
            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                ContentValues values = new ContentValues(7);
                values.put(SurveyTable.COLUMN_TIME_STAMP, calendar.toString());
                values.put(SurveyTable.COLUMN_TAKEN, 0);
                values.put(SurveyTable.COLUMN_LOST_MEDICATION, 0);
                values.put(SurveyTable.COLUMN_INCONVENIENT_TIME, 0);
                values.put(SurveyTable.COLUMN_DIFFICULT_TO_SWALLOW, 1);
                values.put(SurveyTable.COLUMN_WAY_IT_FEELS, 0);
                values.put(SurveyTable.COLUMN_DONT_NEED_IT, 0);

                finish();
            }
        });

        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                ContentValues values = new ContentValues(7);
                values.put(SurveyTable.COLUMN_TIME_STAMP, calendar.toString());
                values.put(SurveyTable.COLUMN_TAKEN, 0);
                values.put(SurveyTable.COLUMN_LOST_MEDICATION, 0);
                values.put(SurveyTable.COLUMN_INCONVENIENT_TIME, 0);
                values.put(SurveyTable.COLUMN_DIFFICULT_TO_SWALLOW, 0);
                values.put(SurveyTable.COLUMN_WAY_IT_FEELS, 1);
                values.put(SurveyTable.COLUMN_DONT_NEED_IT, 0);

                finish();
            }
        });

        Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                ContentValues values = new ContentValues(7);
                values.put(SurveyTable.COLUMN_TIME_STAMP, calendar.toString());
                values.put(SurveyTable.COLUMN_TAKEN, 0);
                values.put(SurveyTable.COLUMN_LOST_MEDICATION, 0);
                values.put(SurveyTable.COLUMN_INCONVENIENT_TIME, 0);
                values.put(SurveyTable.COLUMN_DIFFICULT_TO_SWALLOW, 0);
                values.put(SurveyTable.COLUMN_WAY_IT_FEELS, 0);
                values.put(SurveyTable.COLUMN_DONT_NEED_IT, 1);

                finish();
            }
        });
    }
}
