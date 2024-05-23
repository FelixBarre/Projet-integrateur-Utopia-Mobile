/****************************************
 Fichier : MessageLocal.java
 Auteur : Félix Barré
 Fonctionnalité : Classe pour un message qui n'a pas encore été envoyé à la BD
 Date : 13 mai 2024
 Vérification :

 =========================================================
 Historique de modifications :

 =========================================================
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class MessageLocal {
    public static ArrayList<MessageLocal> messagesLocauxArrayList = new ArrayList<>();
    private int id;
    private int id_conversation;
    private Bitmap image;
    private String texte;

    public MessageLocal(int id, int id_conversation, Bitmap image, String texte) {
        this.setId(id);
        this.setIdConversation(id_conversation);
        this.setImage(image);
        this.setTexte(texte);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setIdConversation(int id_conversation) {
        this.id_conversation = id_conversation;
    }

    public int getIdConversation() {
        return id_conversation;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public String getTexte() {
        return texte;
    }
}
