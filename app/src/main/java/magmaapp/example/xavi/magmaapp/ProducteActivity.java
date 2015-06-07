package magmaapp.example.xavi.magmaapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
//import com.example.xavi.magmaapp.R;

import java.util.ArrayList;

public class ProducteActivity extends Activity {

    private Button mCompra;
    private Producte producte_sel = new Producte();
    private Spinner spinner;
    private MyViewHolder holder;
    private ImageButton imatge;
    private BitmapDrawable imatge_bitmap;

    static Button notifCount;
    static int mNotifCount = 0;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producte);

        mCompra = (Button)findViewById(R.id.button_afegir);
        imatge = (ImageButton)findViewById(R.id.imageProducte);
        //Intent--> Ens diu la ref. del producte seleccionat
        Bundle extras = getIntent().getExtras();
        configuraSpinner(extras);
        producte_sel = extras.getParcelable("producte");
        //String producte = extras.getString("producte");
        holder = new MyViewHolder();

        holder.nomProducte = (TextView) findViewById(R.id.nom_producte);
        holder.catProducte = (TextView) findViewById(R.id.tema);
        holder.preu = (TextView) findViewById(R.id.preu);
        holder.imatge = (ImageButton) findViewById(R.id.imageProducte);
        //holder.imatge_bitmap = (SubsamplingScaleImageView) findViewById(R.id.big_image);
        //Log.i("holder task abans", ""+holder.task);
        //holder.imatge_bitmap = holder.getImatge_bitmap();


        holder = configuraHolder(producte_sel, holder);
        //Log.i("holder abans", ""+holder.descarregada);

        //new DownloadImageTask(holder).onPreExecute();
        DownloadImageTask taska = new DownloadImageTask(holder);
        //Log.i("holder abans", "" + taska);
        taska.execute();
        //Log.i("holder despres", ""+holder.descarregada);
        //Log.i("holder despres", "" + taska);
        // Anem a Compra Activity

        mCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });

        //SubsamplingScaleImageView BigImage = (SubsamplingScaleImageView) findViewById(R.id.big_image);
        //ImageView BigImage = (ImageView) dialog.findViewById(R.id.big_image);
        //BigImage.setImage(ImageSource.bitmap(holder.imatge_bitmap));
        //BigImage.setImage(ImageSource.resource(R.drawable.nena3));
        //findViewById(R.id.loading).setVisibility(View.GONE);
        imatge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final Dialog dialog = new Dialog(context);
                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //dialog.setCancelable(false);
                imatge.setVisibility(View.INVISIBLE);
                //dialog.setContentView(R.layout.dialog_custom);

                //set big image
                //Button btnClose = (Button) dialog.findViewById(R.id.btn_closebigimage);

                SubsamplingScaleImageView BigImage1 = (SubsamplingScaleImageView) findViewById(R.id.big_image);
                BigImage1.setImage(ImageSource.bitmap(holder.imatge_bitmap));
/*
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        dialog.dismiss();
                    }
                });

                dialog.show();
                */
            }
        });

    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------

    private MyViewHolder configuraHolder(Producte producte_sel, MyViewHolder holder){
        //holder.catProducte.setText(producte_sel.getTema());
        holder.nomProducte.setText(producte_sel.getCategoria());
        holder.catProducte.setText(producte_sel.getTema());
        holder.preu.setText(producte_sel.getPvp()+"0 €");
        holder.id_articulo = producte_sel.getId_articulo();
        //holder.imatge_bitmap = holder.getImatge_bitmap();
        holder.context=context;

        //holder.imatge.setVisibility(View.GONE);
        return holder;
    }

    private void configuraSpinner(Bundle extras){
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayList<String> llistaTalles = new ArrayList<String>();
        ArrayList<String> llistaIntent;
        llistaIntent = extras.getStringArrayList("talles");
        llistaTalles.add("- SELECCIONA LA TALLA -");
        for(int i=0; i<llistaIntent.size(); i++){
            if(!llistaTalles.contains(llistaIntent.get(i))) {
                llistaTalles.add(llistaIntent.get(i));
            }
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, llistaTalles);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptador);
        spinner.setPopupBackgroundResource(R.drawable.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position)!="- SELECCIONA LA TALLA -"){
                    Intent intent = new Intent(ProducteActivity.this, CompraActivity.class);
                    String talla = parent.getItemAtPosition(position).toString();
                    producte_sel.setTalla(talla);
                    intent.putExtra("aporta",true);
                    intent.putExtra("producte",(Parcelable)producte_sel);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ProducteActivity.this, "No s'ha seleccionat res", Toast.LENGTH_SHORT).show();
            }
        });
    }

//ACTIONBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                Intent intent = new Intent(ProducteActivity.this, CompraActivity.class);
                intent.putExtra("aporta", false);
                startActivity(intent);
            }


        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_shop) {
            Intent intent = new Intent(ProducteActivity.this, CompraActivity.class);
            intent.putExtra("aporta",false);
            startActivity(intent);
            return false;
        }else finish();
        return true;
    }
//--------------------------------------------------------------------------------------------------

}
