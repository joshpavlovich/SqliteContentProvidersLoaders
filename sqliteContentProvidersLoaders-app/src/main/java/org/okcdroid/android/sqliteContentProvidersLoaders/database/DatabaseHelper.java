package org.okcdroid.android.sqliteContentProvidersLoaders.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sqliteContentProvidersLoaders.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase database) {
        MemberTable.onCreate(database);
    }

    @Override
    public void onOpen(final SQLiteDatabase database) {
        super.onOpen(database);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase database, final int oldVersion, final int newVersion) {
        MemberTable.onUpgrade(database, oldVersion, newVersion);
    }
}