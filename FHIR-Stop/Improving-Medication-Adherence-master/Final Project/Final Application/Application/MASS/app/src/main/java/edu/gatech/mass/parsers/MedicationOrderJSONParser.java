package edu.gatech.mass.parsers;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.mass.model.MedicationOrder;


public class MedicationOrderJSONParser {

    public static List<MedicationOrder> parseFeed(String content) {
        try {
            JSONObject rootObj = new JSONObject(content);
            JSONArray entries = rootObj.getJSONArray("entry");
            List<MedicationOrder> medicationOrderList = new ArrayList<>();

            for (int i = 0; i < entries.length(); i++) {
                JSONObject entry = entries.getJSONObject(i);
                JSONObject resource = entry.getJSONObject("resource");
                MedicationOrder medicationOrder = new MedicationOrder();

                // Id
                medicationOrder.setId(resource.getString("id"));

                // Prescriber Details
                JSONObject prescriber = resource.getJSONObject("prescriber");
                String prescriberId = prescriber.getString("reference").split("/")[1];
                String prescriberName = prescriber.getString("display");
                medicationOrder.setPrescriberId(prescriberId);
                medicationOrder.setPrescriberName(prescriberName);

                // Dosage Details
                JSONArray dosageInstructionArray = resource.getJSONArray("dosageInstruction");
                JSONObject dosageInstruction = dosageInstructionArray.getJSONObject(0);
                JSONObject doseQuantity = dosageInstruction.getJSONObject("doseQuantity");
                medicationOrder.setDoseUnit(doseQuantity.getString("unit"));
                medicationOrder.setDoseQuantity(doseQuantity.getDouble("value"));

                // Medication Details
                JSONObject dispenseRequest = resource.getJSONObject("dispenseRequest");
                JSONObject medicationCodeableConcept = dispenseRequest.getJSONObject("medicationCodeableConcept");
                JSONArray codingArray = medicationCodeableConcept.getJSONArray("coding");
                JSONObject coding = codingArray.getJSONObject(0);

                String medicationCode = coding.getString("code");
                String medicationName = coding.getString("display");
                medicationOrder.setMedicationCode(medicationCode);
                medicationOrder.setMedicationName(medicationName);

                // Validity Dates
                JSONObject validityPeriod = dispenseRequest.getJSONObject("validityPeriod");
                String startValidityDate = validityPeriod.getString("start");
                String endValidityDate = validityPeriod.getString("end");
                medicationOrder.setStartValidityDate(startValidityDate);
                medicationOrder.setStartValidityDate(endValidityDate);

                // Refill Details
                double refillQuanitity = dispenseRequest.getJSONObject("quantity").getDouble("value");
                int numberOfRepeatsAllowed = dispenseRequest.getInt("numberOfRepeatsAllowed");
                medicationOrder.setRefillsAllowed(numberOfRepeatsAllowed);
                medicationOrder.setRefillQuantity(refillQuanitity);

                medicationOrderList.add(medicationOrder);
            }
            return medicationOrderList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
