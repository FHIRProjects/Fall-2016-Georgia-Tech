package edu.gatech.mass.model;

import android.content.ContentValues;

import java.util.UUID;

import edu.gatech.mass.database.MedicalRecordConnectionsTable;

public class MedicalRecordConnection {

    private String medicalRecordConnectionId;
    private String medicalRecordConnectionName;
    private String patientId;
    private String userName;
    private String password;
    private String url;

    public MedicalRecordConnection() {
    }

    public MedicalRecordConnection(String medicalRecordConnectionId, String medicalRecordConnectionName, String patientId, String userName, String password, String url) {
        if (medicalRecordConnectionId == null) {
            medicalRecordConnectionId = UUID.randomUUID().toString();
        }

        this.medicalRecordConnectionId = medicalRecordConnectionId;
        this.medicalRecordConnectionName = medicalRecordConnectionName;
        this.patientId = patientId;
        this.userName = userName;
        this.password = password;
        this.url = url;
    }

    public String getMedicalRecordConnectionId() {
        return medicalRecordConnectionId;
    }

    public void setMedicalRecordConnectionId(String medicalRecordConnectionId) {
        this.medicalRecordConnectionId = medicalRecordConnectionId;
    }

    public String getMedicalRecordConnectionName() {
        return medicalRecordConnectionName;
    }

    public void setMedicalRecordConnectionName(String medicalRecordConnectionName) {
        this.medicalRecordConnectionName = medicalRecordConnectionName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ContentValues toValues() {
        ContentValues values = new ContentValues(6);

        values.put(MedicalRecordConnectionsTable.COLUMN_ID, medicalRecordConnectionId);
        values.put(MedicalRecordConnectionsTable.COLUMN_NAME, medicalRecordConnectionName);
        values.put(MedicalRecordConnectionsTable.COLUMN_USERNAME, userName);
        values.put(MedicalRecordConnectionsTable.COLUMN_PASSWORD, password);
        values.put(MedicalRecordConnectionsTable.COLUMN_URL, url);
        values.put(MedicalRecordConnectionsTable.COLUMN_PATIENT, patientId);
        return values;
    }

    @Override
    public String toString() {
        return "MedicalRecordConnection{" +
                "emrConnectionId='" + medicalRecordConnectionId + '\'' +
                ", emrName='" + medicalRecordConnectionName + '\'' +
                ", patientId=" + patientId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}