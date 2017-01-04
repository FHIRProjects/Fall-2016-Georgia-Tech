package edu.gatech.mass;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

import edu.gatech.mass.database.DemographicQuestionnaireTable;

public class DemographicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographics_questionnaire);

        final EditText etPrimaryLanguage = (EditText) findViewById(R.id.etPrimaryLanguage);
        final EditText etEducation = (EditText) findViewById(R.id.etEducation);
        final EditText etPeopleInHousehold = (EditText) findViewById(R.id.etPeopleInHousehold);
        final EditText etJobs = (EditText) findViewById(R.id.etJobs);
        final EditText etImpairment = (EditText) findViewById(R.id.etImpairment);
        final EditText etProblemsSwallowing = (EditText) findViewById(R.id.etProblemsSwallowing);
        final EditText etNearestPharmacy = (EditText) findViewById(R.id.etNearestPharmacy);
        final EditText etTreatmentInterference = (EditText) findViewById(R.id.etTreatmentInterference);
        final EditText etProviders = (EditText) findViewById(R.id.etProviders);
        final EditText etMedicationCost = (EditText) findViewById(R.id.etMedicationCost);
        final EditText etVehicles = (EditText) findViewById(R.id.etVehicles);

        Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                ContentValues values = new ContentValues(12);
                values.put(DemographicQuestionnaireTable.COLUMN_TIME_STAMP, calendar.toString());
                values.put(DemographicQuestionnaireTable.COLUMN_PRIMARY_LANGUAGE, etPrimaryLanguage.getText().toString());
                values.put(DemographicQuestionnaireTable.COLUMN_EDUCATION, etEducation.getText().toString());
                values.put(DemographicQuestionnaireTable.COLUMN_PEOPLE_IN_HOUSEHOLD, etPeopleInHousehold.getText().toString());
                values.put(DemographicQuestionnaireTable.COLUMN_JOBS, etJobs.getText().toString());
                values.put(DemographicQuestionnaireTable.COLUMN_IMPAIRMENT, etImpairment.getText().toString());
                values.put(DemographicQuestionnaireTable.COLUMN_PROBLEMS_SWALLOWING, etProblemsSwallowing.getText().toString());
                values.put(DemographicQuestionnaireTable.COLUMN_NEAREST_PHARMACY, etNearestPharmacy.getText().toString());
                values.put(DemographicQuestionnaireTable.COLUMN_TREATMENT_INTERFERENCE, etTreatmentInterference.getText().toString());
                values.put(DemographicQuestionnaireTable.COLUMN_PROVIDERS, etProviders.getText().toString());
                values.put(DemographicQuestionnaireTable.COLUMN_MEDICATION_COST, etMedicationCost.getText().toString());
                values.put(DemographicQuestionnaireTable.COLUMN_VEHICLES, etVehicles.getText().toString());

                finish();
            }
        });


    }

}