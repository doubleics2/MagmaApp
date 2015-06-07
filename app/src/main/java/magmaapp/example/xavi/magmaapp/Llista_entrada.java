package magmaapp.example.xavi.magmaapp;

public class Llista_entrada {
    private int idImage;
    private String text_nom_prod;
    private String text_ref_prod;

    public Llista_entrada (int idImage, String text_nom_prod, String text_ref_prod) {
        this.idImage = idImage;
        this.text_nom_prod = text_nom_prod;
        this.text_ref_prod = text_ref_prod;
    }

    public String get_text_nom_prod() {
        return text_nom_prod;
    }

    public String get_text_ref_prod() {
        return text_ref_prod;
    }

    public int get_idImage() {
        return idImage;
    }
}
