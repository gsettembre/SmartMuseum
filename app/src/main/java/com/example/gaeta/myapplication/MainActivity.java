package com.example.gaeta.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonqr = (Button) findViewById(R.id.buttonestart);

        buttonqr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                runSubActivity();
            }
        });

    }

    private void runSubActivity() {
        Intent intent = new Intent(this,SubActivity.class);
        startActivity(intent);
    }

}