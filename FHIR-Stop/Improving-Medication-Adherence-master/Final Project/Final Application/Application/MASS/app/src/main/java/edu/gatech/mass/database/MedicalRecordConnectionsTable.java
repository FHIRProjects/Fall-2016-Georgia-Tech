package edu.gatech.mass.database;

public class MedicalRecordConnectionsTable {
    public static final String TABLE_MEDICAL_RECORD_CONNECTIONS = "MedicalRecordConnection";
    public static final String COLUMN_ID = "medicalRecordConnectionId";
    public static final String COLUMN_NAME = "medicalRecordConnectionName";
    public static final String COLUMN_PATIENT = "patientId";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_URL = "url";

    public static final String[] ALL_COLUMNS =
            {COLUMN_ID, COLUMN_NAME, COLUMN_PATIENT, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_URL};

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_MEDICAL_RECORD_CONNECTIONS + "(" +
                    COLUMN_ID + " TEXT PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_PATIENT + " TEXT," +
                    COLUMN_USERNAME + " TEXT," +
                    COLUMN_PASSWORD + " INTEGER," +
                    COLUMN_URL + " TEXT" + ");";
    public static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_MEDICAL_RECORD_CONNECTIONS;
}
