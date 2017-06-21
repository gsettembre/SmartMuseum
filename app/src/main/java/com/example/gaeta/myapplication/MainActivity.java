package com.example.gaeta.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * PJDCC - Classe che modella l'Activity principale dove è presente una piccola introduzione.
 *         E' presente il bottone che farà avviare l'Activity per la scansione del codice QR.
 *
 * @authors Oranger Edoardo, Settembre Gaetano, Recchia Vito, Marchese Vito
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    static final int permesso_camera = 1;
    static final int permesso_archiviazione = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonqr = (Button) findViewById(R.id.buttonestart);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},permesso_camera);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},permesso_archiviazione);

        if(!connessioneInternet(this))
            Toast.makeText(this, "Connessione non presente", Toast.LENGTH_SHORT).show();

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

    private static boolean connessioneInternet(Context contesto){
        boolean connWifi = false;
        boolean connMobile = false;
        ConnectivityManager cm = (ConnectivityManager)contesto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info[] = cm.getAllNetworkInfo();
        for (NetworkInfo ni : info){
            if(ni.getTypeName().equalsIgnoreCase("WIFI"))
                if(ni.isConnected())
                    connWifi = true;
            if(ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if(ni.isConnected())
                    connMobile = true;
        }

       return connWifi || connMobile;
    }



}