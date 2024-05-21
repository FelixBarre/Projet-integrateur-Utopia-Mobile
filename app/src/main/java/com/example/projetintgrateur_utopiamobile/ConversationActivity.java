package com.example.projetintgrateur_utopiamobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ConversationActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView titreConversation;
    private EditText editTextMessage;
    private Button buttonEnvoyerMessage;
    private ListView messagesListView;
    private MessageAdapter messageAdapter;
    private Conversation conversation;
    private ArrayList<Message> messages;
    private User interlocuteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_conversation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getConversation();
        initWidgets();
    }

    private void getConversation() {
        Intent intentData = getIntent();
        int id_conversation = intentData.getIntExtra("id_conversation", 0);

        if (id_conversation != 0) {
            conversation = Conversation.getConversationById(id_conversation);

            if (conversation != null) {
                interlocuteur = conversation.getInterlocuteur();
            }
        }
    }

    private void initWidgets() {
        titreConversation = (TextView) findViewById(R.id.titreConversation);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        buttonEnvoyerMessage = (Button) findViewById(R.id.buttonEnvoyerMessage);

        buttonEnvoyerMessage.setOnClickListener(this);

        if (conversation != null) {
            titreConversation.setText(interlocuteur.getPrenom() + " " + interlocuteur.getNom());
            messagesListView = (ListView) findViewById(R.id.messagesListView);
            messages = conversation.getMessages();
            setMessageAdapter();
            messagesListView.setSelection(messageAdapter.getCount() - 1);
        }
        else {
            finish();
        }
    }

    private void setMessageAdapter() {
        messageAdapter = new MessageAdapter(getApplicationContext(), messages);
        messagesListView.setAdapter(messageAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonEnvoyerMessage) {
            String messageString = editTextMessage.getText().toString();

            if (messageString.length() > 255) {
                Toast.makeText(this, getResources().getString(R.string.erreur255), Toast.LENGTH_LONG).show();
            }
            else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpClient httpClient = HttpClient.instanceOfClient();

                            String body = "{ \"texte\" : \"" + messageString + "\", \"id_conversation\" : " + conversation.getId() + " }";
                            String response = "";

                            response = httpClient.post("messages", body);

                            JSONObject responseJSON = new JSONObject(response);

                            if (responseJSON.has("SUCCÈS")) {
                                try {
                                    Message message = new Message(responseJSON.getJSONObject("message"));

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            editTextMessage.setText("");
                                            messages.add(message);
                                            messageAdapter.notifyDataSetChanged();
                                            messagesListView.setSelection(messageAdapter.getCount() - 1);
                                        }
                                    });
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if (responseJSON.has("ERREUR")) {

                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }
}