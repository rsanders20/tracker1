package com.example.android.tracker1;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tracker1.data.Contract;
import com.example.android.tracker1.data.WeightDbHelper;
import com.example.android.tracker1.data.WeightProvider;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    TextView testText;

    private static final int WEIGHT_LOADER = 0;

    WeightCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonAdd = (Button) findViewById(R.id.add_button);
        buttonAdd.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Intent addIntent = new Intent(MainActivity.this, EditorActivity.class);
                MainActivity.this.startActivity(addIntent);
            }
        });

        Button buttonList = (Button) findViewById(R.id.list_button);
        buttonList.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Intent addIntent = new Intent(MainActivity.this, ListActivity.class);
                MainActivity.this.startActivity(addIntent);
            }
        });

        testText = (TextView) findViewById(R.id.test_text);
        getLoaderManager().initLoader(WEIGHT_LOADER, null, this);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        testText.setText("");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

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

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(cursor==null || cursor.getCount()<1){
            return;
        }

        List<String> weightlist = new ArrayList<>();
        List<String> datelist = new ArrayList<>();
        while (cursor.moveToNext()){
            String dateString = cursor.getString(cursor.getColumnIndex(Contract.WeightEntry.COLUMN_DATE));
            String weightString = cursor.getString(cursor.getColumnIndex(Contract.WeightEntry.COLUMN_WEIGHT));
            weightlist.add(weightString);
            datelist.add(dateString);
        }

        int listLength = weightlist.size();

        //graphing

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy");

        List<DataPoint> dpl = new ArrayList<>();
        for (int i=0;i<listLength; i++) {

            try{
                long x = (sdf.parse(datelist.get(i)).getTime());
                int y = Integer.valueOf(weightlist.get(i));

                dpl.add(new DataPoint(x,y));

                Log.i("ufuu", "the parsed date is "+ sdf.parse(datelist.get(i)).toString());
            } catch (Exception e) {
                Log.i("ufuu", "the parsed date is throwing exception ");
                return;
            }


        };
        ;

        DataPoint[] dp = new DataPoint[dpl.size()];
        dp = dpl.toArray(dp);

        Log.i("ufuu","The array size is "+dp.length);

        GraphView graph = (GraphView) findViewById(R.id.graph);

        //LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(dp);
        graph.removeAllSeries();
        graph.addSeries(series);


        graph.getGridLabelRenderer().setLabelFormatter(new
                DateAsXAxisLabelFormatter(MainActivity.this));

        graph.getGridLabelRenderer().setNumHorizontalLabels(5);

        graph.getViewport().setMinX(dpl.get(0).getX());
        graph.getViewport().setMaxX(dpl.get(listLength-1).getX());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getGridLabelRenderer().setHumanRounding(false, true);

        testText.setText(Integer.toString(listLength));
    }
}
