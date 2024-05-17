package com.example.projetintgrateur_utopiamobile;

import org.json.JSONObject;

import java.util.ArrayList;

public class Message {
    public static ArrayList<Message> messageArrayList = new ArrayList<>();
    private int id;
    private String created_at;
    private String updated_at;
    private String texte;
    private String chemin_du_fichier;
    private String date_heure_supprime;
    private int id_envoyeur;
    private int id_receveur;
    private int id_conversation;
    public Message(JSONObject message) {

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

    public void setIdEnvoyeur(int id_envoyeur) {
        this.id_envoyeur = id_envoyeur;
    }

    public int getIdEnvoyeur() {
        return id_envoyeur;
    }

    public void setIdReceveur(int id_receveur) {
        this.id_receveur = id_receveur;
    }

    public int getIdReceveur() {
        return id_receveur;
    }

    public void setIdConversation(int id_conversation) {
        this.id_conversation = id_conversation;
    }

    public int getIdConversation() {
        return id_conversation;
    }
}
