package edu.gatech.mass.model;

import android.content.ContentValues;

import java.util.UUID;

import edu.gatech.mass.database.MedicationOrdersTable;

public class MedicationOrder {

    private String id;
    private String prescriberId;
    private String prescriberName;
    private String medicationCode;
    private String medicationName;
    private Double doseQuantity;
    private String doseUnit;
    private String startValidityDate;
    private String endValidityDate;
    private int refillsAllowed;
    private Double refillQuantity;

    private static final int MaxPrintableMedicationNameLength = 17;

    public MedicationOrder() {}

    public MedicationOrder(String id, String prescriberId, String prescriberName, String medicationCode,
                           String medicationName, double doseQuantity, String doseUnit, String startValidityDate,
                           String endValidityDate, int refillsAllowed, double refillQuantity) {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        this.id = id;
        this.prescriberId = prescriberId;
        this.prescriberName = prescriberName;
        this.medicationCode = medicationCode;
        this.medicationName = medicationName;
        this.doseQuantity = doseQuantity;
        this.doseUnit = doseUnit;
        this.startValidityDate = startValidityDate;
        this.endValidityDate = endValidityDate;
        this.refillsAllowed = refillsAllowed;
        this.refillQuantity = refillQuantity;
    }

    public String getMedicationShortName() {
        if (this.medicationName.length() > MaxPrintableMedicationNameLength) {
            return this.getMedicationName().substring(0, 17) + "...";
        }
        else {
            return this.getMedicationName();
        }
    }

    public String getDoseQuantityWithUnits() {
        return this.doseQuantity.intValue() + " " + this.doseUnit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrescriberId() {
        return prescriberId;
    }

    public void setPrescriberId(String prescriberId) {
        this.prescriberId = prescriberId;
    }

    public String getPrescriberName() {
        return prescriberName;
    }

    public void setPrescriberName(String prescriberName) {
        this.prescriberName = prescriberName;
    }

    public String getMedicationCode() {
        return medicationCode;
    }

    public void setMedicationCode(String medicationCode) {
        this.medicationCode = medicationCode;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public double getDoseQuantity() {
        return doseQuantity;
    }

    public void setDoseQuantity(double doseQuantity) {
        this.doseQuantity = doseQuantity;
    }

    public String getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(String doseUnit) {
        this.doseUnit = doseUnit;
    }

    public String getStartValidityDate() {
        return startValidityDate;
    }

    public void setStartValidityDate(String startValidityDate) {
        this.startValidityDate = startValidityDate;
    }

    public String getEndValidityDate() {
        return endValidityDate;
    }

    public void setEndValidityDate(String endValidityDate) {
        this.endValidityDate = endValidityDate;
    }

    public int getRefillsAllowed() {
        return refillsAllowed;
    }

    public void setRefillsAllowed(int refillsAllowed) {
        this.refillsAllowed = refillsAllowed;
    }

    public double getRefillQuantity() {
        return refillQuantity;
    }

    public void setRefillQuantity(double refillQuantity) {
        this.refillQuantity = refillQuantity;
    }

    public ContentValues toValues() {
        ContentValues values = new ContentValues(6);

        values.put(MedicationOrdersTable.COLUMN_ID, id);
        values.put(MedicationOrdersTable.COLUMN_PRESCRIBER_ID, prescriberId);
        values.put(MedicationOrdersTable.COLUMN_PRESCRIBER_NAME, prescriberName);
        values.put(MedicationOrdersTable.COLUMN_MEDICATION_CODE, medicationCode);
        values.put(MedicationOrdersTable.COLUMN_MEDICATION_NAME, medicationName);
        values.put(MedicationOrdersTable.COLUMN_DOSE_QUANTITY, doseQuantity);
        values.put(MedicationOrdersTable.COLUMN_DOSE_UNIT, doseUnit);
        values.put(MedicationOrdersTable.COLUMN_REFILLS_ALLOWED, refillsAllowed);
        values.put(MedicationOrdersTable.COLUMN_REFILLS_QUANTITY, refillQuantity);
        values.put(MedicationOrdersTable.COLUMN_START_VALIDITY_DATE, startValidityDate);
        values.put(MedicationOrdersTable.COLUMN_END_VALIDITY_DATE, endValidityDate);
        return values;
    }
}
