package com.example.gaeta.myapplication;

import android.content.Intent;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.net.URL;


/**
 * PJDCC - Classe che modella l'Activity dove è presente la QRCodeReaderView per la scansione del QR.
 *         E' presente una classe innestata per il controllo dell'ID nel file JSON.
 *
 * @author Oranger Edoardo, Settembre Gaetano, Recchia Vito, Marchese Vito
 * @version 1.0
 */
public class SubActivity extends AppCompatActivity implements OnQRCodeReadListener {

	private QRCodeReaderView qrCodeReaderView;
    private String trovato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "Inquadra il QR Code", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        qrCodeReaderView = (QRCodeReaderView)findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        long autofocuslatency = 1500L;
        qrCodeReaderView.setAutofocusInterval(autofocuslatency);
        qrCodeReaderView.setQRDecodingEnabled(true);

    }

    /**
     * Medoto che viene chiamato quando il QR è codificato
     * @param  text : the text encoded in QR
     * @param points : points where QR control points are placed in View
     */
    @Override
	public void onQRCodeRead(String text, PointF[] points) {
        trovato = text;
        new JsonTask().execute("http://durresmuseum.altervista.org/CreazioneJsonOpere.php", trovato);
    }


    /**
     *  Metodo che permette di avviare l'Activity per la visualizzazione dell'opera.
     */
    private void runActivityOpera(String qr_letto) {
        Intent intent = new Intent(this, ActivityOpera.class);
		Bundle data = new Bundle();
        data.putString("qr_letto", qr_letto);
        intent.putExtras(data);
        startActivity(intent);
    }

    /**
     *  SubActivity.JsonTask - Classe che recupera i dati dal file Json creato dal PHP del sito.
     *                         Ha un metodo che controlla se l'id del QR code è presente nel file Json.
     */
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

                String linea = reader.readLine();
                while(linea!= null){
                    stringBuffer.append(linea);
                    linea = reader.readLine();
                }

                String finalJSON = stringBuffer.toString();

                StringBuffer finalString = new StringBuffer();

                JSONObject jsonObject = new JSONObject(finalJSON);
                JSONArray jsonArray = jsonObject.getJSONArray("opere");

                int jsonLength = jsonArray.length();
                for(int i = 0; i < jsonLength; i++) {

                    JSONObject finalObject = jsonArray.getJSONObject(i);
                    //Se il codice QR letto e la stringa dell' array sono uguali ...
                    if (params[1].equals(finalObject.getString("ID"))) {
                        flag = true;
                    }
                }
                return flag;

            } catch (IOException | JSONException e) {
                Log.e("Errore di I/O",e.toString());
            } finally {
                if(conn != null) {
                    conn.disconnect();
                }
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("Errore di I/O", e.toString());
                    }
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
                toastError();
            }
        }
    }

    /** Medoto per la visualizzazione del toast nel caso di QR non valido */
    private void toastError(){
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