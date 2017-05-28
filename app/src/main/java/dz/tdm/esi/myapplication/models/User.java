package dz.tdm.esi.myapplication.models;

import java.util.List;

/**
 * Created by Amine on 20/05/2017.
 */

public class User {
    private Long id;
    private String userName;
    private String email;
    private String token;
    private String fcmToken;
    private List<Vehicule> vehicules;
    private List<Dossier> dossiers;

    public User(){

    }

    public User(Long id, String userName, String email, String token) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.token = token;
    }

    public User(String userName, String email, String token, String fcmToken) {
        this.userName = userName;
        this.email = email;
        this.token = token;
        this.fcmToken = fcmToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Vehicule> getVehicules() {
        return vehicules;
    }

    public void setVehicules(List<Vehicule> vehicules) {
        this.vehicules = vehicules;
    }

    public List<Dossier> getDossiers() {
        return dossiers;
    }

    public void setDossiers(List<Dossier> dossiers) {
        this.dossiers = dossiers;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", vehicules=" + vehicules +
                ", dossiers=" + dossiers +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
