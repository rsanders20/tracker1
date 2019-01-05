package com.example.android.tracker1;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.android.tracker1.data.Contract;

/**
 * Created by ryan on 5/13/18.
 */

public class ListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int WEIGHT_LOADER = 0;

    WeightCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //insertWeight();

        ListView weightList = (ListView) findViewById(R.id.list);
        getLoaderManager().initLoader(WEIGHT_LOADER, null, this);

        mCursorAdapter = new WeightCursorAdapter(this, null);
        weightList.setAdapter(mCursorAdapter);

        weightList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final Intent intent = new Intent(ListActivity.this, EditorActivity.class);
                Uri currentUri = ContentUris.withAppendedId(Contract.WeightEntry.CONTENT_URI,id);
                intent.setData(currentUri);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all:
                int delRows = getContentResolver().delete(Contract.WeightEntry.CONTENT_URI,
                        null, null);
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                Contract.WeightEntry._ID,
                Contract.WeightEntry.COLUMN_DATE,
                Contract.WeightEntry.COLUMN_WEIGHT
        };
        return new CursorLoader(this,
                Contract.WeightEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    private void insertWeight(){
        ContentValues values = new ContentValues();

        values.put(Contract.WeightEntry.COLUMN_DATE, "5/5/2018");
        values.put(Contract.WeightEntry.COLUMN_WEIGHT, 175);

        Uri mNewUri = getContentResolver().insert(Contract.WeightEntry.CONTENT_URI, values);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);

    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }
}
