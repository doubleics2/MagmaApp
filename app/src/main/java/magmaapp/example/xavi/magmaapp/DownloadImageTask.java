package magmaapp.example.xavi.magmaapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private ProgressBar progressBar;
    private MyViewHolder holder;

    //ProgressDialog dialog;
    private Dialog dialog;

    public DownloadImageTask(MyViewHolder holder) {
        this.holder = holder;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(holder.context!=null) {
            dialog = new Dialog(holder.context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_custom);
            dialog.show();

        }
    }

    protected Bitmap doInBackground(String... urls) {
        //mirar si el id_articulo esta dins del map, sino creas la url i creas el imatgeBitmap
        String url;
        if(holder.context!=null) url ="http://178.62.15.224:8000/imatgesNovesDetails/" + holder.getId_articulo() +".jpg";
        else url ="http://178.62.15.224:8000/imatgesEscaladesDetails/" + holder.getId_articulo() +".jpg";

        Bitmap imatgeBitmap = null;
        Log.i("map:", holder.getId_articulo());

        mostraMap();

        if(holder.map.containsKey(holder.getId_articulo())){
            imatgeBitmap = holder.map.get(holder.getId_articulo());
            Log.i("foto guardada", "si");
        } else{
            try {
                InputStream in = new URL(url).openStream();
                imatgeBitmap = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                Log.i("error de descarga", "si");
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                Log.i("error de IO", "si");
            }
            holder.map.put(holder.getId_articulo(),imatgeBitmap);
            Log.i("foto descargada", "si");
        }
        return imatgeBitmap;
    }

    private void mostraMap() {
        for (Map.Entry<String, Bitmap> entry : holder.map.entrySet()) {
            Log.i("Map downloadImatge task: Key = " + entry.getKey()," Value = " + entry.getValue());
        }
    }

    protected void onPostExecute(Bitmap result) {
        if(dialog!=null)dialog.dismiss();
        if(result != null){
            holder.imatge.setImageBitmap(result);
            //holder.imatge_bitmap = result;
            holder.setImatge_bitmap(result);
                       //progressBar.setVisibility(View.GONE);
            holder.task = null;
            holder.imatge.setVisibility(View.VISIBLE);
        } else
            holder.imatge.setVisibility(View.INVISIBLE);
        //progressBar.setVisibility(View.VISIBLE);
    }
}