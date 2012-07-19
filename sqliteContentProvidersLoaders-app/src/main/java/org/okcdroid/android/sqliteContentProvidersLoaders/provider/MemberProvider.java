package org.okcdroid.android.sqliteContentProvidersLoaders.provider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import org.okcdroid.android.sqliteContentProvidersLoaders.database.MemberTable;
import org.okcdroid.android.sqliteContentProvidersLoaders.database.DatabaseHelper;

public class MemberProvider extends ContentProvider {

    public static final int MEMBERS = 10;
    public static final int MEMBER_ID = 20;

    private static final String AUTHORITY = "org.okcdroid.android.sqliteContentProvidersLoaders.provider.MemberProvider";

    private static final String BASE_PATH = "members";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/members";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/member";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, MEMBERS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MEMBER_ID);
    }

    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = 0;
        final SQLiteDatabase sqlLiteDatabase = databaseHelper.getWritableDatabase();
        final int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case MEMBERS:
            rowsDeleted = sqlLiteDatabase.delete(MemberTable.DATABASE_TABLE, selection, selectionArgs);
            break;
        case MEMBER_ID:
            final String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                rowsDeleted = sqlLiteDatabase.delete(MemberTable.DATABASE_TABLE, MemberTable._ID + "=" + id, null);
            } else {
                rowsDeleted = sqlLiteDatabase.delete(MemberTable.DATABASE_TABLE, MemberTable._ID + id + " and " + selection, selectionArgs);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = 0;
        final SQLiteDatabase sqlLiteDatabase = databaseHelper.getWritableDatabase();
        final int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case MEMBERS:
            id = sqlLiteDatabase.insert(MemberTable.DATABASE_TABLE, null, values);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Using SQLiteQueryBuilder instead of query() method
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(MemberTable.DATABASE_TABLE);

        final int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case MEMBERS:
            break;
        case MEMBER_ID:
            // Adding the ID to the original query
            queryBuilder.appendWhere(MemberTable._ID + "=" + uri.getLastPathSegment());
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        final SQLiteDatabase db = databaseHelper.getReadableDatabase();
        final Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // Make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlLiteDatabase = databaseHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
        case MEMBERS:
            rowsUpdated = sqlLiteDatabase.update(MemberTable.DATABASE_TABLE, values, selection, selectionArgs);
            break;
        case MEMBER_ID:
            final String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                rowsUpdated = sqlLiteDatabase.update(MemberTable.DATABASE_TABLE, values, MemberTable._ID + "=" + id, null);
            } else {
                rowsUpdated = sqlLiteDatabase.update(MemberTable.DATABASE_TABLE, values, MemberTable._ID + "=" + id + " and " + selection, selectionArgs);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(final String[] projection) {
        if (projection != null) {
            final HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            final HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(MemberTable.COLUMNS_ALL));
            // Check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}