package com.example.android.tracker1;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;


import com.example.android.tracker1.data.Contract;

/**
 * Created by ryan on 5/15/18.
 */

public class WeightCursorAdapter extends CursorAdapter {

    public WeightCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,
                viewGroup, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView dateView = (TextView) view.findViewById(R.id.date);
        TextView weightView = (TextView) view.findViewById(R.id.weight);

        String dateString = cursor.getString(cursor.getColumnIndex(Contract.WeightEntry.COLUMN_DATE));
        String weightString = cursor.getString(cursor.getColumnIndex(Contract.WeightEntry.COLUMN_WEIGHT));

        dateView.setText(dateString);
        weightView.setText(weightString);

    }
}
