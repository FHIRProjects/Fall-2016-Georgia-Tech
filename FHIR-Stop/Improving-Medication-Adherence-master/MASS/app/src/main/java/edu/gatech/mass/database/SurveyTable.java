package edu.gatech.mass.database;

public class SurveyTable {
    public static final String TABLE_SURVEY = "Survey";
    public static final String COLUMN_TIME_STAMP = "timeStamp";
    public static final String COLUMN_TAKEN = "taken";
    public static final String COLUMN_LOST_MEDICATION = "lostMedication";
    public static final String COLUMN_INCONVENIENT_TIME = "inconvenientTime";
    public static final String COLUMN_DIFFICULT_TO_SWALLOW = "difficultToSwallow";
    public static final String COLUMN_WAY_IT_FEELS = "wayItFeels";
    public static final String COLUMN_DONT_NEED_IT = "dontNeedIt";

    public static final String[] ALL_COLUMNS =
            {COLUMN_TIME_STAMP, COLUMN_TAKEN, COLUMN_LOST_MEDICATION, COLUMN_INCONVENIENT_TIME,
                    COLUMN_DIFFICULT_TO_SWALLOW, COLUMN_WAY_IT_FEELS, COLUMN_DONT_NEED_IT};

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_SURVEY + "(" +
                    COLUMN_TIME_STAMP + " KEY TEXT," +
                    COLUMN_TAKEN + " INTEGER," +
                    COLUMN_LOST_MEDICATION + " INTEGER," +
                    COLUMN_INCONVENIENT_TIME + " INTEGER," +
                    COLUMN_DIFFICULT_TO_SWALLOW + " INTEGER," +
                    COLUMN_WAY_IT_FEELS + " INTEGER," +
                    COLUMN_DONT_NEED_IT + " INTEGER" + ");";
    public static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_SURVEY;
}
