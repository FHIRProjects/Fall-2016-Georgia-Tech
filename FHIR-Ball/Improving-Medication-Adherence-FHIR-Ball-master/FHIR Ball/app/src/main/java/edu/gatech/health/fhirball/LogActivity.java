package edu.gatech.health.fhirball;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.*;
import static edu.gatech.health.fhirball.DatabaseUtil.getLog;

public class LogActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_layout);

        setListView();
    }

    private void setListView() {
        ListView lv = (ListView) findViewById(R.id.log_list);

        SQLiteDatabase fhirBall = openOrCreateDatabase("fhirBall", MODE_PRIVATE, null);

        final List<Administration> administrations = getLog(fhirBall);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, administrations) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(administrations.get(position).getMedicine());
                text2.setText(administrations.get(position).getReason() + " @ " + new Date(administrations.get(position).getTime()));

                return view;
            }
        };

        lv.setAdapter(arrayAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setListView();
    }

}
