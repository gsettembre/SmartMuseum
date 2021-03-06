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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermessi();
                } else {
                    runSubActivity();
                }
            }
        });

    }

    /** Metodo per il controllo del permesso della fotocamera */
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

    /** Metodo per il controllo tra l'id del permesso e il permesso effettivo */
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

    /**
     * Metodo che controlla se è attiva una connessione ad internet.
     * @return booleano che controlla la presenza di una connesione.
     */
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
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (ni.isConnected()) {
                    connMobile = true;
                }
            }
        }
        return connWifi || connMobile;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mi =getMenuInflater();
        mi.inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Info SmartMuseum");
            builder.setMessage("Versione 1.0.0\n\nCopyright © 2016-2017\nEdoardo Oranger, Gaetano Settembre, Vito Marchese, Vito Recchia");
            AlertDialog d = builder.create();
            d.show();
        }
        return super.onOptionsItemSelected(item);
    }

}