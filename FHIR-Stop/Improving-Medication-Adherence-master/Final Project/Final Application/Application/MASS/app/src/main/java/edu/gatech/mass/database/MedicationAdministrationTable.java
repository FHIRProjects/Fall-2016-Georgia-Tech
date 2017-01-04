package edu.gatech.mass.database;

public class MedicationAdministrationTable {
    public static final String TABLE_MEDICATION_ADMINISTRATION = "MedicationAdministration";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PRESCRIPTION_ID = "prescriptionId";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_PRACTITIONER = "practitioner";
    public static final String COLUMN_ENCOUNTER = "encounter";
    public static final String COLUMN_WAS_NOT_GIVEN = "wasNotGiven";
    public static final String COLUMN_REASON_NOT_GIVEN = "reasonNotGiven";
    public static final String COLUMN_REASON_GIVEN = "reasonGiven";
    public static final String COLUMN_EFFECTIVE_TIME = "effectiveTime";
    public static final String COLUMN_MEDICATION_ID = "medicationId";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_DOSE_METHOD = "dosageMethod";
    public static final String COLUMN_DOSE_QUANTITY = "dosageQuantity";

    public static final String[] ALL_COLUMNS =
            {COLUMN_ID, COLUMN_PRESCRIPTION_ID, COLUMN_STATUS, COLUMN_PRACTITIONER,
                    COLUMN_ENCOUNTER, COLUMN_WAS_NOT_GIVEN, COLUMN_REASON_NOT_GIVEN,
                    COLUMN_REASON_GIVEN, COLUMN_EFFECTIVE_TIME, COLUMN_MEDICATION_ID,
                    COLUMN_NOTE, COLUMN_DOSE_METHOD, COLUMN_DOSE_QUANTITY};

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_MEDICATION_ADMINISTRATION + "(" +
                    COLUMN_ID + " TEXT PRIMARY KEY," +
                    COLUMN_PRESCRIPTION_ID + " TEXT," +
                    COLUMN_STATUS + " TEXT," +
                    COLUMN_PRACTITIONER + " TEXT," +
                    COLUMN_ENCOUNTER + " TEXT," +
                    COLUMN_WAS_NOT_GIVEN + " INTEGER," +
                    COLUMN_REASON_NOT_GIVEN + " TEXT," +
                    COLUMN_REASON_GIVEN + " TEXT," +
                    COLUMN_EFFECTIVE_TIME + " TEXT," +
                    COLUMN_MEDICATION_ID + " INTEGER," +
                    COLUMN_NOTE + " TEXT," +
                    COLUMN_DOSE_METHOD + " TEXT," +
                    COLUMN_DOSE_QUANTITY + " REAL" +
                    ");";

    public static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_MEDICATION_ADMINISTRATION;
}
