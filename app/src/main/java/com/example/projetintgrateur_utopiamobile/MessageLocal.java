/****************************************
 Fichier : MessageLocal.java
 @author Félix Barré
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

    /**
     *
     * @param id L'id du message
     * @param id_conversation L'id de la conversation
     * @param image L'image en pièce-jointe
     * @param texte Le texte du message
     *
     * Constructeur de l'objet MessageLocal
     */
    public MessageLocal(int id, int id_conversation, Bitmap image, String texte) {
        this.setId(id);
        this.setIdConversation(id_conversation);
        this.setImage(image);
        this.setTexte(texte);
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

    /**
     *
     * @param image L'image en pièce-jointe
     *
     * Setter de l'image en pièce-jointe
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     *
     * @return L'image en pièce-jointe
     *
     * Getter de l'image en pièce-jointe
     */
    public Bitmap getImage() {
        return image;
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
}
