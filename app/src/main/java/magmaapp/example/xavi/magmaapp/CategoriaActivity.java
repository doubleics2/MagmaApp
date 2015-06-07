package magmaapp.example.xavi.magmaapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoriaActivity extends Activity {

    private ListView llista;
    private Map<String,ArrayList<String>> mapTalles = new HashMap<String,ArrayList<String>>();

    static Button notifCount;
    static int mNotifCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.llistat);

        //Ens retorna la categoria seleccionada a MainActivity
        Bundle extras = getIntent().getExtras();
        //mNotifCount = extras.getInt("count");
        //setNotifCount(mNotifCount);
        if (extras != null) {
            String categories = extras.getString("categories").toLowerCase();
            String genere = extras.getString("genere");
            //llistaImatges = extras.getStringArrayList("llistaimatges");
            Log.v("CategoriaActivity", genere);
            Log.v("CategoriaActivity", categories);
            /*if(llistaImatges==null) {
                DescarregaLlistaImatges taska = new DescarregaLlistaImatges();
                taska.execute("http://178.62.15.224:8080/taulaImatges");
            }*/
            new HttpAsyncTask().execute("http://178.62.15.224:8080/" + genere + "/" + categories);
        }
        setNotifCount(mNotifCount);
    }

    //ACTIONBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        View count = menu.findItem(R.id.action_shop).getActionView();
        notifCount = (Button) count.findViewById(R.id.notif_count);
        contador Cont = (contador)getApplication();
        mNotifCount=Cont.getContador();
        notifCount.setText(String.valueOf(mNotifCount));
        // Al apretar el botó mCategoria s'obren les diverses categories
        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriaActivity.this, CompraActivity.class);
                intent.putExtra("aporta", false);
                startActivity(intent);
            }


        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setNotifCount(int count){
        Bundle extras1 = getIntent().getExtras();
        count = extras1.getInt("count");
        mNotifCount = count;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_shop) {
            /*Intent intent = new Intent(CategoriaActivity.this, CompraActivity.class);
            intent.putExtra("aporta",false);
            startActivity(intent);*/
            Intent intent = new Intent(CategoriaActivity.this, CompraActivity.class);
            intent.putExtra("aporta",false);
            startActivity(intent);
            return false;
        }

        return super.onOptionsItemSelected(item);
    }
//--------------------------------------------------------------------------------------------------

    //CONNEXIÓ SERVER
    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayList<Producte> taulaConsulta = JSONtoArray(jArray);
            ArrayList<Producte> taulaConsultaNoRepetits;
            taulaConsultaNoRepetits = noMostrisRepetits(taulaConsulta);
            mostraLlista(taulaConsultaNoRepetits);
        }
    }

    private ArrayList<Producte> JSONtoArray(JSONArray jArray) {

        ArrayList<Producte> taulaConsulta = new ArrayList<Producte>();
        if(jArray.length()!=0){
        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject oneObject = jArray.getJSONObject(i);
                Producte producte = new Producte();

                producte.setCategoria(oneObject.getString("categoria"));
                producte.setTema(oneObject.getString("tema"));
                producte.setMarca(oneObject.getString("marca"));
                producte.setTalla(oneObject.getString("talla"));
                producte.setEan13(oneObject.getString("ean13"));
                producte.setCantidad(oneObject.getInt("cantidad"));
                producte.setPvp(oneObject.getDouble("pvp"));
                producte.setId_articulo(oneObject.getString("id_articulo"));

                taulaConsulta.add(i,producte);
            } catch (JSONException e) {
                // Oops
                Log.i("error", "taulaConsulta");
            }
        }
        }else Toast.makeText(this,"No hi ha productes per aquesta categoria",Toast.LENGTH_SHORT);
        return taulaConsulta;
    }

    private void mostraTaula(ArrayList<Producte> taulaConsulta) {
        for (int i = 0; i < taulaConsulta.size(); i++) {
            Log.i("categoria",taulaConsulta.get(i).getCategoria());
            Log.i("tema",taulaConsulta.get(i).getTema());
            Log.i("marca",taulaConsulta.get(i).getMarca());
            Log.i("talla",taulaConsulta.get(i).getTalla());
            Log.i("ean13",taulaConsulta.get(i).getEan13());
            Log.i("id_articulo",taulaConsulta.get(i).getId_articulo());
            //Log.i("cantidad",taulaConsulta.get(i).getCantidad());
            Log.i("pvp",taulaConsulta.get(i).getPvp().toString());
        }
    }
// elimina els elements repetits de la taulaconsulta guardant les talles dels mateixos productes en un mapa
    private ArrayList<Producte> noMostrisRepetits(ArrayList<Producte> taulaConsulta){
        ArrayList<Producte> noRepetits = new ArrayList<Producte>();
        for(int i=0; i<taulaConsulta.size(); i++){
            if(mapTalles.containsKey(taulaConsulta.get(i).getId_articulo())){
                mapTalles.get(taulaConsulta.get(i).getId_articulo()).add(taulaConsulta.get(i).getTalla()); //afegeix la talla del producte ja guardat al mapa
            } else{
                ArrayList<String> talles = new ArrayList<String>();
                talles.add(taulaConsulta.get(i).getTalla());
                mapTalles.put(taulaConsulta.get(i).getId_articulo(),talles); // afegeix un producte amb la primera de les seves talles
                noRepetits.add(taulaConsulta.get(i));
            }
        }
        return noRepetits;
    }

    private void mostraLlista(ArrayList<Producte> noRepetits){
        llista = (ListView) findViewById(R.id.ListView_llistat);
        llista.setAdapter(new Llista_adaptador(this, R.layout.activity_categoria, noRepetits));
        //------------------------------------------------------------------------------------------
        //Click a Producte, anem a ProducteActivity
        llista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                Producte producte = (Producte) pariente.getItemAtPosition(posicion);
                Intent intent = new Intent(CategoriaActivity.this, ProducteActivity.class);
                intent.putExtra("producte", (Parcelable)producte);
                intent.putExtra("talles", mapTalles.get(producte.getId_articulo())); // passem l'array de talles del producte seleccionat
                //intent.putExtra("producte",producte.getEan13());
                startActivity(intent);
                //CharSequence texto = "Seleccionado: " + elegido.get_textoDebajo();
            }
        });
    }
}
