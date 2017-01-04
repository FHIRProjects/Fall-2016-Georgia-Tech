package edu.gatech.health.fhirball;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static edu.gatech.health.fhirball.DatabaseConstants.MedOrders;

public class ReasonActivity extends Activity {

    public static final String MEDICATION_KEY = "Med";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reason_layout);
        Intent intent = getIntent();
        final String medication = intent.getStringExtra(ReasonActivity.MEDICATION_KEY);
        final boolean closeOnSubmit = intent.getBooleanExtra("closeOnSubmit", false);


        final RadioGroup reasonRadioGroup = (RadioGroup)findViewById(R.id.reason_radio_grp);

        for (MedEncounterReason reason : MedEncounterReason.getUserAvailableReasons()) {
            RadioButton rb  = new RadioButton(this);
            rb.setText(reason.toString());
            reasonRadioGroup.addView(rb);
        }

        TextView reasonText = (TextView)findViewById(R.id.reason_txt);
        reasonText.setText("Medication: " + medication + "" +
                "\nSelect whether you took the medication or specify the reason for not." );
        Button medsButton = (Button)findViewById(R.id.reason_submit_btn);

        medsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonID = reasonRadioGroup.getCheckedRadioButtonId();
                if(radioButtonID == -1) {
                    Toast.makeText(getApplicationContext(), "Please first select a reason for not taking " + medication, Toast.LENGTH_SHORT).show();
                } else {
                    View radioButton = reasonRadioGroup.findViewById(radioButtonID);
                    int idx = reasonRadioGroup.indexOfChild(radioButton);
                    RadioButton r = (RadioButton) reasonRadioGroup.getChildAt(idx);
                    MedEncounterReason reason = MedEncounterReason.fromString(r.getText().toString());
                    registerAdministration(medication, reason);
                    Toast.makeText(getApplicationContext(), medication + " Selected:" + r.getText().toString(), Toast.LENGTH_SHORT).show();
                    if(closeOnSubmit) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }
            }
        });


    }

    private void registerAdministration(String medication, MedEncounterReason reason) {
        SQLiteDatabase fhirBall = openOrCreateDatabase("fhirBall", MODE_PRIVATE, null);
        DatabaseUtil.registerTakenMed(fhirBall, medication, reason);
        uploadToFhir(medication, reason);
    }



    public JSONObject generateMedStatementJSON(String forMedication, Boolean taken, String reasonNotTaken) {
        JSONObject jsonBody = new JSONObject();
        final String patientID = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("patient", "");
        String badFormat = sdf.format(new Date());
        String currentTimeStamp = badFormat.substring(0, badFormat.length() - 2) + ":" + badFormat.substring(badFormat.length() -2);
        
        Log.i("FHIR", "String formatted date is " + currentTimeStamp);
        final String ndcMedCode = "00339647811"; //This is hard-coded currently, yet should be written in the local MedOrders table and extracted along with medication name
        final String medText = forMedication;
        final String takenSegment = "\"wasNotTaken\":" + (taken ? "false, " : "true, \"reasonNotTaken\": [{\"coding\": [{\"system\": \"http://hl7.org/fhir/reason-medication-not-given\", \"code\": \""+reasonNotTaken+"\"}]}],");
        try {
            jsonBody = new JSONObject("{\"resourceType\":\"MedicationStatement\", \"patient\":{" +
                                        "\"reference\":\"Patient/" + patientID + "\"}, " +
                                        "\"dateAsserted\":\"" + currentTimeStamp + "\", " +
                                        takenSegment +
                                        "\"status\":\"active\", " +
                                        "\"medicationCodeableConcept\":{\"coding\":[{\"system\":\"http://hl7.org/fhir/sid/ndc\", " +
                                        "\"code\":\"" + ndcMedCode + "\", " +
                                        "\"display\":\"" + medText + "\"}], " +
                                        "\"text\":\"" + medText + "\"}, " +
                                        "\"dosage\":{\"quantityQuantity\":{\"value\":1},\"timing\":{\"repeat\":{\"frequency\":1,\"period\":1,\"periodUnits\":\"m\"}}}}");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonBody;
    }

    private void uploadToFhir(String medication, MedEncounterReason reason) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        String fhir = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("fhir", "");
        final String url = fhir + "MedicationStatement?_format=json&_pretty=true";
        final JSONObject jsonBody;

        if (reason == MedEncounterReason.TAKEN) {
            jsonBody = generateMedStatementJSON(medication, true, "");
        } else {
            jsonBody = generateMedStatementJSON(medication, false, reason.getFhirCode());
            // TODO upload with reason code: reason.getFhirCode()
        }
        String jsonString = jsonBody.toString();
        Log.i("FHIR", jsonString);
            JsonObjectRequest req = new JsonObjectRequest(url, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.i("FHIR", response.toString(4));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    JsonObjectRequest req2 = new JsonObjectRequest(url, jsonBody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.i("FHIR", response.toString(4));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Could not upload because the server is either down or is read only. Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.i("FHIR", "Failed to upload the second time", error);
                        }
                    });
                    queue.add(req2);

                    Log.i("FHIR", "Failed to upload the first time", error);
                }
            });

            queue.add(req);


    }


}
