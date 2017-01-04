package edu.gatech.health.fhirball;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FirstActivity extends Activity {

    // private final String FHIR_URL = "http://polaris.i3l.gatech.edu:8080/fhir-omopv5/base/";
    // private final String FHIR_URL = "http://polaris.i3l.gatech.edu:8080/gt-fhir-webapp" // RW version

    private final String FHIR_URL = "http://52.72.172.54:8080/fhir/baseDstu2/";

    // private final String PATIENT_ID = "523050";
    private final String PATIENT_ID = "2019307";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);

        Button submit = (Button)findViewById(R.id.first_submit_btn);
        final EditText fhir = (EditText)findViewById(R.id.fhir_edit_text);
        final EditText patient = (EditText)findViewById(R.id.patient_edit_text);

        fhir.setText(FHIR_URL);
        patient.setText(PATIENT_ID);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
                if (firstrun){
                    // Save the state
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit()
                            .putBoolean("firstrun", false)
                            .putString("fhir", fhir.getText().toString())
                            .putString("patient", patient.getText().toString())
                            .commit();
                } else {
                    Toast.makeText(getApplicationContext(), "FHIR instance and patient ID have already been set", Toast.LENGTH_LONG).show();
                }
                startService(new Intent(FirstActivity.this, AlarmService.class));
                startActivity(new Intent(FirstActivity.this, MainActivity.class));
            }
        });
    }
}
