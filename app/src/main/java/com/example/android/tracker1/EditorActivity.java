package com.example.android.tracker1;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tracker1.data.Contract;

import org.w3c.dom.Text;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ryan on 5/14/18.
 */

public class EditorActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private Uri currentUri;
    private EditText dateText;
    private EditText weightText;
    private Button saveButton;
    private Button deleteButton;

    private static final int LOAD_DATA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();

        currentUri = intent.getData();

        if(currentUri==null) {
            setTitle("Add record");
        } else {
            setTitle("Edit record");
        }

        dateText = (EditText) findViewById(R.id.date_text);
        weightText = (EditText) findViewById(R.id.weight_text);
        saveButton = (Button) findViewById(R.id.save_button);
        deleteButton = (Button) findViewById(R.id.delete_button);

        getLoaderManager().initLoader(LOAD_DATA, null, this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEntry();
                finish();
            }
        });

    }

    private void save() {
        String dateString = dateText.getText().toString().trim();
        boolean emptyDate = TextUtils.isEmpty(dateString);
        if (emptyDate) {
            Toast.makeText(this, "Please add a date", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check date format
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);

        try{
            sdf.parse(dateString);
            //Log.i("date_tag", "the parsed weight is "+sdf);
            //Log.i("date_tag", "the parsed weight is "+ sdf.parse(dateString).toString());
        } catch (Exception e) {
            Toast.makeText(this, "Date format incorrect", Toast.LENGTH_SHORT).show();
            return;
        }


        String weightString = weightText.getText().toString().trim();
        boolean emptyWeight = TextUtils.isEmpty(weightString);
        if (emptyWeight) {
            //Log.i("weight_tag", "The weight is"+weightString);
            Toast.makeText(this, "Please add a weight", Toast.LENGTH_SHORT).show();
            return;
        }


        ContentValues values = new ContentValues();
        values.put(Contract.WeightEntry.COLUMN_DATE, dateString);
        values.put(Contract.WeightEntry.COLUMN_WEIGHT, weightString);

        if (currentUri == null) {
            Uri mNewUri = getContentResolver().insert(Contract.WeightEntry.CONTENT_URI, values);
            if (mNewUri == null) {
                Toast.makeText(this, "Error saving entry", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Entry saved", Toast.LENGTH_SHORT).show();
            }

        } else {
            int rowsUpdated = getContentResolver().update(currentUri, values, null, null);
            if (rowsUpdated == 0) {
                Toast.makeText(this, "Error updating", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Entry updated", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private int deleteEntry() {
        if(currentUri==null){
            return 0;
        }

        int rowsDeleted = getContentResolver().delete(currentUri,null,null);

        if (rowsDeleted==0){
            Toast.makeText(this, "Error deleting",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Entry deleted",Toast.LENGTH_SHORT).show();
        }
        finish();
        return rowsDeleted;
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dateText.setText("");
        weightText.setText("");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if(currentUri==null){
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy");
            String date = sdf.format(new Date());
            dateText.setText(date);
            return null;
        }

        String[] projection = {
                Contract.WeightEntry._ID,
                Contract.WeightEntry.COLUMN_DATE,
                Contract.WeightEntry.COLUMN_WEIGHT
        };
        return new CursorLoader(this,
                currentUri,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
         if(cursor==null || cursor.getCount()<1){
             return;
         }

         if (cursor.moveToFirst()) {
             String dateString = cursor.getString(cursor.getColumnIndex(Contract.WeightEntry.COLUMN_DATE));
             String weightString = cursor.getString(cursor.getColumnIndex(Contract.WeightEntry.COLUMN_WEIGHT));

             dateText.setText(dateString);
             weightText.setText(weightString);
         }

    }
}
