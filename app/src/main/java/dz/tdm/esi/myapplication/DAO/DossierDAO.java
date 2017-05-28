package dz.tdm.esi.myapplication.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import dz.tdm.esi.myapplication.models.Dossier;
import dz.tdm.esi.myapplication.models.EtatDossier;
import dz.tdm.esi.myapplication.models.User;

/**
 * Created by Amine on 20/05/2017.
 */

public class DossierDAO extends DAOBase {



    public static final String TABLE_NAME = "dossier";
    public static final String KEY = "id_dossier";
    public static final String DATE = "date";
    public static final String ETAT = "etat";
    public static final String IMG_URL = "imageURL";
    public static final String VID_URL = "videoURL";
    public static final String MONTANT = "montant";
    public static final String Nom_ADVER = "nomAdversaire";
    public static final String NUM_PERMIS_ADVER = "numPermisAdversaire";
    public static final String VEHICULE_ADVER = "vehiculeAdversaire";
    public static final String MATRICULE_ADVER = "matriculeAdversaire";


    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + " ("
            + KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DATE + " TEXT, "
            + ETAT + " TEXT, "
            + IMG_URL + " TEXT, "
            + VID_URL + " TEXT, "
            + MONTANT + " TEXT, "
            + Nom_ADVER + " TEXT, "
            + NUM_PERMIS_ADVER + " TEXT, "
            + VEHICULE_ADVER + " TEXT, "
            + MATRICULE_ADVER + " TEXT);";

    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public DossierDAO(Context pContext) {
        super(pContext);
        open();
        getDb().execSQL(TABLE_CREATE);
    }

    public void drop(){
        getDb().execSQL(TABLE_DROP);
    }

    public void ajouter(Dossier dossier) {
        // CODE
        ContentValues value = new ContentValues();
        value.put(DATE, dossier.getDate());
        value.put(ETAT, String.valueOf(dossier.getEtat()));
        value.put(IMG_URL, dossier.getImageURL());
        value.put(VID_URL, dossier.getVideoURL());
        value.put(MONTANT, dossier.getMontant());
        value.put(Nom_ADVER, dossier.getNomAdversaire());
        value.put(NUM_PERMIS_ADVER, dossier.getVehiculeAdversaire());
        value.put(VEHICULE_ADVER, dossier.getVehiculeAdversaire());
        value.put(MATRICULE_ADVER, dossier.getMatriculeAdversaire());
        mDb.insert(TABLE_NAME, null, value);
    }

    public void supprimer(long id) {
        // CODE
        mDb.delete(TABLE_NAME, KEY + " = ?", new String[] {String.valueOf(id)});
    }

    public void modifier(Dossier dossier) {
        // CODE
        ContentValues value = new ContentValues();
        value.put(DATE, dossier.getDate());
        value.put(ETAT, String.valueOf(dossier.getEtat()));
        value.put(IMG_URL, dossier.getImageURL());
        value.put(VID_URL, dossier.getVideoURL());
        value.put(MONTANT, dossier.getMontant());
        value.put(Nom_ADVER, dossier.getNomAdversaire());
        value.put(NUM_PERMIS_ADVER, dossier.getVehiculeAdversaire());
        value.put(VEHICULE_ADVER, dossier.getVehiculeAdversaire());
        value.put(MATRICULE_ADVER, dossier.getMatriculeAdversaire());
        mDb.update(TABLE_NAME, value, KEY  + " = ?", new String[] {String.valueOf(dossier.getId())});

    }

    public Dossier selectionner(Long id) {
        // CODE
        Cursor cursor = mDb.rawQuery("select * from " + TABLE_NAME + " where " + KEY +" = ?", new String[]{String.valueOf(id)});

        cursor.moveToFirst();

        Dossier dossier = new Dossier(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9)
        );

        return dossier;
    }

    public List<Dossier> selectionnerOuvert() {

        List<Dossier> dossiers  = new ArrayList<Dossier>();
        List<Dossier> dossiersResult  = new ArrayList<Dossier>();
        // CODE
        // Name of the columns we want to select

        dossiers = getDossiers();
        for (Dossier dossier:dossiers) {
            if (dossier.getEtat().toString().compareTo("ouvert") == 0)
                dossiersResult.add(dossier);
        }


        return dossiersResult;
    }

    public List<Dossier> selectionnerTraitement() {

        List<Dossier> dossiers  = new ArrayList<Dossier>();
        List<Dossier> dossiersResult  = new ArrayList<Dossier>();
        // CODE
        // Name of the columns we want to select

        dossiers = getDossiers();
        for (Dossier dossier:dossiers) {
            if (dossier.getEtat().toString().compareTo("traitement") == 0)
                dossiersResult.add(dossier);
        }


        return dossiersResult;
    }

    public List<Dossier> getDossiers() {
        List<Dossier> dossiers  = new ArrayList<Dossier>();

        // Name of the columns we want to select
        String[] tableColumns = new String[] {KEY,DATE,ETAT,IMG_URL,VID_URL,MONTANT,Nom_ADVER,NUM_PERMIS_ADVER,VEHICULE_ADVER,MATRICULE_ADVER};

        // Query the database
        Cursor cursor = mDb.query(TABLE_NAME, tableColumns, null, null, null, null, null);
        cursor.moveToFirst();

        // Iterate the results
        while (!cursor.isAfterLast()) {

            Dossier dossier = new Dossier(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9)
            );


            // Add to the DB
            dossiers.add(dossier);

            // Move to the next result
            cursor.moveToNext();
        }

        return dossiers;
    }
}
