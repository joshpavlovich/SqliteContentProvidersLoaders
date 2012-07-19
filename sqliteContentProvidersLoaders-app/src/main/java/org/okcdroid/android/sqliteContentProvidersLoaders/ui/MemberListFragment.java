package org.okcdroid.android.sqliteContentProvidersLoaders.ui;

import org.okcdroid.android.sqliteContentProvidersLoaders.database.MemberTable;
import org.okcdroid.android.sqliteContentProvidersLoaders.provider.MemberProvider;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

public class MemberListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;

    private OnListItemSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnListItemSelectedListener {
        public void onMemberSelected(final int position, final long rowId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Create a new Adapter and bind it to the List View
        adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.two_line_list_item, null, new String[] { MemberTable.COLUMN_NAME, MemberTable.COLUMN_DESCRIPTION }, new int[] {
                android.R.id.text1, android.R.id.text2 }, 0);

        setListAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnListItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnListItemSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long rowId) {
        // Send the event to the host activity
        mCallback.onMemberSelected(position, rowId);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MemberProvider.CONTENT_URI, MemberTable.COLUMNS_ALL, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}