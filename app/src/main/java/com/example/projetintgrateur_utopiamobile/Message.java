/****************************************
 Fichier : Message.java
 Auteur : Félix Barré
 Fonctionnalité : Classe pour l'objet message
 Date : 13 mai 2024
 Vérification :

 =========================================================
 Historique de modifications :

 =========================================================
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Message {
    private int id;
    private String created_at;
    private String updated_at;
    private String texte;
    private String chemin_du_fichier;
    private String date_heure_supprime;
    private User envoyeur;
    private User receveur;
    private int id_conversation;
    public Message(JSONObject messageJSON) {
        try {
            this.setId(messageJSON.getInt("id"));
            this.setCreatedAt(messageJSON.getString("created_at"));
            this.setUpdatedAt(messageJSON.getString("updated_at"));
            this.setTexte(messageJSON.getString("texte"));
            this.setCheminDuFichier(messageJSON.getString("chemin_du_fichier"));
            this.setDateHeureSupprime(messageJSON.getString("date_heure_supprime"));
            this.setEnvoyeur(new User(messageJSON.getJSONObject("envoyeur")));
            this.setReceveur(new User(messageJSON.getJSONObject("receveur")));
            this.setIdConversation(messageJSON.getInt("id_conversation"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getCreatedAtFormatted() {
        try {
            DateFormat createdAtFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.CANADA_FRENCH);
            Date dateCreatedAt = createdAtFormat.parse(this.getCreatedAt());

            CharSequence ilYA = DateUtils.getRelativeTimeSpanString(dateCreatedAt.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);

            return ilYA.toString();
        }
        catch (Exception e) {
            return this.getCreatedAt();
        }
    }

    public void setUpdatedAt(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public String getTexte() {
        return texte;
    }

    public void setCheminDuFichier(String chemin_du_fichier) {
        this.chemin_du_fichier = chemin_du_fichier;
    }

    public String getCheminDuFichier() {
        return chemin_du_fichier;
    }

    public void setDateHeureSupprime(String date_heure_supprime) {
        this.date_heure_supprime = date_heure_supprime;
    }

    public String getDateHeureSupprime() {
        return date_heure_supprime;
    }

    public void setEnvoyeur(User envoyeur) {
        this.envoyeur = envoyeur;
    }

    public User getEnvoyeur() {
        return envoyeur;
    }

    public void setReceveur(User receveur) {
        this.receveur = receveur;
    }

    public User getReceveur() {
        return receveur;
    }

    public void setIdConversation(int id_conversation) {
        this.id_conversation = id_conversation;
    }

    public int getIdConversation() {
        return id_conversation;
    }
}
