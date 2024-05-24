/****************************************
 Fichier : Message.java
 @author Félix Barré
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

    /**
     *
     * @param messageJSON L'objet JSON qui correspond au message
     *
     * Constructeur de l'objet Message
     */
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

    /**
     *
     * @param id L'id du message
     *
     * Setter de l'id du message
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return L'id du message
     *
     * Getter de l'id du message
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param created_at Le moment de création du message
     *
     * Setter du moment de création du message
     */
    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    /**
     *
     * @return Le moment de création du message
     *
     * Getter du moment de création du message
     */
    public String getCreatedAt() {
        return created_at;
    }

    /**
     *
     * @return Le moment de création du message formaté en temps relatif
     *
     * Getter du moment de création du message formaté en temps relatif
     */
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

    /**
     *
     * @param updated_at Le moment de modification du message
     *
     * Setter du moment de modification du message
     */
    public void setUpdatedAt(String updated_at) {
        this.updated_at = updated_at;
    }

    /**
     *
     * @return Le moment de modification du message
     *
     * Getter du moment de modification du message
     */
    public String getUpdatedAt() {
        return updated_at;
    }

    /**
     *
     * @param texte Le texte du message
     *
     * Setter du texte du message
     */
    public void setTexte(String texte) {
        this.texte = texte;
    }

    /**
     *
     * @return Le texte du message
     *
     * Getter du texte du message
     */
    public String getTexte() {
        return texte;
    }

    /**
     *
     * @param chemin_du_fichier Chemin relatif de la pièce-jointe
     *
     * Setter du chemin relatif de la pièce-jointe
     */
    public void setCheminDuFichier(String chemin_du_fichier) {
        this.chemin_du_fichier = chemin_du_fichier;
    }

    /**
     *
     * @return Chemin relatif de la pièce-jointe
     *
     * Getter du chemin relatif de la pièce-jointe
     */
    public String getCheminDuFichier() {
        return chemin_du_fichier;
    }

    /**
     *
     * @param date_heure_supprime Le moment de suppression du message
     *
     * Setter du moment de suppression du message
     */
    public void setDateHeureSupprime(String date_heure_supprime) {
        this.date_heure_supprime = date_heure_supprime;
    }

    /**
     *
     * @return Le moment de suppression du message
     *
     * Getter du moment de suppression du message
     */
    public String getDateHeureSupprime() {
        return date_heure_supprime;
    }

    /**
     *
     * @param envoyeur Le User envoyeur du message
     *
     * Setter du User envoyeur du message
     */
    public void setEnvoyeur(User envoyeur) {
        this.envoyeur = envoyeur;
    }

    /**
     *
     * @return Le User envoyeur du message
     *
     * Getter du User envoyeur du message
     */
    public User getEnvoyeur() {
        return envoyeur;
    }

    /**
     *
     * @param receveur Le User receveur du message
     *
     * Setter du User receveur du message
     */
    public void setReceveur(User receveur) {
        this.receveur = receveur;
    }

    /**
     *
     * @return Le User receveur du message
     *
     * Getter du User receveur du message
     */
    public User getReceveur() {
        return receveur;
    }

    /**
     *
     * @param id_conversation L'id de la conversation
     *
     * Setter de l'id de la conversation
     */
    public void setIdConversation(int id_conversation) {
        this.id_conversation = id_conversation;
    }

    /**
     *
     * @return L'id de la conversation
     *
     * Getter de l'id de la conversation
     */
    public int getIdConversation() {
        return id_conversation;
    }
}
