package edu.gatech.health.fhirball;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static edu.gatech.health.fhirball.DatabaseConstants.Frequency;
import static edu.gatech.health.fhirball.DatabaseConstants.Medicine;

public class ScheduleActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_layout);

        final ListView lv = (ListView) findViewById(R.id.sched_list);

        ArrayAdapter arrayAdapter = getArrayAdapter(getNextAdministrations());

        lv.setAdapter(arrayAdapter);

    }

    private List<Map.Entry<String, Long>> getNextAdministrations() {
        final List<Map.Entry<String, Long>> nextAdministration = determineUpcoming();

        // sort so that soonest administration is on top
        Collections.sort(nextAdministration, new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> lhs, Map.Entry<String, Long> rhs) {
                return Long.compare(lhs.getValue(), rhs.getValue());
            }
        });
        return nextAdministration;
    }

    private ArrayAdapter getArrayAdapter(final List<Map.Entry<String, Long>> nextAdministration) {
        return new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, nextAdministration) {
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                    text1.setText(nextAdministration.get(position).getKey().toString());
                    Long nextAdministationMillis = nextAdministration.get(position).getValue();
                    long millisFromNow = nextAdministationMillis - System.currentTimeMillis();
                    long minutesFromNow = TimeUnit.MILLISECONDS.toMinutes(millisFromNow);
                    long hoursFromNow = TimeUnit.MILLISECONDS.toHours(millisFromNow);
                    String prettyTimeFromNow = String.format("%d hrs, %02d min, %02d sec",
                            hoursFromNow,
                            minutesFromNow - TimeUnit.HOURS.toMinutes(hoursFromNow),
                            TimeUnit.MILLISECONDS.toSeconds(millisFromNow) - TimeUnit.MINUTES.toSeconds(minutesFromNow)
                    );
                    text2.setText(new Date(nextAdministationMillis).toString() + "\nDue in: " + prettyTimeFromNow);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ScheduleActivity.this, ReasonActivity.class);
                            i.putExtra(ReasonActivity.MEDICATION_KEY, nextAdministration.get(position).getKey().toString());
                            ScheduleActivity.this.startActivity(i);
                        }
                    });
                    return view;
                }
            };
    }

    private List<Map.Entry<String, Long>> determineUpcoming() {
        final List<Map.Entry<String, Long>> nextAdministration = new ArrayList<>();
        SQLiteDatabase fhirBall = openOrCreateDatabase("fhirBall", MODE_PRIVATE, null);

        List<Map<DatabaseConstants, Object>> medsFromDb = DatabaseUtil.getMedsFromDb(fhirBall);
        Map<String, Long> lastTaken = DatabaseUtil.getLastTaken(fhirBall);
        for(Map<DatabaseConstants, Object> rowValues : medsFromDb) {
            String medicine = rowValues.get(Medicine).toString();
            long medFrequency = (Long) rowValues.get(Frequency);
            Long medWasTaken = lastTaken.get(medicine);
            if (medWasTaken != null && medWasTaken > System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(medFrequency)) {
                nextAdministration.add(new AbstractMap.SimpleEntry<>(medicine, medWasTaken + TimeUnit.MINUTES.toMillis(medFrequency)));
            }
        }
        return nextAdministration;
    }

    @Override
    public void onResume() {
        super.onResume();
        final ListView lv = (ListView) findViewById(R.id.sched_list);
        ArrayAdapter arrayAdapter = getArrayAdapter(getNextAdministrations());
        lv.setAdapter(arrayAdapter);
    }
}
