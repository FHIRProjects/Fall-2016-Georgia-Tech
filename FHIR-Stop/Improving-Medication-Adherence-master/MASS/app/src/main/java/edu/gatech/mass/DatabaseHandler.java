package edu.gatech.mass;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseContract.EmrConnectionEntry.TABLE_NAME + " (" +
                    DatabaseContract.EmrConnectionEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.EmrConnectionEntry.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.EmrConnectionEntry.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.EmrConnectionEntry.COLUMN_NAME_PASSWORD + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.EmrConnectionEntry.TABLE_NAME;

    // Database Name
    private static final String DATABASE_NAME = "mass.db";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}
