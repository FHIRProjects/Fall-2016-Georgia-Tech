package edu.gatech.mass.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.mass.model.MedicalRecordConnection;

public class SampleMedicalRecordConnectionDataProvider {
    public static List<MedicalRecordConnection> medicalRecordConnectionList;
    public static Map<String, MedicalRecordConnection> medicalRecordConnectionMap;

    static {
        medicalRecordConnectionList = new ArrayList<>();
        medicalRecordConnectionMap = new HashMap<>();

        addMedicalRecordConnection(new MedicalRecordConnection(null, "Georgia Tech", "523050", "marladixon", "p@ssW0rd",
                "http://polaris.i3l.gatech.edu:8080/fhir-omopv5/base/"));
    }

    private static void addMedicalRecordConnection(MedicalRecordConnection medicalRecordConnection){
        medicalRecordConnectionList.add(medicalRecordConnection);
        medicalRecordConnectionMap.put(medicalRecordConnection.getMedicalRecordConnectionId(), medicalRecordConnection);
    }

}
