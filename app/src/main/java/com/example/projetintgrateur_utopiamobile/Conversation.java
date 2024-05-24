/****************************************
 Fichier : Conversation.java
 @author Félix Barré
 Fonctionnalité : Classe pour l'objet conversation
 Date : 13 mai 2024
 Vérification :

 =========================================================
 Historique de modifications :

 =========================================================
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Conversation {
    public static ArrayList<Conversation> conversationArrayList = new ArrayList<>();

    private int id;
    private int ferme;
    private ArrayList<Message> messages;

    /**
     *
     * @param conversation L'objet JSON qui correspond à la conversation
     *
     * Constructeur de l'objet Conversation
     */
    public Conversation(JSONObject conversation) {
        try {
            this.setId(conversation.getInt("id"));
            this.setFerme(conversation.getInt("ferme"));
            this.messages = new ArrayList<>();

            JSONArray messagesJSON = conversation.getJSONArray("messages");
            for (int i = 0; i < messagesJSON.length(); i++) {
                this.messages.add(new Message(messagesJSON.getJSONObject(i)));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param id L'id de la conversation
     *
     * Setter de l'id de la conversation
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return L'id de la conversation
     *
     * Getter de l'id de la conversation
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param ferme La valeur "ferme" de la conversation
     *
     * Setter de la valeur ferme de la conversation
     */
    public void setFerme(int ferme) {
        this.ferme = ferme;
    }

    /**
     *
     * @return La valeur "ferme" de la conversation
     *
     * Getter de la valeur ferme de la conversation
     */
    public int getFerme() {
        return ferme;
    }

    /**
     *
     * @param messages L'arraylist qui contient les message de la conversation
     *
     * Setter de la liste de messages de la conversation
     */
    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    /**
     *
     * @return L'arraylist qui contient les message de la conversation
     *
     * Getter de la liste de messages de la conversation
     */
    public ArrayList<Message> getMessages() {
        return messages;
    }

    /**
     *
     * @return L'objet Message qui correspond au dernier message de la conversation
     *
     * Permet d'obtenir le dernier message de la conversation
     */
    public Message getDernierMessage() {
        Collections.sort(messages, Comparator.comparingLong(Message::getId));

        return messages.get(messages.size() - 1);
    }

    /**
     *
     * @return L'objet User auquel on s'adresse dans cette conversation
     *
     * Permet d'obtenir le User auquel on s'adresse dans la conversation
     */
    public User getInterlocuteur() {
        Message dernierMessage = this.getDernierMessage();

        if (dernierMessage.getEnvoyeur().getId() == UserManager.getAuthUser().getId()) {
            return dernierMessage.getReceveur();
        }
        else {
            return dernierMessage.getEnvoyeur();
        }
    }

    /**
     *
     * @param id_conversation L'id de la conversation
     * @return L'objet conversation qui correspond à la conversation voulue
     *
     * Permet d'obtenir une conversation de l'arraylist par son id
     */
    public static Conversation getConversationById(int id_conversation) {
        for (int i = 0; i < conversationArrayList.size(); i++) {
            if (conversationArrayList.get(i).getId() == id_conversation) {
                return conversationArrayList.get(i);
            }
        }

        return null;
    }
}
