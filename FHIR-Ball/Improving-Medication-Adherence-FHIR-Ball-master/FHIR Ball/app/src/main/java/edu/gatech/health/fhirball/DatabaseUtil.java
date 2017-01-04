package edu.gatech.health.fhirball;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.gatech.health.fhirball.DatabaseConstants.*;
import static edu.gatech.health.fhirball.DatabaseConstants.MedOrders;

public class DatabaseUtil {

    /**
     * @return a list of a list of objects. The list is the rows. The map is a specific row of mapping columnName -> value.
     * Objects can either be Strings or longs.
     */
    public static List<Map<DatabaseConstants, Object>> getMedsFromDb(SQLiteDatabase fhirBall) {
        ensureMedOrdersTbl(fhirBall);

        final Cursor resultSet = fhirBall.rawQuery("Select * from " + MedOrders, null);
        resultSet.moveToFirst();

        List<Map<DatabaseConstants, Object>> medsToDescription = new ArrayList<>();

        while(!resultSet.isAfterLast()) {
            medsToDescription.add(new HashMap<DatabaseConstants, Object>(){{
                put(Medicine, resultSet.getString(0));
                put(Description, resultSet.getString(1));
                put(Frequency, resultSet.getLong(2));}});
            resultSet.moveToNext();

        }
        resultSet.close();

        return medsToDescription;
    }

    public static List<Administration> getLog(SQLiteDatabase fhirBall) {
        ensureNotificationHistoryTbl(fhirBall);

        Cursor resultSet = fhirBall.rawQuery("Select " + Medicine + ", " + LastTaken + ", " + ReasonCode + " " +
                "from " + NotificationHistory + " " +
                "where " + ReasonCode + " <= 4 " +
                "ORDER BY " + LastTaken + " DESC " +
                "LIMIT 100", null);
        resultSet.moveToFirst();

        List<Administration> administrations = new ArrayList<>();

        while(!resultSet.isAfterLast()) {
            administrations.add(new Administration(resultSet.getString(0), resultSet.getLong(1), MedEncounterReason.get(resultSet.getInt(2))));
            resultSet.moveToNext();
        }
        resultSet.close();

        return administrations;
    }



    public static Map<String, Long> getLastTaken(SQLiteDatabase fhirBall) {
        ensureNotificationHistoryTbl(fhirBall);

        Cursor resultSet = fhirBall.rawQuery("Select " + Medicine + ", MAX(" + LastTaken + ") " +
                "from " + NotificationHistory + " " +
                "GROUP BY " + Medicine, null);
        resultSet.moveToFirst();

        Map<String, Long> medsToTaken = new HashMap<>();

        while(!resultSet.isAfterLast()) {
            medsToTaken.put(resultSet.getString(0), resultSet.getLong(1));
            resultSet.moveToNext();
        }
        resultSet.close();

        return medsToTaken;
    }

    public static void registerTakenMed(SQLiteDatabase fhirBall, String medName, MedEncounterReason reason) {
        long time = System.currentTimeMillis();
        fhirBall.execSQL("INSERT INTO " + NotificationHistory + " " +
                "VALUES('" + medName + "', " + time + ", "+reason.getLocalCode()+");");
    }

    public static void ensureMedOrdersTbl(SQLiteDatabase fhirBall) {
        fhirBall.execSQL("CREATE TABLE IF NOT EXISTS " + MedOrders + "("
                + Medicine + " VARCHAR,"
                + Description + " VARCHAR,"
                // Frequency is the period in minutes that the medicine should be taken.
                // For daily meds, it should be 1440 ( = 60 * 24)
                + Frequency + " int);");
    }

    public static void ensureNotificationHistoryTbl(SQLiteDatabase fhirBall) {
        fhirBall.execSQL("CREATE TABLE IF NOT EXISTS " + NotificationHistory + "("
                + Medicine + " VARCHAR,"
                // timeInMillis of the last time taken
                + LastTaken + " INTEGER,"
                // values are one of the MedEncounterReasons enum
                + ReasonCode + " INTEGER);");
    }
}
