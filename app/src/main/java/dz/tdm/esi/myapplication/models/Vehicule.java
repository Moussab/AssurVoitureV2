package dz.tdm.esi.myapplication.models;

/**
 * Created by Amine on 20/05/2017.
 */

public class Vehicule {

    private int id;
    private String nom;
    private String categorie;
    private String numero;
    private String pays;
    private String matricule;

    public Vehicule(){}

    public Vehicule(String nom, String categorie, String numero, String pays, String matricule) {
        this.nom = nom;
        this.categorie = categorie;
        this.numero = numero;
        this.pays = pays;
        this.matricule = matricule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
}
