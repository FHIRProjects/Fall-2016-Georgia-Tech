package edu.gatech.mass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.List;

import edu.gatech.mass.database.DataSource;
import edu.gatech.mass.fhir.FhirSingleton;

import edu.gatech.mass.model.MedicalRecordConnection;
import edu.gatech.mass.model.MedicationOrder;

import edu.gatech.mass.parsers.MedicationOrderJSONParser;
import edu.gatech.mass.sample.SampleMedicalOrderDataProvider;

public class MedicationOrdersActivity extends AppCompatActivity {

    FhirSingleton fhir;
    DataSource mDataSource;
    List<MedicationOrder> medicationOrderList = SampleMedicalOrderDataProvider.medicationOrderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_orders);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mDataSource = new DataSource(this);
        mDataSource.open();
        List<MedicalRecordConnection> medicalConnections = mDataSource.getAllMedicalRecordConnections();
        if (medicalConnections.isEmpty()) {
            // TODO: Go to Medical Record Connections Settings
            String message = "Please setup your medical record connection";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        }

        MedicalRecordConnection connection = medicalConnections.get(0);

        List<MedicationOrder> medicationOrders;
        String url = connection.getUrl() + "MedicationOrder?patient._id=" + connection.getPatientId();
        requestData(url);

        // proceed to schedule setting activity when clicking on medication
        ListView list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.nameText);
                String medicationText = textView.getText().toString();

                Intent intent = new Intent(view.getContext(), ScheduleSettingActivity.class);
                intent.putExtra("medicationText", medicationText);
                startActivity(intent);
            }
            @SuppressWarnings("unused")
            public void onClick(View v){
            };
        });
    }

    public void requestData(String url) {
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        List<MedicationOrder> medicationOrderList = MedicationOrderJSONParser.parseFeed(response.toString());

                        for (MedicationOrder med : medicationOrderList) {
                            mDataSource.createMedicalOrder(med);
                        }

                        updateDisplay(medicationOrderList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage;
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            errorMessage = "Could not connect to the server. Contact your administrator";
                        } else if (error instanceof AuthFailureError) {
                            errorMessage = "Could not authenticate your user credentials";
                        } else if (error instanceof ServerError) {
                            errorMessage = "Server Error";
                        } else if (error instanceof NetworkError) {
                            errorMessage = "Network Error. Try again later";
                        } else if (error instanceof ParseError) {
                            errorMessage = "Bad Response";
                        } else {
                            errorMessage = "Unknown Error";
                        }

                        Toast.makeText(MedicationOrdersActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });

        fhir.getInstance(this).addToRequestQueue(jsonObjRequest);
    }

    protected void updateDisplay(List<MedicationOrder> medicationOrders) {

        MedicationOrderListAdapter adapter = new MedicationOrderListAdapter(this, R.layout.list_medication_order, medicationOrders);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);
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
