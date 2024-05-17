package com.example.projetintgrateur_utopiamobile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Conversation {
    public static ArrayList<Conversation> conversationArrayList = new ArrayList<>();

    private int id;
    private int ferme;

    public Conversation(JSONObject conversation) {
        try {
            this.setId(conversation.getInt("id"));
            this.setFerme(conversation.getInt("ferme"));
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
}
