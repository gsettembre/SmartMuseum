package com.example.gaeta.myapplication;

import android.content.Intent;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SubActivity extends AppCompatActivity implements OnQRCodeReadListener {

	private QRCodeReaderView qrCodeReaderView;
    public String trovato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "Inquadra il QR Code", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);


        qrCodeReaderView.setAutofocusInterval(1500L);
        qrCodeReaderView.setQRDecodingEnabled(true);

    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
	@Override
	public void onQRCodeRead(String text, PointF[] points) {
        trovato = text;
        new JsonTask().execute("http://durresmuseum.altervista.org/CreazioneJsonOpere.php", trovato);
    }

    private void runActivityOpera(String qr_letto) {
        Intent intent = new Intent(this, ActivityOpera.class);
		Bundle data = new Bundle();
        data.putString("qr_letto", qr_letto);
        intent.putExtras(data);
        startActivity(intent);
    }


    private class JsonTask extends AsyncTask<String,String,Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            boolean flag = false;
            BufferedReader reader = null;
            HttpURLConnection conn = null;
            try {
                URL url = new URL(params[0]);
                conn = (HttpURLConnection)url.openConnection();
                conn.connect();
                InputStream stream = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer stringBuffer = new StringBuffer();
                String linea;
                while((linea = reader.readLine())!= null){
                    stringBuffer.append(linea);
                }

                String finalJSON = stringBuffer.toString();

                StringBuffer finalString = new StringBuffer();

                JSONObject jsonObject = new JSONObject(finalJSON);
                JSONArray jsonArray = jsonObject.getJSONArray("opere");

                for(int i = 0; i < jsonArray.length(); i++) {

                    JSONObject finalObject = jsonArray.getJSONObject(i);
                    //Se il codice QR letto e la stringa dell' array sono uguali ...
                    if (params[1].equals(finalObject.getString("ID"))) {
                        flag = true;
                    }
                }
                return flag;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if(conn != null)
                    conn.disconnect();
                if(reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            return flag;


        }

        @Override
        protected void onPostExecute(Boolean found) {
            super.onPostExecute(found);
            if(found != false)
            {
                qrCodeReaderView.setQRDecodingEnabled(false);
                runActivityOpera(trovato);
                qrCodeReaderView.setQRDecodingEnabled(true);
            }
            else {
                ToastError();
            }
        }
    }


    private void ToastError(){
        Toast.makeText(this, "QR Code non valido", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

}