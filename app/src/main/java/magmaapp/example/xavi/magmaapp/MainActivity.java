package magmaapp.example.xavi.magmaapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;

//import com.example.xavi.magmaapp.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import android.R;


public class MainActivity extends Activity {

    private ImageButton mCategoria;

    //Variables Desplegable
    private Spinner spinner1;
    private List<String> llista;
    private String categories;
    private String genere;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    static Button notifCount;
    private int mNotifCount=0;

    private ArrayList<String> llistaImatges;

    private String fitxer_cont = "contador.obj";
    //private String fitxer = "llista_imatges.obj";

    //private DescarregaLlistaImatges taska;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().setDisplayShowTitleEnabled(false);


        //Cridem la funció categories
        //categories();
        restoreFromFile(fitxer_cont);


        /*if(llistaImatges.isEmpty()) {
            Toast.makeText(MainActivity.this, "Descarregant...", Toast.LENGTH_SHORT).show();
            taska = new DescarregaLlistaImatges();
            taska.execute("http://178.62.15.224:8080/taulaImatges");
            saveToFile(fitxer);
            restoreFromFile(fitxer);
        } else Toast.makeText(MainActivity.this, "Descarregat", Toast.LENGTH_SHORT).show();
        //mCategoria = (ImageButton)findViewById(R.id.img_cat);

        // Al apretar el botó mCategoria s'obren les diverses categories
        /*mCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obre el spinner al clicar la imatge
                //spinner1.performClick();
                genere = "nena";

            }


        });*/
        //notifCount = (Button) count.findViewById(R.id.notif_count);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableListView);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

       /* // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });*/

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                //if(llistaImatges!=null){
                Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
                categories = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                genere = listDataHeader.get(groupPosition);
                intent.putExtra("categories", categories);
                intent.putExtra("genere", genere);
                intent.putStringArrayListExtra("llistaimatges", llistaImatges);
                startActivity(intent);
                // TODO Auto-generated method stub
                /*Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();*/
                //}else Toast.makeText(MainActivity.this, "Carregant...",Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }


//Tractament de l'ActionBAr
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //restoreFromFile(fitxer_cont);

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
                Intent intent = new Intent(MainActivity.this, CompraActivity.class);
                intent.putExtra("aporta",false);
                startActivity(intent);
            }


        });
        return super.onCreateOptionsMenu(menu);


        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();



        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_shop) {
            Intent intent = new Intent(MainActivity.this, CompraActivity.class);
            intent.putExtra("aporta",false);
            startActivity(intent);
            return true;
        }*/



        return super.onOptionsItemSelected(item);
    }


//--------------------------------------------------------------------------------------------------
/*
    //Funció que obre el desplegable de categories i al triar-ne una es va a CategoriaActivity
    private void categories() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        llista = new ArrayList<String>();
        spinner1 = (Spinner) this.findViewById(R.id.spinner1);
        //Afegim a la llista les diverses categories
        llista.add("");
        llista.add("jaquetes");//Mixte
        llista.add("jerseis");//
        llista.add("camises");//Mixte
        llista.add("pantalons");//
        llista.add("vestits");//
        llista.add("faldilles");//
        llista.add("accessoris");//
        llista.add("banyadors");

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, llista);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adaptador);

        //Tractem la categories seleccionada, depenent de la categories seleccionem els productes
        spinner1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(parent.getItemAtPosition(position)!=""){
                            Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
                            categories = parent.getItemAtPosition(position).toString();
                            intent.putExtra("categories",categories);
                            intent.putExtra("genere", genere);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(MainActivity.this, "No s'ha seleccionat res", Toast.LENGTH_SHORT).show();

                    }
                }
        );
    }
*/
//--------------------------------------------------------------------------------------------------

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("bebeF");
        listDataHeader.add("bebeM");
        listDataHeader.add("nena");
        listDataHeader.add("nen");
        listDataHeader.add("juniorF");
        listDataHeader.add("juniorM");

        List<String> categories_bebeF = new ArrayList<String>();
        categories_bebeF.add("Camises");
        categories_bebeF.add("Jaquetes");
        categories_bebeF.add("Pantalons");
        categories_bebeF.add("Faldilles");
        categories_bebeF.add("Vestits");
        categories_bebeF.add("Banyadors");

        List<String> categories_bebeM = new ArrayList<String>();
        categories_bebeM.add("Camises");
        categories_bebeM.add("Jaquetes");
        categories_bebeM.add("Jerseis");
        categories_bebeM.add("Pantalons");
        categories_bebeM.add("Vestits");

        List<String> categories_nen = new ArrayList<String>();
        categories_nen.add("Camises");
        categories_nen.add("Jaquetes");
        categories_nen.add("Jerseis");
        categories_nen.add("Pantalons");
        categories_nen.add("Banyadors");

        List<String> categories_nena = new ArrayList<String>();
        categories_nena.add("Camises");
        categories_nena.add("Jaquetes");
        categories_nena.add("Jerseis");
        categories_nena.add("Pantalons");
        categories_nena.add("Faldilles");
        categories_nena.add("Vestits");
        categories_nena.add("Banyadors");
        //categories_nena.add("accessoris");

        List<String> categories_juniorM = new ArrayList<String>();
        categories_juniorM.add("Camises");
        categories_juniorM.add("Jaquetes");
        categories_juniorM.add("Jerseis");
        categories_juniorM.add("Pantalons");
        categories_juniorM.add("Banyadors");
        //categories_nena.add("accessoris");

        List<String> categories_juniorF = new ArrayList<String>();
        categories_juniorF.add("Camises");
        categories_juniorF.add("Jaquetes");
        categories_juniorF.add("Jerseis");
        categories_juniorF.add("Pantalons");
        categories_juniorF.add("Faldilles");
        categories_juniorF.add("Vestits");
        categories_juniorF.add("Banyadors");
        //categories_juniorF.add("accesProductesoris");


        listDataChild.put(listDataHeader.get(0), categories_bebeF); // Header, Child data
        listDataChild.put(listDataHeader.get(1), categories_bebeM);
        listDataChild.put(listDataHeader.get(2), categories_nena);
        listDataChild.put(listDataHeader.get(3), categories_nen);
        listDataChild.put(listDataHeader.get(4), categories_juniorF);
        listDataChild.put(listDataHeader.get(5), categories_juniorM);
    }

//--------------------------------------------------------------------------------------------------
    //DESCARREGAR LLISTA IMATGES
    /*public void setLlista(ArrayList<String> llista){
        llistaImatges = llista;
    }

    private class DescarregaLlistaImatges extends AsyncTask<String, Void, ArrayList<String>> {

        protected ArrayList<String> doInBackground(String... urls) {
            ArrayList<String> result = new ArrayList<>();
            try {
                URL url = new URL(urls[0]);

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str;
                while ((str = in.readLine()) != null) {
                    result.add(str);
                }
                in.close();
            } catch (MalformedURLException e) {
                Log.i("malformed", "fail...");
            } catch (IOException e) {
                Log.i("IOException",e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            MainActivity.this.setLlista(result);
        }
    }*/

//--------------------------------------------------------------------------------------------------
    private void restoreFromFile(String filename) {
        FileInputStream fis;
        try {
            fis = openFileInput(filename);
            mNotifCount = fis.read();
            contador Cont = (contador) getApplication();
            Cont.setContador(mNotifCount);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
    private void saveToFile(String filename) {
        FileOutputStream fos;
        try {
            fos = openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(llistaImatges);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("fitxer", "error al guardar IO!");
            e.printStackTrace();
        }
    }
*/

}
