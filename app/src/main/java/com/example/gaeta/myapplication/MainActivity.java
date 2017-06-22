package com.example.gaeta.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * PJDCC - Classe che modella l'Activity principale dove è presente una piccola introduzione.
 *         E' presente il bottone che farà avviare l'Activity per la scansione del codice QR.
 *
 * @author Oranger Edoardo, Settembre Gaetano, Recchia Vito, Marchese Vito
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /*
     *  Variabile identificativa del permesso della fotocamera
     */
    private final static int PERMESSO_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonqr = (Button) findViewById(R.id.buttonestart);

        if(!connessioneInternet(this)){
            Toast.makeText(this, "Connessione non presente", Toast.LENGTH_SHORT).show();
        }


        buttonqr.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v){
                checkPermessi();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermessi(){
        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            String[] permissionRequest = {Manifest.permission.CAMERA};
            requestPermissions(permissionRequest,PERMESSO_CAMERA);
        }
        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            runSubActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMESSO_CAMERA){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                runSubActivity();
            }
        }
    }


    /**
     *  Metodo che permette di avviare l'Activity della scansione del QR.
     */
    private void runSubActivity() {
        Intent intent = new Intent(this,SubActivity.class);
        startActivity(intent);
    }

    private static boolean connessioneInternet(Context contesto) {
        boolean connWifi = false;
        boolean connMobile = false;
        ConnectivityManager cm = (ConnectivityManager) contesto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = cm.getAllNetworkInfo();
        for (NetworkInfo ni : info) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                if (ni.isConnected()) {
                    connWifi = true;
                }
                if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                    if (ni.isConnected()) {
                        connMobile = true;
                    }
                }
            }
        }
        return connWifi || connMobile;
    }


}