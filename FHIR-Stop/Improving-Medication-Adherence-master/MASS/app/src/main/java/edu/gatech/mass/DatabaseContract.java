package edu.gatech.mass;

import android.provider.BaseColumns;

/**
 * Created by domir on 11/12/2016.
 */

public final class DatabaseContract implements BaseColumns {
    private DatabaseContract() {}

    public static class EmrConnectionEntry implements BaseColumns {
        public static final String TABLE_NAME = "EmrConnections";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";

    }
}
