package com.example.gaeta.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gaeta.myapplication.classOpere.opere;
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
import java.net.MalformedURLException;
import java.net.URL;


public class ActivityOpera extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opera);

        String passato = getIntent().getExtras().getString("qr_letto");


            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions).build();
            ImageLoader.getInstance().init(config);
            new JsonTask().execute("http://durresmuseum.altervista.org/CreazioneJsonOpere.php", passato);
    }

    public class JsonTask extends AsyncTask<String,String,opere>{

        @Override
        protected opere doInBackground(String... params) {
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
                String linea = "";
                while((linea = reader.readLine())!= null){
                    stringBuffer.append(linea);
                }

                String finalJSON = stringBuffer.toString();

                StringBuffer finalString = new StringBuffer();

                opere opere = new opere();
                JSONObject jsonObject = new JSONObject(finalJSON);
                JSONArray jsonArray = jsonObject.getJSONArray("opere");

                for(int i = 0; i < jsonArray.length(); i++) {

                    JSONObject finalObject = jsonArray.getJSONObject(i);
                    //Se il codice QR letto e la stringa dell' array sono uguali ...
                    if (params[1].compareTo(finalObject.getString("ID")) == 0) {
                        opere.setID(finalObject.getString("ID"));
                        opere.setTitolo(finalObject.getString("Nome"));
                        opere.setAutore(finalObject.getString("Autore"));
                        opere.setCorrente(finalObject.getString("Corrente_artistica"));
                        opere.setAnno(finalObject.getString("Anno_realizzazione"));
                        opere.setCategoria(finalObject.getString("Categoria"));
                        opere.setDimensioni(finalObject.getString("Dimensioni"));
                        opere.setUbicazione(finalObject.getString("Ubicazione"));
                        opere.setDescrizione(finalObject.getString("Descrizione"));
                        opere.setImmagine(finalObject.getString("Immagine"));
                    }
                }
                return opere;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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
            return null;


        }

        @Override
        protected void onPostExecute(opere s) {
            super.onPostExecute(s);
            TextView titolo = (TextView)findViewById(R.id.titolo);
            TextView autore = (TextView)findViewById(R.id.autore);
            TextView corrente = (TextView)findViewById(R.id.corrente);
            TextView anno = (TextView)findViewById(R.id.anno);
            TextView categoria = (TextView)findViewById(R.id.categoria);
            TextView dimensioni = (TextView)findViewById(R.id.dimensione);
            TextView ubicazione = (TextView)findViewById(R.id.ubicazione);
            TextView id = (TextView)findViewById(R.id.id);
            ImageView image = (ImageView)findViewById(R.id.imageView3);

            titolo.setText(s.getTitolo());
            autore.setText(s.getAutore());
            corrente.setText(s.getCorrente());
            anno.setText(s.getAnno());
            categoria.setText(s.getCategoria());
            dimensioni.setText(s.getDimensioni());
            ubicazione.setText(s.getUbicazione());
            id.setText(s.getDescrizione());
            ImageLoader.getInstance().displayImage(s.getImmagine(), image);

        }
    }

}

