package magmaapp.example.xavi.magmaapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MyViewHolder extends Object{
    public TextView getNomProducte() {
        return nomProducte;
    }

    public void setNomProducte(TextView nomProducte) {
        this.nomProducte = nomProducte;
    }

    public TextView getCatProducte() {
        return catProducte;
    }

    public void setCatProducte(TextView catProducte) {
        this.catProducte = catProducte;
    }

    public TextView getPreu() {
        return preu;
    }

    public void setPreu(TextView preu) {
        this.preu = preu;
    }

    public ImageView getImatge() {
        return imatge;
    }

    public void setImatge(ImageView imatge) {
        this.imatge = imatge;
    }

    public ProgressBar getProgress() {
        return progress;
    }

    public void setProgress(ProgressBar progress) {
        this.progress = progress;
    }

    public String getId_articulo() {
        return id_articulo;
    }

    public void setId_articulo(String id_articulo) {
        this.id_articulo = id_articulo;
    }

    public Bitmap getImatge_bitmap() {
        return imatge_bitmap;
    }

    public void setImatge_bitmap(Bitmap imatge_bitmap) {
        this.imatge_bitmap = imatge_bitmap;
    }

    public Map<String, Bitmap> getMap() {
        return map;
    }

    public void setMap(Map<String, Bitmap> map) {
        this.map = map;
    }

    public TextView nomProducte;
    public TextView catProducte;
    public TextView preu;
    public ImageView imatge;
    public ProgressBar progress;
    public String id_articulo;
    public DownloadImageTask task;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context context;
    public Bitmap imatge_bitmap;
    public Map<String,Bitmap> map = new HashMap<String,Bitmap>();


}
