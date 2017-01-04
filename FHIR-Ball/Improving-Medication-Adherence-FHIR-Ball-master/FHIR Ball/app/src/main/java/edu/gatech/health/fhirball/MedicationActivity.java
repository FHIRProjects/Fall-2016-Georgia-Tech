package edu.gatech.health.fhirball;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.gatech.health.fhirball.DatabaseConstants.*;
import static edu.gatech.health.fhirball.DatabaseConstants.MedOrders;
import static edu.gatech.health.fhirball.DatabaseUtil.getMedsFromDb;

public class MedicationActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication_layout);

        displayDataFromDb();
    }

    private void displayDataFromDb() {
        ListView lv = (ListView) findViewById(R.id.med_list);

        SQLiteDatabase fhirBall = openOrCreateDatabase("fhirBall", MODE_PRIVATE, null);

        final List<Map<DatabaseConstants, Object>> meds = getMedsFromDb(fhirBall);

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, meds) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(meds.get(position).get(Medicine).toString());
                text2.setText(meds.get(position).get(Description).toString());

                return view;
            }
        };

        lv.setAdapter(arrayAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        displayDataFromDb();
    }
}
