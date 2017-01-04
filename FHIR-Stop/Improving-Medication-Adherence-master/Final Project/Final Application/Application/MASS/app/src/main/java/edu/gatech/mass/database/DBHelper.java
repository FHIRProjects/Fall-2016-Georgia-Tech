package edu.gatech.mass.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {

    private static final String DB_FILE_NAME = "massapp.db";
    private static final int DB_VERSION = 2;

    DBHelper(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MedicalRecordConnectionsTable.SQL_CREATE);
        db.execSQL(MedicationOrdersTable.SQL_CREATE);
        db.execSQL(DemographicQuestionnaireTable.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MedicalRecordConnectionsTable.SQL_DELETE);
        db.execSQL(MedicationOrdersTable.SQL_DELETE);
        db.execSQL(DemographicQuestionnaireTable.SQL_DELETE);
        onCreate(db);
    }
}
