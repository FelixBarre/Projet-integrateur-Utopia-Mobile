/****************************************
 Fichier : Conversations.java
 Auteur : Félix Barré
 Fonctionnalité : Page qui affiche les conversations d'un uilisateur
 Date : 13 mai 2024
 Vérification :

 =========================================================
 Historique de modifications :

 =========================================================
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class Conversations extends AppCompatActivity {
    private ListView conversationsListView;
    private ConversationAdapter conversationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_conversations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initWidgets();
        setConversationAdapter();
        loadConversations();
    }

    private void initWidgets() {
        conversationsListView = (ListView) findViewById(R.id.conversationsListView);
    }
    private void setConversationAdapter() {
        conversationAdapter = new ConversationAdapter(getApplicationContext(), Conversation.conversationArrayList);
        conversationsListView.setAdapter(conversationAdapter);
    }

    private void loadConversations() {
        Intent loadingHttp = new Intent(Conversations.this, LoadingHttp.class);
        loadingHttp.putExtra("method", "GET");
        loadingHttp.putExtra("route", "conversations");
        startActivityForResult(loadingHttp, RequestCodes.CONVERSATIONS_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCodes.CONVERSATIONS_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    String responseGET = data.getStringExtra("response");

                    try {
                        JSONObject reponse = new JSONObject(responseGET);

                        if (reponse.has("data")) {
                            JSONArray conversationsJSON = reponse.getJSONArray("data");
                            Conversation.conversationArrayList.clear();
                            conversationAdapter.clear();
                            conversationAdapter.setNotifyOnChange(false);
                            for (int i = 0; i < conversationsJSON.length(); i++) {
                                Conversation.conversationArrayList.add(new Conversation(conversationsJSON.getJSONObject(i)));
                            }
                            conversationAdapter.setNotifyOnChange(true);
                            conversationAdapter.notifyDataSetChanged();
                            setConversationAdapter();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }
}