package magmaapp.example.xavi.magmaapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.xavi.magmaapp.R;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;
import com.stripe.model.Charge;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PagamentActivity extends Activity {

    private ArrayList<Producte> llistaCompra;
    private Double preuTotal;
    private int Total;
    //private TextView aPagar;
    private TextView mNumTarjeta;
    private TextView mCvc;
    private TextView mMes;
    private TextView mAny;
    private TextView mNomCognom;
    private TextView mEmail;
    private TextView mPoblacio;
    private TextView mDireccio;
    private TextView mCP;

    private Dialog dialog;
    final Context context = this;

    private Factura factura = new Factura();

    private String fitxer = "llistacompra.obj";

    public static final String PUBLISHABLE_KEY = "pk_test_tyyI7BDb8SWb15j3T5K1uUN0";
    public static final String SECRET_KEY = "sk_test_oWJT9zwBJXjtMr2bZVGiU9VR";
    private Button paga;

    static private String nomcognom, email, numTarjeta, cvc, direccio, poblacio;
    private int mes,any;

    static Button notifCount;
    static int mNotifCount = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagament);


        mNomCognom = (TextView)findViewById(R.id.nom_cognoms);
        mEmail = (TextView)findViewById(R.id.email);
        mNumTarjeta = (TextView)findViewById(R.id.numTarjeta);
        mCvc = (TextView)findViewById(R.id.cvc);
        mMes = (TextView)findViewById(R.id.mes);
        mAny = (TextView)findViewById(R.id.any);
        mDireccio = (TextView)findViewById(R.id.direccio);
        mCP = (TextView)findViewById(R.id.cp);
        mPoblacio = (TextView)findViewById(R.id.poblacio);
        paga = (Button)findViewById(R.id.paga);

        nomcognom = mNomCognom.toString();

        restoreFromFile(fitxer);

        Bundle extras = getIntent().getExtras();
        preuTotal = extras.getDouble("total")*100;
        Total = preuTotal.intValue();
        String pf = ""+Total;
        Log.i("preu centims",pf);

        //preuTotal=300;
        int xifres;
        if(extras.getDouble("total") < 10.0) xifres = 4;
        else if (extras.getDouble("total") >= 100.0) xifres = 6;
        else xifres = 5;
        paga.setText("CONFIRMA (" + String.format("%2f",extras.getDouble("total")).substring(0,xifres) + "€)");

        paga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(    ((mNomCognom.getText().toString()).equals("")) || (mEmail.getText().toString().equals(""))
                        || (mNumTarjeta.getText().toString().equals("")) || ((mMes.getText().toString()).equals(""))
                        || (mAny.getText().toString().equals("")) || (mCvc.getText().toString().equals(""))
                        || ((mDireccio.getText().toString()).equals("")) || (mCP.getText().toString().equals(""))
                        || (mPoblacio.getText().toString().equals(""))) {
                    Toast.makeText(PagamentActivity.this, "Omple tots els camps", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PagamentActivity.this);
                    builder.setTitle("Confirmacio de la compra")
                            .setMessage("Estas segur?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    numTarjeta = mNumTarjeta.getText().toString();
                                    mes = Integer.parseInt(mMes.getText().toString());
                                    any = Integer.parseInt(mAny.getText().toString());
                                    cvc = mCvc.getText().toString();
                                    configuraTargeta(numTarjeta,mes,any,cvc,preuTotal/100);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();
                }
            }
        });

    }

    private void configuraTargeta(String numTarjeta, int mes, int any, String cvc, final double preuTotal) {

        progressbar();
        Card tarjeta = new Card(numTarjeta, mes, any, cvc);

        if(!tarjeta.validateNumber()){
            Toast.makeText(this,"Numero incorrecte",Toast.LENGTH_SHORT).show();
        }
        if(!tarjeta.validateCVC()){
            Toast.makeText(this,"CVC incorrecte",Toast.LENGTH_SHORT).show();
        }
        if(!tarjeta.validateCard()){
            Toast.makeText(this,"Tarjeta incorrecte",Toast.LENGTH_SHORT).show();
        }

        Stripe stripe = null;
        try {
            stripe = new Stripe(PUBLISHABLE_KEY);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        final Map<String, Object> chargeParams = new HashMap<String, Object>();
        if (stripe != null) {
            stripe.createToken(tarjeta, new TokenCallback() {
                        public void onSuccess(Token token) {
                            // Send token to your server
                            chargeParams.put("amount",preuTotal);
                            chargeParams.put("currency", "eur");
                            chargeParams.put("card", token.getId());
                            //chargeParams.put("captured",true);
                            new AsyncTask<Void, Void, Void>() {

                                Charge charge;

                                @Override
                                protected Void doInBackground(Void... params) {
                                    try {
                                        com.stripe.Stripe.apiKey = SECRET_KEY;
                                        charge = Charge.create(chargeParams);
                                        charge.capture(chargeParams);
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    return null;
                                }
                                protected void onPostExecute(Void result) {
                                    /*Toast.makeText(PagamentActivity.this,
                                            "Card Charged : " + charge.getCreated() + "\nPaid : " +charge.getPaid(),
                                            Toast.LENGTH_LONG
                                    ).show();*/

                                    Toast.makeText(PagamentActivity.this,
                                            "Compra realitzada amb exit!",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    factura.setCp(mCP.getText().toString());
                                    factura.setAdresa(mDireccio.getText().toString());
                                    factura.setCorreu(mEmail.getText().toString());
                                    String descripcio = "";
                                    for(int i = 0; i < llistaCompra.size(); i++){
                                        descripcio += llistaCompra.get(i).getId_articulo() +" ";
                                        Log.i("Llista compra id: ", llistaCompra.get(i).getId_articulo());
                                        Log.i("Productes triats: ", descripcio);
                                    }
                                    factura.setDescripcio(descripcio);
                                    factura.setNom(mNomCognom.getText().toString());
                                    factura.setPoblacio(mPoblacio.getText().toString());
                                    factura.setPreu(String.valueOf(preuTotal));

                                    FacturaTask task = new FacturaTask(factura);
                                    task.execute("http://178.62.15.224:8080/inserta");
                                    setNotifCount(0);
                                    llistaCompra.clear();
                                    saveToFile(fitxer);
                                };
                            }.execute();
                            setNotifCount(0);
                            finish();
                            Intent intent = new Intent(PagamentActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                        public void onError(Exception error) {
                            Log.i("error", "crack");
                            // Show localized error message
                            // Toast.makeText(getContext(), "error",Toast.LENGTH_LONG).show();
                        }
                    }
            );
        }

    }

    private void progressbar() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_custom);
        dialog.show();
    }


//--------------------------------------------------------------------------------------------------

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



    //ACTIONBAR

    private void setNotifCount(int count){
        mNotifCount = count;
        invalidateOptionsMenu();
        contador Cont = (contador)getApplication();
        Cont.setContador(mNotifCount);
    }

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
            Intent intent = new Intent(PagamentActivity.this, CompraActivity.class);
            intent.putExtra("aporta", false);
            startActivity(intent);
        }


    });
    return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_shop) {
            Intent intent = new Intent(PagamentActivity.this, CompraActivity.class);
            intent.putExtra("aporta",false);
            startActivity(intent);
            return false;
        }

        return super.onOptionsItemSelected(item);
    }


}
