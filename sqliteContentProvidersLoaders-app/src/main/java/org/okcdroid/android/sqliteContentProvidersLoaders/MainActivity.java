package org.okcdroid.android.sqliteContentProvidersLoaders;

import java.math.BigDecimal;
import java.util.Calendar;

import org.okcdroid.android.sqliteContentProvidersLoaders.database.MemberTable;
import org.okcdroid.android.sqliteContentProvidersLoaders.provider.MemberProvider;
import org.okcdroid.android.sqliteContentProvidersLoaders.ui.MemberListFragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements MemberListFragment.OnListItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Cursor cursor = getContentResolver().query(MemberProvider.CONTENT_URI, MemberTable.COLUMNS_ALL, null, null, null);
        if (cursor != null && !cursor.moveToFirst()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MemberTable.COLUMN_SALARY, new BigDecimal("250000.00").doubleValue());
            contentValues.put(MemberTable.COLUMN_DESCRIPTION, "I am an Android developer.");
            contentValues.put(MemberTable.COLUMN_NAME, "John Smith");
            contentValues.put(MemberTable.COLUMN_JOIN_DATE, Calendar.getInstance().getTimeInMillis());
            getContentResolver().insert(MemberProvider.CONTENT_URI, contentValues);

            contentValues = new ContentValues();
            contentValues.put(MemberTable.COLUMN_SALARY, new BigDecimal("45000.00").doubleValue());
            contentValues.put(MemberTable.COLUMN_DESCRIPTION, "I am an J2EE developer.");
            contentValues.put(MemberTable.COLUMN_NAME, "Jerry Jones");
            contentValues.put(MemberTable.COLUMN_JOIN_DATE, Calendar.getInstance().getTimeInMillis());
            getContentResolver().insert(MemberProvider.CONTENT_URI, contentValues);

            Toast.makeText(getApplicationContext(), "Added Members", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Members Exists", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMemberSelected(final int position, final long rowId) {
    }
}