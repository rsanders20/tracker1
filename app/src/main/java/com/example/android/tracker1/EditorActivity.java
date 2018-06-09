package com.example.android.tracker1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.net.URI;

/**
 * Created by ryan on 5/14/18.
 */

public class EditorActivity extends AppCompatActivity {

    private Uri currentUri;

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


    }


}
