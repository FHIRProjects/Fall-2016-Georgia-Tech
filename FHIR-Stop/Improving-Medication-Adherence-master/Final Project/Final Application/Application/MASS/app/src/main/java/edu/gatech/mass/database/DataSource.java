package edu.gatech.mass.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.mass.model.MedicalRecordConnection;
import edu.gatech.mass.model.MedicationOrder;


public class DataSource {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private SQLiteOpenHelper mDbHelper;

    public DataSource(Context context) {
        this.mContext = context;
        mDbHelper = new DBHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void open() {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public MedicalRecordConnection createMedicalRecordConnection(MedicalRecordConnection medicalRecordConnection) {
        ContentValues values = medicalRecordConnection.toValues();
        mDatabase.insert(MedicalRecordConnectionsTable.TABLE_MEDICAL_RECORD_CONNECTIONS, null, values);
        return medicalRecordConnection;
    }

    public long getMedicalRecordConnectionsCount() {
        return DatabaseUtils.queryNumEntries(mDatabase, MedicalRecordConnectionsTable.TABLE_MEDICAL_RECORD_CONNECTIONS);
    }

    public void seedDatabaseWithMedicalConnections(List<MedicalRecordConnection> medicalRecordConnectionList) {
        long numConnections = getMedicalRecordConnectionsCount();
        if (numConnections == 0) {
            for (MedicalRecordConnection connection : medicalRecordConnectionList) {
                try {
                    createMedicalRecordConnection(connection);
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<MedicalRecordConnection> getAllMedicalRecordConnections() {
        List<MedicalRecordConnection> medicalRecordConnections = new ArrayList<>();
        Cursor cursor = mDatabase.query(MedicalRecordConnectionsTable.TABLE_MEDICAL_RECORD_CONNECTIONS,
                MedicalRecordConnectionsTable.ALL_COLUMNS, null, null, null, null, null);
        while (cursor.moveToNext()) {
            MedicalRecordConnection connection = new MedicalRecordConnection();
            connection.setMedicalRecordConnectionId(cursor.getString(
                    cursor.getColumnIndex(MedicalRecordConnectionsTable.COLUMN_ID)));
            connection.setMedicalRecordConnectionName(cursor.getString(
                    cursor.getColumnIndex(MedicalRecordConnectionsTable.COLUMN_NAME)));
            connection.setPatientId(cursor.getString(
                    cursor.getColumnIndex(MedicalRecordConnectionsTable.COLUMN_PATIENT)));
            connection.setUserName(cursor.getString(
                    cursor.getColumnIndex(MedicalRecordConnectionsTable.COLUMN_USERNAME)));
            connection.setPassword(cursor.getString(
                    cursor.getColumnIndex(MedicalRecordConnectionsTable.COLUMN_PASSWORD)));
            connection.setUrl(cursor.getString(
                    cursor.getColumnIndex(MedicalRecordConnectionsTable.COLUMN_URL)));

            medicalRecordConnections.add(connection);
        }
        cursor.close();
        return medicalRecordConnections;
    }

    public MedicalRecordConnection updateMedicalRecordConnection(MedicalRecordConnection medicalRecordConnection) {
        ContentValues values = medicalRecordConnection.toValues();
        String whereClause = MedicalRecordConnectionsTable.COLUMN_ID + " = '" + medicalRecordConnection.getMedicalRecordConnectionId() + "'";
        mDatabase.update(MedicalRecordConnectionsTable.TABLE_MEDICAL_RECORD_CONNECTIONS, values, whereClause, null);
        return medicalRecordConnection;
    }


    // MEDICAL ORDERS
    public void seedDatabaseWithMedicalOrders(List<MedicationOrder> medicationOrderList) {
        long numConnections = getMedicationOrdersCount();
        if (numConnections == 0) {
            for (MedicationOrder order : medicationOrderList) {
                try {
                    createMedicalOrder(order);
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public MedicationOrder createMedicalOrder(MedicationOrder medicationOrder) {
        ContentValues values = medicationOrder.toValues();
        mDatabase.insert(MedicationOrdersTable.TABLE_MEDICATION_ORDERS, null, values);
        return medicationOrder;
    }

    public long getMedicationOrdersCount() {
        return DatabaseUtils.queryNumEntries(mDatabase, MedicationOrdersTable.TABLE_MEDICATION_ORDERS);
    }

    public List<MedicationOrder> getMedicationOrders() {
        List<MedicationOrder> medicationOrderList = new ArrayList<>();
        Cursor cursor = mDatabase.query(MedicationOrdersTable.TABLE_MEDICATION_ORDERS,
                MedicationOrdersTable.ALL_COLUMNS, null, null, null, null, null);
        while (cursor.moveToNext()) {
            MedicationOrder order = new MedicationOrder();
            order.setId(cursor.getString(
                    cursor.getColumnIndex(MedicationOrdersTable.COLUMN_ID)));
            order.setPrescriberId(cursor.getString(
                    cursor.getColumnIndex(MedicationOrdersTable.COLUMN_PRESCRIBER_ID)));
            order.setPrescriberName(cursor.getString(
                    cursor.getColumnIndex(MedicationOrdersTable.COLUMN_PRESCRIBER_NAME)));
            order.setMedicationCode(cursor.getString(
                    cursor.getColumnIndex(MedicationOrdersTable.COLUMN_MEDICATION_CODE)));
            order.setMedicationName(cursor.getString(
                    cursor.getColumnIndex(MedicationOrdersTable.COLUMN_MEDICATION_NAME)));
            order.setDoseQuantity(cursor.getDouble(
                    cursor.getColumnIndex(MedicationOrdersTable.COLUMN_DOSE_QUANTITY)));
            order.setDoseUnit(cursor.getString(
                    cursor.getColumnIndex(MedicationOrdersTable.COLUMN_DOSE_UNIT)));
            order.setRefillsAllowed(cursor.getInt(
                    cursor.getColumnIndex(MedicationOrdersTable.COLUMN_REFILLS_ALLOWED)));
            order.setRefillQuantity(cursor.getDouble(
                    cursor.getColumnIndex(MedicationOrdersTable.COLUMN_REFILLS_QUANTITY)));
            order.setStartValidityDate(cursor.getString(
                    cursor.getColumnIndex(MedicationOrdersTable.COLUMN_START_VALIDITY_DATE)));
            order.setEndValidityDate(cursor.getString(
                    cursor.getColumnIndex(MedicationOrdersTable.COLUMN_END_VALIDITY_DATE)));
            medicationOrderList.add(order);
        }
        cursor.close();
        return medicationOrderList;
    }

    public MedicationOrder updateMedicationOrders(MedicationOrder medicationOrder) {
        ContentValues values = medicationOrder.toValues();
        String whereClause = MedicationOrdersTable.COLUMN_ID + " = '" + medicationOrder.getId() + "'";
        mDatabase.update(MedicationOrdersTable.TABLE_MEDICATION_ORDERS, values, whereClause, null);
        return medicationOrder;
    }
}
