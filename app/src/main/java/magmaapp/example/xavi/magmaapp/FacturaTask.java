package magmaapp.example.xavi.magmaapp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class FacturaTask extends AsyncTask<String, Void, String> {
    public Factura factura;

    public FacturaTask(Factura factura) {
        this.factura = factura;
    }
    @Override
    protected String doInBackground(String... urls) {

        return POST(urls[0]);
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
    }
    public String POST(String url){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("codiPostal", factura.getCp()));
        nameValuePairs.add(new BasicNameValuePair("adresa", factura.getAdresa()));
        nameValuePairs.add(new BasicNameValuePair("descripcio", factura.getDescripcio()));
        nameValuePairs.add(new BasicNameValuePair("preu", factura.getPreu()));
        nameValuePairs.add(new BasicNameValuePair("telefon", factura.getTelefon()));
        nameValuePairs.add(new BasicNameValuePair("nom", factura.getNom()));
        nameValuePairs.add(new BasicNameValuePair("poblacio", factura.getPoblacio()));
        nameValuePairs.add(new BasicNameValuePair("correu", factura.getCorreu()));

        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpclient.execute(httppost);

        }catch(Exception e){
            Log.e("error", "Error in http connection " + e.toString());
        }
        return null;
    }
}