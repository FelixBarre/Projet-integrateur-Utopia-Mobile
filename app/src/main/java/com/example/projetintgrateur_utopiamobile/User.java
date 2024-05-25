/****************************************
 Fichier : User.java
 @author : Max Belval-Michaud
 Fonctionnalité : M-CTE-1 -> M-CTE-6 Profil utilisateur
 Date de création: 2024-05-15
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe qui implémente un utilisateur
 */
public class User {
    private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private String noCivique;
    private String noPorte;
    private String rue;
    private int idVille;
    private String codePostal;
    private String email;

    /**
     * Constructeur sans paramètres du User
     */
    public User() {
        this.id = Integer.parseInt(null);
        this.nom = null;
        this.prenom = null;
        this.telephone = null;
        this.noCivique = null;
        this.noPorte = null;
        this.rue = null;
        this.idVille = Integer.parseInt(null);
        this.codePostal = null;
        this.email = null;
    }

    /**
     *
     * @param id l'ID de l'utilisateur
     * @param nom Le nom de l'utilisateur
     * @param prenom Le prenom de l'utilisateur
     * @param telephone Le numéro de téléphone de l'utilisateur
     * @param noCivique Le numéro civique de l'adresse de l'utilisateur
     * @param noPorte Le numéro de porte de l'adresse de l'utilisateur
     * @param rue Le nom de rue de l'adresse de l'utilisateur
     * @param idVille L'ID de la ville de l'utilisateur
     * @param codePostal Le code postal de l'utilisateur
     * @param email L'adresse courriel de l'utilisateur
     *
     * Constructeur avec paramètres entrés manuellement de l'utilisateur
     */
    public User(int id, String nom, String prenom, String telephone, String noCivique, String noPorte, String rue, int idVille, String codePostal, String email) {
        this.id = id;
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

    /**
     *
     * @param userJSON L'utilisateur en objet JSON
     *
     * Constructeur d'utilisateur avec un objet JSON
     */
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

    /**
     *
     * @return l'ID de l'utilisateur
     *
     * Fonction qui retourne l'ID de l'objet utilisateur
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id l'ID de l'utilisateur
     *
     * Fonction qui initialise l'ID de l'objet utilisateur
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return Le nom de l'utilisateur
     *
     * Fonction qui retourne le nom de l'objet utilisateur
     */
    public String getNom() {
        return nom;
    }

    /**
     *
     * @param nom Le nom de l'utilisateur
     *
     * Fonction qui initialise le nom de l'objet utilisateur
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     *
     * @return Le prénom de l'utilisateur
     *
     * Fonction qui retourne le prénom de l'utilisateur
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     *
     * @param prenom Le prénom de l'utilisateur
     *
     * Fonction qui initialise le prénom de l'utilisateur
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     *
     * @return Le numéro de téléphone de l'utilisateur
     *
     * Fonction qui retourne le téléphone de l'utilisateur
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     *
     * @param telephone Le téléphone de l'utilisateur
     *
     * Fonction qui initialise le téléphone de l'utilisateur
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     *
     * @return Le numéro civique de l'utilisateur
     *
     * Fonction qui retourne le numéro civique de l'utilisateur
     */
    public String getNoCivique() {
        return noCivique;
    }

    /**
     *
     * @param noCivique Le numéro civique de l'utilisateur
     *
     * Fonction qui initialise le numéro civique de l'utilisateur
     */
    public void setNoCivique(String noCivique) {
        this.noCivique = noCivique;
    }

    /**
     *
     * @return Le numéro de porte de l'utilisateur
     *
     * Fonction qui retourne le numéro de porte de l'utilisateur
     */
    public String getNoPorte() {
        return noPorte;
    }

    /**
     *
     * @param noPorte Le numéro de porte de l'utilisateur
     *
     * Fonction qui initialise le numéro de porte de l'utilisateur
     */
    public void setNoPorte(String noPorte) {
        this.noPorte = noPorte;
    }

    /**
     *
     * @return Le nom de rue de l'utilisateur
     *
     * Fonction qui retourne le nom de rue de l'utilisateur
     */
    public String getRue() {
        return rue;
    }

    /**
     *
     * @param rue La rue de l'utilisateur
     *
     * Fonction qui initialise le nom de rue de l'utilisateur
     */
    public void setRue(String rue) {
        this.rue = rue;
    }

    /**
     *
     * @return L'ID de ville de l'utilisateur
     *
     * Fonction qui retourne l'ID de la ville de l'utilisateur
     */
    public int getIdVille() {
        return idVille;
    }

    /**
     *
     * @param idVille L'ID de ville de l'utilisateur
     *
     * Fonction qui initialise l'ID de ville de l'utilisateur
     */
    public void setIdVille(int idVille) {
        this.idVille = idVille;
    }

    /**
     *
     * @return Le code postal de l'utilisateur
     *
     * Fonction qui retourne le code postal de l'utilisateur
     */
    public String getCodePostal() {
        return codePostal;
    }

    /**
     *
     * @param codePostal Le code postal de l'utilisateur
     *
     * Fonction qui initialise le code postal de l'utilisateur
     */
    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    /**
     *
     * @return Le courriel de l'utilisateur
     *
     * Fonction qui retourne le courriel de l'utilisateur
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email Le courriel de l'utilisateur
     *
     * Fonction qui initialise le courriel de l'utilisateur
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
