package magmaapp.example.xavi.magmaapp;

import android.app.Application;

/**
 * Created by xavi on 13/05/15.
 */
public class contador extends Application {


    private int contador = 0;


    public int getContador() {
        return this.contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }



}
