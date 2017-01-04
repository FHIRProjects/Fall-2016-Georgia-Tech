package edu.gatech.mass.parsers;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.mass.model.Patient;

public class PatientJSONParser {

    public static List<Patient> parseFeed(String content) {
        try {
            JSONObject rootObj = new JSONObject(content);
            JSONArray ar = rootObj.getJSONArray("entry");
            List<Patient> patientList = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                JSONObject entry = ar.getJSONObject(i);
                JSONObject obj = entry.getJSONObject("resource");
                Patient patient = new Patient();

                patient.setId(obj.getString("id"));
                patient.setActive(obj.getBoolean("active"));

                JSONArray namesArray = obj.getJSONArray("name");
                JSONObject nameObj = namesArray.getJSONObject(0);
                patient.setFamilyName(nameObj.getJSONArray("family").getString(0));
                patient.setGivenName(nameObj.getJSONArray("given").getString(0));

                patient.setGender(obj.getString("gender"));
                patient.setBirthDate(obj.getString("birthDate"));

                JSONArray addressArray = obj.getJSONArray("address");
                JSONObject addressObj = addressArray.getJSONObject(0);
                if (addressObj.getString("use").equals("home")) {
                    patient.setAddress1(addressObj.getJSONArray("line").getString(0));
                    patient.setCity(addressObj.getString("city"));
                    patient.setState(addressObj.getString("state"));
                    patient.setPostalCode(addressObj.getString("postalCode"));
                }
                patientList.add(patient);
            }
            return patientList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
