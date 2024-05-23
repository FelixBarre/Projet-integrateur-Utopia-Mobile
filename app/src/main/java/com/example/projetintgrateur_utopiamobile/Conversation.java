/****************************************
 Fichier : Conversation.java
 Auteur : Félix Barré
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setFerme(int ferme) {
        this.ferme = ferme;
    }

    public int getFerme() {
        return ferme;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public Message getDernierMessage() {
        Collections.sort(messages, Comparator.comparingLong(Message::getId));

        return messages.get(messages.size() - 1);
    }

    public User getInterlocuteur() {
        Message dernierMessage = this.getDernierMessage();

        if (dernierMessage.getEnvoyeur().getId() == UserManager.getAuthUser().getId()) {
            return dernierMessage.getReceveur();
        }
        else {
            return dernierMessage.getEnvoyeur();
        }
    }

    public static Conversation getConversationById(int id_conversation) {
        for (int i = 0; i < conversationArrayList.size(); i++) {
            if (conversationArrayList.get(i).getId() == id_conversation) {
                return conversationArrayList.get(i);
            }
        }

        return null;
    }
}
