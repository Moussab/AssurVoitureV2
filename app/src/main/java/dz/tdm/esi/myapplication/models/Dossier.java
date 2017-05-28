package dz.tdm.esi.myapplication.models;

/**
 * Created by Amine on 20/05/2017.
 */

public class Dossier {

    private String date = "";
    private EtatDossier etat;
    private String imageURL = "";
    private String videoURL = "";
    private String montant = "";
    private String nomAdversaire = "";
    private String numPermisAdversaire = "";
    private String vehiculeAdversaire = "";
    private String matriculeAdversaire = "";
    private int id;

    public Dossier(){}

    public Dossier(int id, String date, String etat,
                   String imageURL, String videoURL, String montant,
                   String nomAdversaire, String numPermisAdversaire,
                   String vehiculeAdversaire, String matriculeAdversaire) {
        this.id = id;
        this.date = date;
        this.etat = EtatDossier.parsTo(etat);
        this.imageURL = imageURL;
        this.videoURL = videoURL;
        this.montant = montant;
        this.nomAdversaire = nomAdversaire;
        this.numPermisAdversaire = numPermisAdversaire;
        this.vehiculeAdversaire = vehiculeAdversaire;
        this.matriculeAdversaire = matriculeAdversaire;
    }

    public Dossier(String date, String etat, String imageURL,
                   String videoURL, String montant,
                   String nomAdversaire, String numPermisAdversaire,
                   String vehiculeAdversaire, String matriculeAdversaire) {

        this.date = date;
        this.etat = EtatDossier.parsTo(etat);
        this.imageURL = imageURL;
        this.videoURL = videoURL;
        this.montant = montant;
        this.nomAdversaire = nomAdversaire;
        this.numPermisAdversaire = numPermisAdversaire;
        this.vehiculeAdversaire = vehiculeAdversaire;
        this.matriculeAdversaire = matriculeAdversaire;
    }

    public Dossier(String date, String etat, String imageURL, String videoURL, String montant) {
        this.date = date;
        this.etat = EtatDossier.parsTo(etat);
        this.imageURL = imageURL;
        this.videoURL = videoURL;
        this.montant = montant;
    }

    public Dossier(String date, String etat) {
        this.date = date;
        this.etat = EtatDossier.valueOf(etat);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public EtatDossier getEtat() {
        return etat;
    }

    public void setEtat(EtatDossier etat) {
        this.etat = etat;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getNomAdversaire() {
        return nomAdversaire;
    }

    public void setNomAdversaire(String nomAdversaire) {
        this.nomAdversaire = nomAdversaire;
    }

    public String getNumPermisAdversaire() {
        return numPermisAdversaire;
    }

    public void setNumPermisAdversaire(String numPermisAdversaire) {
        this.numPermisAdversaire = numPermisAdversaire;
    }
    public String getVehiculeAdversaire() {
        return vehiculeAdversaire;
    }


    public void setVehiculeAdversaire(String vehiculeAdversaire) {
        this.vehiculeAdversaire = vehiculeAdversaire;
    }

    public String getMatriculeAdversaire() {
        return matriculeAdversaire;
    }

    public void setMatriculeAdversaire(String matriculeAdversaire) {
        this.matriculeAdversaire = matriculeAdversaire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Dossier{" +
                "date='" + date + '\'' +
                ", etat=" + etat +
                ", imageURL='" + imageURL + '\'' +
                ", videoURL='" + videoURL + '\'' +
                ", montant='" + montant + '\'' +
                ", nomAdversaire='" + nomAdversaire + '\'' +
                ", numPermisAdversaire='" + numPermisAdversaire + '\'' +
                ", vehiculeAdversaire='" + vehiculeAdversaire + '\'' +
                ", matriculeAdversaire='" + matriculeAdversaire + '\'' +
                ", id=" + id +
                '}';
    }
}


