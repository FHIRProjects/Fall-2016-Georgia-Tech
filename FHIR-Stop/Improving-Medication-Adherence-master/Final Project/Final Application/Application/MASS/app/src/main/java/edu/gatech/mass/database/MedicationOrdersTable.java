package edu.gatech.mass.database;

public class MedicationOrdersTable {
    public static final String TABLE_MEDICATION_ORDERS = "MedicationOrder";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PRESCRIBER_ID = "prescriberId";
    public static final String COLUMN_PRESCRIBER_NAME = "prescriberName";
    public static final String COLUMN_MEDICATION_CODE = "medicationCode";
    public static final String COLUMN_MEDICATION_NAME = "medicationName";
    public static final String COLUMN_DOSE_QUANTITY = "doseQuantity";
    public static final String COLUMN_DOSE_UNIT = "doseUnit";
    public static final String COLUMN_START_VALIDITY_DATE = "startValidityDate";
    public static final String COLUMN_END_VALIDITY_DATE = "endValidityDate";
    public static final String COLUMN_REFILLS_ALLOWED = "refillsAllowed";
    public static final String COLUMN_REFILLS_QUANTITY = "refillQuantity";

    public static final String[] ALL_COLUMNS =
            {COLUMN_ID, COLUMN_PRESCRIBER_ID, COLUMN_PRESCRIBER_NAME, COLUMN_MEDICATION_CODE,
                    COLUMN_MEDICATION_NAME, COLUMN_DOSE_QUANTITY, COLUMN_DOSE_UNIT,
                    COLUMN_START_VALIDITY_DATE, COLUMN_END_VALIDITY_DATE, COLUMN_REFILLS_ALLOWED,
                    COLUMN_REFILLS_QUANTITY};

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_MEDICATION_ORDERS + "(" +
                    COLUMN_ID + " TEXT PRIMARY KEY," +
                    COLUMN_PRESCRIBER_ID + " TEXT," +
                    COLUMN_PRESCRIBER_NAME + " TEXT," +
                    COLUMN_MEDICATION_CODE + " TEXT," +
                    COLUMN_MEDICATION_NAME + " TEXT," +
                    COLUMN_DOSE_QUANTITY + " REAL," +
                    COLUMN_DOSE_UNIT + " STRING," +
                    COLUMN_START_VALIDITY_DATE + " STRING," +
                    COLUMN_END_VALIDITY_DATE + " STRING," +
                    COLUMN_REFILLS_ALLOWED + " INTEGER," +
                    COLUMN_REFILLS_QUANTITY + " REAL" +
                    ");";

    public static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_MEDICATION_ORDERS;
}
