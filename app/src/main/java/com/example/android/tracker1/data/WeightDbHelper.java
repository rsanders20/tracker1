package com.example.android.tracker1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.tracker1.data.Contract.WeightEntry;

/**
 * Created by ryan on 5/13/18.
 */

public class WeightDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "shelter.db";
    public static final int DATABASE_VERSION = 1;

    public WeightDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + WeightEntry.TABLE_NAME+" ("
                + WeightEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +WeightEntry.COLUMN_DATE+ " TEXT NOT NULL,"
                +WeightEntry.COLUMN_WEIGHT+" TEXT)";

        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //onUpgrade(db, oldVersion, newVersion);
    }
}
