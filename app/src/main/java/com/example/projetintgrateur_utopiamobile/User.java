package com.example.projetintgrateur_utopiamobile;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public int id;
    public String nom;
    public String prenom;
    public String telephone;
    public String noCivique;
    public String noPorte;
    public String rue;
    public int idVille;
    public String codePostal;
    public String email;

    public User(String nom, String prenom, String telephone, String noCivique, String noPorte, String rue, int idVille, String codePostal, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.noCivique = noCivique;
        this.noPorte = noPorte;
        this.rue = rue;
        this.idVille = idVille;
        this.codePostal = codePostal;
        this.email = email;
    }

    public User (JSONObject userJSON) {
        try {
            this.setId(userJSON.getInt("id"));
            this.setNom(userJSON.getString("nom"));
            this.setPrenom(userJSON.getString("prenom"));
            this.setTelephone(userJSON.getString("telephone"));
            this.setNoCivique(userJSON.getString("no_civique"));
            this.setNoPorte(userJSON.getString("no_porte"));
            this.setRue(userJSON.getString("rue"));
            this.setIdVille(userJSON.getInt("id_ville"));
            this.setCodePostal(userJSON.getString("code_postal"));
            this.setEmail(userJSON.getString("email"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getNoCivique() {
        return noCivique;
    }

    public void setNoCivique(String noCivique) {
        this.noCivique = noCivique;
    }

    public String getNoPorte() {
        return noPorte;
    }

    public void setNoPorte(String noPorte) {
        this.noPorte = noPorte;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public int getIdVille() {
        return idVille;
    }

    public void setIdVille(int idVille) {
        this.idVille = idVille;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
