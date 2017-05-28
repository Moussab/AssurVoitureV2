package dz.tdm.esi.myapplication.models;

/**
 * Created by Amine on 20/05/2017.
 */

public enum EtatDossier {
    ouvert,envoye,traitement,accepte,refuse;

    public static EtatDossier parsTo(String s){

        System.out.println(s+""+s+""+s);
        if (s.compareTo("accepte") == 0)
            return accepte;

        if (s.compareTo("envoye") == 0)
            return envoye;

        if (s.compareTo("ouvert") == 0)
            return ouvert;

        if (s.compareTo("refuse") == 0)
            return refuse;

        if (s.compareTo("traitement") == 0)
            return traitement;

        return null;
    }
}
