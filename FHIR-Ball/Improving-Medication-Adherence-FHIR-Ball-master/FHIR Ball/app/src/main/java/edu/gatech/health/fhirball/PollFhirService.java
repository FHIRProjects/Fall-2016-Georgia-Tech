package edu.gatech.health.fhirball;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static edu.gatech.health.fhirball.DatabaseConstants.*;

import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import org.json.*;

public class PollFhirService extends Service {

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SQLiteDatabase fhirBall = openOrCreateDatabase("fhirBall", MODE_PRIVATE, null);

        DatabaseUtil.ensureMedOrdersTbl(fhirBall);
        DatabaseUtil.ensureNotificationHistoryTbl(fhirBall);
        queryFhirUpdateDb(fhirBall);

        return START_STICKY;
    }

    private void postNotifications(SQLiteDatabase fhirBall) {
        List<Map<DatabaseConstants, Object>> medsFromDb = DatabaseUtil.getMedsFromDb(fhirBall);
        Map<String, Long> lastTaken = DatabaseUtil.getLastTaken(fhirBall);
        for(Map<DatabaseConstants, Object> rowValues : medsFromDb) {
            String medicine = rowValues.get(Medicine).toString();
            long medFrequency = (Long) rowValues.get(Frequency);
            Long medWasTaken = lastTaken.get(medicine);
            if (medWasTaken == null || medWasTaken < System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(medFrequency)) {
                postNotification(fhirBall, medicine);
            }
        }
    }

    private void postNotification(SQLiteDatabase fhirBall, String medicine) {
        Intent intent = new Intent(this, ReasonActivity.class);
        intent.putExtra(ReasonActivity.MEDICATION_KEY, medicine);
        intent.putExtra("closeOnSubmit", true);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        Notification n  = new Notification.Builder(this)
                .setContentTitle("Time to take your meds!")
                .setContentText(medicine)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();
            //    .addAction(R.drawable.icon, "Call", pIntent)
            //    .addAction(R.drawable.icon, "More", pIntent)
            //    .addAction(R.drawable.icon, "And more", pIntent).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify((int) System.currentTimeMillis(), n);
        Log.i("FHIRBALL", "Posting notification for " + medicine);
        DatabaseUtil.registerTakenMed(fhirBall, medicine, MedEncounterReason.NO_RESPONSE_RECEIVED);
    }

    /**
     * Deletes all old entries and uses the new ones it finds on the FHIR server.
     */
    private void queryFhirUpdateDb(final SQLiteDatabase fhirBall) {

        RequestQueue queue = Volley.newRequestQueue(this);

        String fhir = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("fhir", "");
        String patient = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("patient", "");
        String url = fhir + "MedicationOrder?patient=" + patient;
        if(fhir.equals("") || patient.equals("")) {
            Toast.makeText(this, "URL is empty. You must set this value!", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("SERVERDB success", response.toString());
                        System.out.println(response.toString());
                        try {

                            List<String> medications = new ArrayList<String>();
                            List<Integer> dosage = new ArrayList<Integer>();
                            List<Integer> number = new ArrayList<Integer>();

                            //Map<String, Map<String, Integer>> medicationMap = new HashMap<>();

                            JSONArray jEntries = response.getJSONArray("entry");
                            for (int i = 0; i < jEntries.length(); ++i) {
                                JSONObject entry = jEntries.getJSONObject(i);
                                JSONObject resource = entry.getJSONObject("resource");
                                JSONObject medicationConcept = resource.getJSONObject("medicationCodeableConcept");
                                JSONArray codingArray = medicationConcept.getJSONArray("coding");
                                JSONObject coding = codingArray.getJSONObject(0);
                                String medicationName = coding.getString("display");

                                //Map<String, Integer> medicationData = new HashMap<>();
                                //medicationData.put("dosage", 30);
                                //medicationData.put("intake", 4);

                                //medicationMap.put(medicationName, medicationData);

                                medications.add(medicationName);


                                JSONArray dosageInstruction = resource.getJSONArray("dosageInstruction");
                                JSONObject dosageEntry = dosageInstruction.getJSONObject(0);
                                JSONObject dosageQuantity = dosageEntry.getJSONObject("doseQuantity");
                                Integer dose = dosageQuantity.getInt("value");
                                dosage.add(dose);

                                JSONObject doseTimingRepeat = dosageEntry.getJSONObject("timing").getJSONObject("repeat");
                                Integer doseFrequency = doseTimingRepeat.getInt("frequency");
                                Integer frequencyInMins = 1440 / doseFrequency;
                                number.add(frequencyInMins);


                            }

                            int i = 0;
                            fhirBall.execSQL("DELETE FROM " + MedOrders);
                            for (String medication: medications) {
                                //System.out.println(medication);
                                //System.out.println("*");
                                fhirBall.execSQL("INSERT INTO " + MedOrders + " VALUES('" + medication + "','" + dosage.get(i).toString() + " mg', " + number.get(0).toString() + ");");
                                ++i;
                            }
                            postNotifications(fhirBall);

                        } catch (JSONException e) {
                            postNotifications(fhirBall);
                            Log.w("FHIR", "Something went wrong querying FHIR", e);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVERDB error", error.toString());
                    }

                });

        queue.add(jsObjRequest);


    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}