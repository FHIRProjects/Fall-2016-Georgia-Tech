package edu.gatech.mass;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.List;

import edu.gatech.mass.database.DataSource;
import edu.gatech.mass.fhir.FhirSingleton;
import edu.gatech.mass.model.*;
import edu.gatech.mass.model.Patient;

import edu.gatech.mass.parsers.PatientJSONParser;
import edu.gatech.mass.sample.SampleMedicalRecordConnectionDataProvider;

public class MedicalRecordConnectionActivity extends AppCompatActivity {

    TextView medicalRecordConnectionId;
    EditText medicalRecordConnectionName;
    EditText patientId;
    EditText userName;
    EditText password;
    EditText url;
    FhirSingleton fhir;

    List<MedicalRecordConnection> medicalRecordConnectionList = SampleMedicalRecordConnectionDataProvider.medicalRecordConnectionList;
    DataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record_connection);

        mDataSource = new DataSource(this);
        mDataSource.open();
        mDataSource.seedDatabaseWithMedicalConnections(medicalRecordConnectionList);

        // Later we may want more than one connection
        // But for now we just support one.
        MedicalRecordConnection connection = mDataSource.getAllMedicalRecordConnections().get(0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MedicalRecordConnection connection = getActivityMedicalRecordConnectionView();
                mDataSource.updateMedicalRecordConnection(connection);
                Snackbar.make(view, "Connection Saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        setActivityMedicalRecordConnectionView(connection);
    }

    public void testMedicalRecordConnection(View view) {
        EditText patientIdEditText = (EditText) findViewById(R.id.patientId);
        final String patientId = patientIdEditText.getText().toString();

        EditText urlEditText = (EditText) findViewById(R.id.url);
        String url = urlEditText.getText().toString() + "Patient?_id=" + patientId;

        final Button testButton = (Button) findViewById(R.id.testButton);
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<Patient> patientList = PatientJSONParser.parseFeed(response.toString());
                Patient patient = patientList.get(0);
                if (patient.getId().equals(patientId)) {
                    testButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                    testButton.setText("Success");
                }
                else {
                    testButton.setText("Failed");
                    testButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                testButton.setText("Unable to connect");
                testButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            }
        });

        FhirSingleton.getInstance(this).addToRequestQueue(jsonObjRequest);
    }

    protected MedicalRecordConnection getActivityMedicalRecordConnectionView() {
        MedicalRecordConnection connection = new MedicalRecordConnection();

        medicalRecordConnectionId = (TextView) findViewById(R.id.medicalRecordConnectionId);
        connection.setMedicalRecordConnectionId(medicalRecordConnectionId.getText().toString());

        medicalRecordConnectionName = (EditText) findViewById(R.id.medicalRecordConnectionName);
        connection.setMedicalRecordConnectionName(medicalRecordConnectionName.getText().toString());

        patientId = (EditText) findViewById(R.id.patientId);
        connection.setPatientId(patientId.getText().toString());

        userName = (EditText) findViewById(R.id.userName);
        connection.setUserName(userName.getText().toString());

        password = (EditText) findViewById(R.id.password);
        connection.setPassword(password.getText().toString());

        url = (EditText) findViewById(R.id.url);
        connection.setUrl(url.getText().toString());

        return connection;
    }

    protected void setActivityMedicalRecordConnectionView(MedicalRecordConnection connection) {
        medicalRecordConnectionId = (TextView) findViewById(R.id.medicalRecordConnectionId);
        medicalRecordConnectionId.setText(connection.getMedicalRecordConnectionId());

        medicalRecordConnectionName = (EditText) findViewById(R.id.medicalRecordConnectionName);
        medicalRecordConnectionName.setText(connection.getMedicalRecordConnectionName());

        patientId = (EditText) findViewById(R.id.patientId);
        patientId.setText(connection.getPatientId());

        userName = (EditText) findViewById(R.id.userName);
        userName.setText(connection.getUserName());

        password = (EditText) findViewById(R.id.password);
        password.setText(connection.getPassword());

        url = (EditText) findViewById(R.id.url);
        url.setText(connection.getUrl());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
