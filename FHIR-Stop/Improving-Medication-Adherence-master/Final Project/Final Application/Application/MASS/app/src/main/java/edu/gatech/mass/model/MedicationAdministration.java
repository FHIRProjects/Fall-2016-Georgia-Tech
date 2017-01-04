package edu.gatech.mass.model;

import android.content.ContentValues;

import edu.gatech.mass.database.MedicationAdministrationTable;

public class MedicationAdministration {

    private String id;
    private String patientId;
    private String status;
    private String practitioner;
    private String encounter;
    private String prescriptionId;
    private Boolean wasNotGiven;
    private String reasonNotGiven;
    private String reasonGiven;
    private String effectiveTime;
    private String medicationId;
    private String note;
    private String doseMethod;
    private Double doseQuantity;

    public MedicationAdministration() {
    }

    public MedicationAdministration(String id, String patientId, String status, String practitioner,
                                    String encounter, String prescriptionId, Boolean wasNotGiven,
                                    String reasonNotGiven, String reasonGiven, String effectiveTime,
                                    String medicationId, String note, String doseMethod, Double doseQuantity) {
        this.id = id;
        this.patientId = patientId;
        this.status = status;
        this.practitioner = practitioner;
        this.encounter = encounter;
        this.prescriptionId = prescriptionId;
        this.wasNotGiven = wasNotGiven;
        this.reasonNotGiven = reasonNotGiven;
        this.reasonGiven = reasonGiven;
        this.effectiveTime = effectiveTime;
        this.medicationId = medicationId;
        this.note = note;
        this.doseMethod = doseMethod;
        this.doseQuantity = doseQuantity;
    }


    public ContentValues toValues() {
        ContentValues values = new ContentValues(6);
        values.put(MedicationAdministrationTable.COLUMN_ID, id);
        values.put(MedicationAdministrationTable.COLUMN_PRESCRIPTION_ID, prescriptionId);
        values.put(MedicationAdministrationTable.COLUMN_STATUS, status);
        values.put(MedicationAdministrationTable.COLUMN_PRACTITIONER, practitioner);
        values.put(MedicationAdministrationTable.COLUMN_ENCOUNTER, encounter);
        values.put(MedicationAdministrationTable.COLUMN_WAS_NOT_GIVEN, wasNotGiven);
        values.put(MedicationAdministrationTable.COLUMN_REASON_NOT_GIVEN, reasonNotGiven);
        values.put(MedicationAdministrationTable.COLUMN_REASON_GIVEN, reasonGiven);
        values.put(MedicationAdministrationTable.COLUMN_EFFECTIVE_TIME, effectiveTime);
        values.put(MedicationAdministrationTable.COLUMN_MEDICATION_ID, medicationId);
        values.put(MedicationAdministrationTable.COLUMN_NOTE, note);
        values.put(MedicationAdministrationTable.COLUMN_DOSE_METHOD, doseMethod);
        values.put(MedicationAdministrationTable.COLUMN_DOSE_QUANTITY, doseQuantity);
        return values;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPractitioner() {
        return practitioner;
    }

    public void setPractitioner(String practitioner) {
        this.practitioner = practitioner;
    }

    public String getEncounter() {
        return encounter;
    }

    public void setEncounter(String encounter) {
        this.encounter = encounter;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Boolean getWasNotGiven() {
        return wasNotGiven;
    }

    public void setWasNotGiven(Boolean wasNotGiven) {
        this.wasNotGiven = wasNotGiven;
    }

    public String getReasonNotGiven() {
        return reasonNotGiven;
    }

    public void setReasonNotGiven(String reasonNotGiven) {
        this.reasonNotGiven = reasonNotGiven;
    }

    public String getReasonGiven() {
        return reasonGiven;
    }

    public void setReasonGiven(String reasonGiven) {
        this.reasonGiven = reasonGiven;
    }

    public String getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDoseMethod() {
        return doseMethod;
    }

    public void setDoseMethod(String doseMethod) {
        this.doseMethod = doseMethod;
    }

    public Double getDoseQuantity() {
        return doseQuantity;
    }

    public void setDoseQuantity(Double doseQuantity) {
        this.doseQuantity = doseQuantity;
    }

}
