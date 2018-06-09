package com.example.android.tracker1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

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


    }
}
