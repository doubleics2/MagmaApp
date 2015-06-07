package magmaapp.example.xavi.magmaapp;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Producte implements Parcelable, Serializable {

    private String categoria, tema, marca, talla, ean13, id_articulo;
    private double pvp;
    private int cantidad;

    public Producte(){
    }

    public Producte(Producte p){
        this.categoria = p.categoria;
        this.tema = p.tema;
        this.marca = p.marca;
        this.talla = p.talla;
        this.ean13 = p.ean13;
        this.cantidad = p.cantidad;
        this.pvp = p.pvp;
        this.id_articulo = p.id_articulo;
    }

    public Producte(String categoria, String tema, String marca, String talla, String ean13, int cantidad, Double pvp, String id_articulo){
        this.categoria = categoria;
        this.tema = tema;
        this.marca = marca;
        this.talla = talla;
        this.ean13 = ean13;
        this.cantidad = cantidad;
        this.pvp = pvp;
        this.id_articulo = id_articulo;
    }

    public String getCategoria() {return categoria;}
    public String getTema() {return tema;}
    public String getMarca() {return marca;}
    public String getTalla() {return talla;}
    public String getEan13() {return ean13;}
    public Double getPvp() {return pvp;}
    public String getId_articulo() {return id_articulo;}
    public int getCantidad() {return cantidad;}

    public void setCategoria(String categoria) {this.categoria = categoria;}
    public void setTema(String tema) {this.tema = tema;}
    public void setMarca(String marca) {this.marca = marca;}
    public void setTalla(String talla) {this.talla = talla;}
    public void setEan13(String ean13) {this.ean13 = ean13;}
    public void setPvp(Double pvp) {this.pvp = pvp;}
    public void setId_articulo(String id_articulo) {this.id_articulo = id_articulo;}
    public void setCantidad(int cantidad) {this.cantidad = cantidad;}

    public Producte(Parcel in){
        readFromParcel(in);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoria);
        dest.writeString(this.tema);
        dest.writeString(this.marca);
        dest.writeString(this.talla);
        dest.writeString(this.ean13);
        dest.writeDouble(this.pvp);
        dest.writeString(this.id_articulo);
        dest.writeInt(this.cantidad);
    }

    private void readFromParcel(Parcel in){
        this.categoria = in.readString();
        this.tema = in.readString();
        this.marca = in.readString();
        this.talla = in.readString();
        this.ean13 = in.readString();
        this.pvp = in.readDouble();
        this.id_articulo = in.readString();
        this.cantidad = in.readInt();
    }

    public static final Creator<Producte> CREATOR =new Creator<Producte>() {
        public Producte createFromParcel(Parcel in){
            return new Producte(in);
        }

        public Producte [] newArray(int size){
            return new Producte[size];
        }

    };
}