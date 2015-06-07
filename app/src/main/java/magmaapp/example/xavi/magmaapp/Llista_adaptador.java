package magmaapp.example.xavi.magmaapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.example.xavi.magmaapp.R;

import java.util.ArrayList;
import java.util.Map;

public class Llista_adaptador extends BaseAdapter {

    private ArrayList<Producte> entradas;
    private int R_layout_IdView;
    private Context contexto;

    private int length_cat;

    public Llista_adaptador(Context contexto, int R_layout_IdView, ArrayList<Producte> entradas) {
        super();
        this.contexto = contexto;
        this.entradas = entradas;
        this.R_layout_IdView = R_layout_IdView;
    }

    @Override
    public View getView(int posicion, View view, ViewGroup pariente) {
        MyViewHolder holder;

        if (view == null) {
            holder = new MyViewHolder();
            LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
            holder.nomProducte = (TextView) view.findViewById(R.id.textView_nom_prod);
            holder.catProducte = (TextView) view.findViewById(R.id.textView_ref_prod);
            holder.preu = (TextView) view.findViewById(R.id.preu);
            holder.imatge = (ImageView) view.findViewById(R.id.imageView_image_prod);
            //holder.progress = (ProgressBar) view.findViewById(R.id.progressBar);
            view.setTag(holder);
        } else{
            holder = (MyViewHolder) view.getTag();
        }

        Producte producteActual = entradas.get(posicion);
        holder = configuraHolder(producteActual, holder);

        //view.setTag(holder);

        //mirar si el holder te un task de descarrega, la petem. Si != null, hi ha una tasca. M'ho carrego i surt un altre. Avisar de que ha acabat

        return view;
    }

    private MyViewHolder configuraHolder(Producte producteActual, MyViewHolder holder){
        holder.catProducte.setText(producteActual.getTema());
        holder.nomProducte.setText(producteActual.getCategoria());
        holder.preu.setText(producteActual.getPvp()+"€");
        holder.id_articulo = producteActual.getId_articulo();

        holder.context = null;

        //holder.imatge.setVisibility(View.GONE);
        if(holder.task != null){
            holder.task.cancel(true);
        }
        mostraMap(holder);
        holder.task = new DownloadImageTask(holder);
        holder.task.execute();
        return holder;
    }

    private void mostraMap(MyViewHolder holder) {
        for (Map.Entry<String, Bitmap> entry : holder.map.entrySet()) {
            Log.i("Llista adaptador map: Key = " + entry.getKey(), " Value = " + entry.getValue());
        }
    }

    @Override
    public int getCount() {
        return entradas.size();
    }

    @Override
    public Object getItem(int posicion) {
        return entradas.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }


    /** Devuelve cada una de las entradas con cada una de las vistas a la que debe de ser asociada
     * @param entrada La entrada que será la asociada a la view. La entrada es del tipo del paquete/handler
     * @param view View particular que contendrá los datos del paquete/handler
     */
    //public abstract void onEntrada (Object entrada, View view);

}
