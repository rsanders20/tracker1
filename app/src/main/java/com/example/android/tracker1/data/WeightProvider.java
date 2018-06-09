package com.example.android.tracker1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.CursorAdapter;

import static com.example.android.tracker1.data.Contract.CONTENT_AUTHORITY;
import static com.example.android.tracker1.data.Contract.PATH_WEIGHT;
import static com.example.android.tracker1.data.Contract.WeightEntry;

/**
 * Created by ryan on 5/13/18.
 */

public class WeightProvider extends ContentProvider {

    public static final String LOG_TAG = WeightProvider.class.getSimpleName();


    private WeightDbHelper mDbHelper;

    private static final int WEIGHTS = 100;
    private static final int WEIGHT_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY,PATH_WEIGHT,WEIGHTS);
        sUriMatcher.addURI(CONTENT_AUTHORITY,PATH_WEIGHT+"/#",WEIGHT_ID);
    }


    @Override
    public boolean onCreate() {
        mDbHelper = new WeightDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case WEIGHTS:
                cursor = database.query(WeightEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case WEIGHT_ID:
                selection = WeightEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(WeightEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WEIGHTS:
                return WeightEntry.CONTENT_LIST_TYPE;
            case WEIGHT_ID:
                return WeightEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case WEIGHTS:
                return insertWeight(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion not supported for "+uri);
        }
    }

    private Uri insertWeight(Uri uri, ContentValues values){
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Data check of date
        String date = values.getAsString(WeightEntry.COLUMN_DATE);
        if (date==null){
            throw new IllegalArgumentException("Weight requires a date");
        }
        //Data check for weight value
        Integer weight = values.getAsInteger(WeightEntry.COLUMN_WEIGHT);
        if (weight==null){
            throw new IllegalArgumentException("Weight requires a numeric value");
        }

        long id = database.insert(WeightEntry.TABLE_NAME, null, values);
        if (id==-1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WEIGHTS:
                rowsDeleted = database.delete(WeightEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case WEIGHT_ID:
                selection = WeightEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(WeightEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WEIGHTS:
                return updateWeight(uri, contentValues, selection, selectionArgs);
            case WEIGHT_ID:
                selection = WeightEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateWeight(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updateWeight(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        //Data check for date
        if (values.containsKey(WeightEntry.COLUMN_DATE)) {
            String date = values.getAsString(WeightEntry.COLUMN_DATE);
            if (date==null){
                throw new IllegalArgumentException("Weight entry requires a date");
            }
        }

        //Data check for weight value
        if (values.containsKey(WeightEntry.COLUMN_WEIGHT)) {
            String date = values.getAsString(WeightEntry.COLUMN_WEIGHT);
            if (date==null){
                throw new IllegalArgumentException("Weight entry requires a value");
            }
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        int rowsUpdated = database.update(WeightEntry.TABLE_NAME, values,
                selection, selectionArgs);

        if (rowsUpdated!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsUpdated;
    }

}
