package magmaapp.example.xavi.magmaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.xavi.magmaapp.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;


public class CompraActivity extends Activity {

    private Producte producte_sel = new Producte();

    private Double total = 0.0;

    private String fitxer = "llistacompra.obj";


    private ListView llista;

    private ArrayList<Producte> llistaCompra;

    private Button mCompra;

    static Button notifCount;
    static int mNotifCount = 0;

    //private contador Contador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.llista_compra);

        mCompra = (Button)findViewById(R.id.button_compra);
        

        restoreFromFile(fitxer);


        Bundle extras = getIntent().getExtras();

        if(extras.getBoolean("aporta")){
            producte_sel = extras.getParcelable("producte");
            llistaCompra.add(new Producte(producte_sel));
            saveToFile(fitxer);
        }

        if(llistaCompra.isEmpty()) {
            TextView Total_productes = (TextView) findViewById(R.id.preu_total);
            total = 0.0;
            Total_productes.setText("TOTAL " + total + "€  ");
            setNotifCount(0);

        }else mostraLlista(llistaCompra);




        mCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNotifCount!=0){
                    Intent intent = new Intent(CompraActivity.this, PagamentActivity.class);
                    intent.putExtra("llista compra", llistaCompra);
                    intent.putExtra("total", total);
                    startActivity(intent);
                } else Toast.makeText(CompraActivity.this,"El carretó està buit.",Toast.LENGTH_SHORT).show();
                //finish();
            }
        });
    }

    private void mostraLlista(final ArrayList<Producte> llistaCompra){
        llista = (ListView) findViewById(R.id.ListView_llistat_compra);
        final Llista_adaptador adapter = new Llista_adaptador(this, R.layout.activity_compra, llistaCompra);
        llista.setAdapter(adapter);
        TextView Total_productes = (TextView) findViewById(R.id.preu_total);
        //------------------------------------------------------------------------------------------
        //Deslizar item para borrarlo
        SwipeListViewTouchListener touchListener =new SwipeListViewTouchListener(llista,new SwipeListViewTouchListener.OnSwipeCallback() {
            @Override
            public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {
                //Aqui ponemos lo que hara el programa cuando deslizamos un item ha la izquierda
                llistaCompra.remove(reverseSortedPositions[0]);
                if(!llistaCompra.isEmpty()) actualitza_total(llistaCompra);
                else {
                    TextView Total_productes = (TextView) findViewById(R.id.preu_total);
                    total = 0.0;
                    Total_productes.setText("TOTAL " + total + "€  ");
                    setNotifCount(0);
                }
                Log.i("TOTAL", total.toString());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onSwipeRight(ListView listView, int [] reverseSortedPositions) {
                //Aqui ponemos lo que hara el programa cuando deslizamos un item ha la derecha
                llistaCompra.remove(reverseSortedPositions[0]);
                if(!llistaCompra.isEmpty()) actualitza_total(llistaCompra);
                else {
                    TextView Total_productes = (TextView) findViewById(R.id.preu_total);
                    total = 0.0;
                    Total_productes.setText("TOTAL " + total + "€  ");
                    setNotifCount(0);

                }
                Log.i("TOTAL", total.toString());
                adapter.notifyDataSetChanged();
            }
        },true, false);

        //Escuchadores del listView
        llista.setOnTouchListener(touchListener);
        llista.setOnScrollListener(touchListener.makeScrollListener());

        if(llistaCompra!=null) actualitza_total(llistaCompra);
        else {
            total = 0.0;
            Total_productes.setText("TOTAL " + total + "€  ");
        }
        Log.i("TOTAL", total.toString());
    }


    private void actualitza_total(ArrayList<Producte> taulaConsulta) {
        total = 0.0;
        TextView Total_productes = (TextView) findViewById(R.id.preu_total);
        for (int i = 0; i < taulaConsulta.size(); i++) {
            total += taulaConsulta.get(i).getPvp();
            int xifres;
            if(total < 10.0) xifres = 4;
            else if (total >= 100.0) xifres = 6;
            else xifres = 5;
            Total_productes.setText("TOTAL " + String.format("%2f",total).substring(0,xifres) + "€  ");
            setNotifCount(i+1);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveToFile(fitxer);
        super.onSaveInstanceState(outState);
        Log.i("fitxer", "onSaveInstanceState!");
    }

    @Override
    protected void onPause() {
        saveToFile(fitxer);
        super.onPause();
        Log.i("fitxer", "onPause!");
    }

    private void restoreFromFile(String filename) {
        FileInputStream fis;
        try {
            fis = openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            llistaCompra = (ArrayList<Producte>) ois.readObject();
            fis.close();
        } catch (FileNotFoundException e) {
            llistaCompra = new ArrayList<Producte>();
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            llistaCompra = new ArrayList<Producte>();
            e.printStackTrace();
        } catch (IOException e) {
            llistaCompra = new ArrayList<Producte>();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            llistaCompra = new ArrayList<Producte>();
            e.printStackTrace();
        }
    }

    private void saveToFile(String filename) {
        FileOutputStream fos,fos1;
        try {
            fos = openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(llistaCompra);
            fos1 = openFileOutput("contador.obj", Context.MODE_PRIVATE);
            fos1.write(mNotifCount);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("fitxer", "error al guardar IO!");
            e.printStackTrace();
        }
    }
// ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_compra, menu);
        View count = menu.findItem(R.id.action_shop).getActionView();
        notifCount = (Button) count.findViewById(R.id.notif_count);
        notifCount.setText(String.valueOf(mNotifCount));
        return super.onCreateOptionsMenu(menu);
        //return true;
    }

    private void setNotifCount(int count){
        mNotifCount = count;
        invalidateOptionsMenu();
        contador Cont = (contador)getApplication();
        Cont.setContador(mNotifCount);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                /*Intent intent = new Intent(CompraActivity.this, CategoriaActivity.class);
                intent.putExtra("count", mNotifCount);
                startActivity(intent);*/
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void finish() {
        // Prepare data intent
        Intent intent = new Intent();
        intent.putExtra("count",mNotifCount);
        setResult(RESULT_OK, intent);

        super.finish();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}