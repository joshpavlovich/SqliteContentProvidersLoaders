package org.okcdroid.android.sqliteContentProvidersLoaders.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class MemberTable implements BaseColumns {

    public static final String COLUMN_SALARY = "amount";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_JOIN_DATE = "startDate";
    public static final String COLUMN_ACTIVE = "archivable";
    public static final String[] COLUMNS_ALL = new String[] { _ID, COLUMN_SALARY, COLUMN_DESCRIPTION, COLUMN_NAME, COLUMN_JOIN_DATE, COLUMN_ACTIVE };

    public static final String DATABASE_TABLE = "member";

    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + " ("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_SALARY + " REAL NOT NULL, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_JOIN_DATE + " INTEGER NOT NULL, "
            + COLUMN_ACTIVE  + " INTEGER NOT NULL DEFAULT 1);";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(MemberTable.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(database);
    }
}