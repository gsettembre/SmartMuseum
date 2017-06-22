package com.example.gaeta.myapplication;

import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Classe che modella l'Activity dove viene visualizzata la scheda
 * dopo la decodifica del relativo codice QR.
 * Vengono visualizzare tutte le informazioni, l'immagine se presente e il bottone
 * per l'avvio della sintesi vocale della descrizione.
 *
 * @author Oranger Edoardo, Settembre Gaetano, Recchia Vito, Marchese Vito
 * @version 1.0
 */
public class ActivityOpera extends AppCompatActivity implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    private TextToSpeech textToSpeech;
    private TextView descrizione;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opera);


        textToSpeech = new TextToSpeech(ActivityOpera.this, ActivityOpera.this);

        final ImageButton speech = (ImageButton)findViewById(R.id.speech);

        speech.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!textToSpeech.isSpeaking()){
                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");
                    textToSpeech.speak(descrizione.getText().toString(),TextToSpeech.QUEUE_ADD,map);
                    speech.setImageResource(R.drawable.pause);
                }else{
                    textToSpeech.stop();
                    speech.setImageResource(R.drawable.onaudio);
                }
            }
        });

        String passato = getIntent().getExtras().getString("qr_letto");

            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions).build();
            ImageLoader.getInstance().init(config);
            new JsonTask().execute("http://durresmuseum.altervista.org/CreazioneJsonOpere.php", passato);
    }

    /** Override del metodo dell'interfaccia per il TextToSpeech */
    @Override
    public void onInit(int status) {
        textToSpeech.setOnUtteranceCompletedListener(this);
    }

    /** Override del metodo dell'interfaccia per il TextToSpeech al completamento della lettura */
    @Override
    public void onUtteranceCompleted(String utteranceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageButton speech1 = (ImageButton)findViewById(R.id.speech);
                speech1.setVisibility(Button.VISIBLE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
        }
        super.onDestroy();
    }

    /**
     *  ActivityOpera.JsonTask - Classe che recupera i dati dal file Json creato dal PHP del sito.
     *                           nel file Json sono presenti le opere del museo.
     */
    private class JsonTask extends AsyncTask<String,String,Opere> {

        @Override
        protected Opere doInBackground(String... params) {
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

                Opere opere = new Opere();
                JSONObject jsonObject = new JSONObject(finalJSON);
                JSONArray jsonArray = jsonObject.getJSONArray("opere");

                int jsonLength = jsonArray.length();
                for(int i = 0; i < jsonLength; i++) {

                    JSONObject finalObject = jsonArray.getJSONObject(i);
                    //Se il codice QR letto e la stringa dell' array sono uguali ...
                    if (params[1].equals(finalObject.getString("ID"))) {
                        opere.setID(finalObject.getString("ID"));
                        opere.setTitolo(finalObject.getString("Nome"));
                        opere.setAutore(finalObject.getString("Autore"));
                        opere.setCorrente(finalObject.getString("Corrente_artistica"));
                        opere.setAnno(finalObject.getString("Anno_realizzazione"));
                        opere.setCategoria(finalObject.getString("Categoria"));
                        opere.setDimensioni(finalObject.getString("Dimensioni"));
                        opere.setDescrizione(finalObject.getString("Descrizione"));
                        opere.setImmagine(finalObject.getString("Immagine"));
                    }
                }
                return opere;

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
            return null;

        }

        @Override
        protected void onPostExecute(Opere s) {
            super.onPostExecute(s);
            TextView titolo = (TextView)findViewById(R.id.titolo);
            TextView autore = (TextView)findViewById(R.id.autore);
            TextView corrente = (TextView)findViewById(R.id.corrente);
            TextView anno = (TextView)findViewById(R.id.anno);
            TextView categoria = (TextView)findViewById(R.id.categoria);
            TextView dimensioni = (TextView)findViewById(R.id.dimensione);
            descrizione = (TextView)findViewById(R.id.descrizione);
            ImageView image = (ImageView)findViewById(R.id.imageView3);

            titolo.setText(s.getTitolo());
            autore.setText(s.getAutore());
            corrente.setText(s.getCorrente());
            anno.setText(s.getAnno());
            categoria.setText(s.getCategoria());
            dimensioni.setText(s.getDimensioni());
            descrizione.setText(s.getDescrizione());
            ImageLoader.getInstance().displayImage(s.getImmagine(), image);

        }
    }
 }