package edu.gatech.mass.database;

public class DemographicQuestionnaireTable {

    public static final String TABLE_DEMOGRAPHIC = "DemographicQuestionnaire";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIME_STAMP = "timeStamp";
    public static final String COLUMN_PRIMARY_LANGUAGE = "primaryLanguage";
    public static final String COLUMN_EDUCATION = "education";
    public static final String COLUMN_PEOPLE_IN_HOUSEHOLD = "peopleInHousehold";
    public static final String COLUMN_JOBS= "jobs";
    public static final String COLUMN_IMPAIRMENT = "impairment";
    public static final String COLUMN_PROBLEMS_SWALLOWING = "problemsSwallowing";
    public static final String COLUMN_NEAREST_PHARMACY = "nearestPharmacy";
    public static final String COLUMN_TREATMENT_INTERFERENCE = "treatmentInterference";
    public static final String COLUMN_PROVIDERS = "providers";
    public static final String COLUMN_MEDICATION_COST = "medicationCost";
    public static final String COLUMN_VEHICLES = "vehicles";


    public static final String[] ALL_COLUMNS =
            {COLUMN_ID, COLUMN_TIME_STAMP, COLUMN_PRIMARY_LANGUAGE, COLUMN_EDUCATION, COLUMN_PEOPLE_IN_HOUSEHOLD,
                    COLUMN_JOBS, COLUMN_IMPAIRMENT, COLUMN_PROBLEMS_SWALLOWING,
                    COLUMN_NEAREST_PHARMACY, COLUMN_TREATMENT_INTERFERENCE,
                    COLUMN_PROVIDERS, COLUMN_MEDICATION_COST, COLUMN_VEHICLES};

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_DEMOGRAPHIC + "(" +
                    COLUMN_ID + " TEXT PRIMARY KEY," +
                    COLUMN_TIME_STAMP + " KEY TEXT," +
                    COLUMN_PRIMARY_LANGUAGE + " STRING," +
                    COLUMN_EDUCATION + " STRING," +
                    COLUMN_PEOPLE_IN_HOUSEHOLD + " STRING," +
                    COLUMN_JOBS + " STRING," +
                    COLUMN_IMPAIRMENT + " STRING," +
                    COLUMN_PROBLEMS_SWALLOWING + " STRING," +
                    COLUMN_NEAREST_PHARMACY+ " STRING," +
                    COLUMN_TREATMENT_INTERFERENCE + " STRING," +
                    COLUMN_PROVIDERS + " STRING," +
                    COLUMN_MEDICATION_COST + " STRING," +
                    COLUMN_VEHICLES + " STRING" + ");";
    public static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_DEMOGRAPHIC;
}
