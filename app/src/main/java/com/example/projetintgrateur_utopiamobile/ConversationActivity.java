package com.example.projetintgrateur_utopiamobile;

import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ConversationActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private TextView titreConversation;
    private EditText editTextMessage;
    private Button buttonEnvoyerMessage;
    private ListView messagesListView;
    private MessageAdapter messageAdapter;
    private Conversation conversation;
    private ArrayList<Message> messages;
    private User interlocuteur;
    private String dateDerniereUpdate;

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

        context = this;

        setDateDerniereUpdate();
        getConversation();
        initWidgets();
        startBackgroundThreads();
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
        } else {
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
            } else {
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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else if (responseJSON.has("ERREUR")) {
                                Toast.makeText(context, responseJSON.getString("ERREUR"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    private void setDateDerniereUpdate() {
        try {
            DateFormat dateDerniereUpdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA_FRENCH);
            this.dateDerniereUpdate = dateDerniereUpdateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startBackgroundThreads() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        getNewMessages();

                        Thread.sleep(1000);

                        getUpdatedMessages();

                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void getNewMessages() throws Exception {
        HttpClient httpClient = HttpClient.instanceOfClient();

        String response = "";

        response = httpClient.get("messages/" + conversation.getId() + "/" + messageAdapter.getItem(messageAdapter.getCount() - 1).getId());

        JSONObject responseJSON = new JSONObject(response);

        if (responseJSON.has("data")) {
            try {
                JSONArray data = responseJSON.getJSONArray("data");

                if (data.length() > 0) {
                    ArrayList<Message> newMessages = new ArrayList<>();

                    for (int i = 0; i < data.length(); i++) {
                        newMessages.add(new Message(data.getJSONObject(i)));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messages.addAll(newMessages);
                            messageAdapter.notifyDataSetChanged();
                            messagesListView.setSelection(messageAdapter.getCount() - 1);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (responseJSON.has("ERREUR")) {
            Toast.makeText(context, responseJSON.getString("ERREUR"), Toast.LENGTH_LONG).show();
        }
    }

    private void getUpdatedMessages() throws Exception {
        HttpClient httpClient = HttpClient.instanceOfClient();

        String response = httpClient.get("messages/updated/" + conversation.getId() + "/" + dateDerniereUpdate);

        JSONObject responseJSON = new JSONObject(response);

        if (responseJSON.has("data")) {
            try {
                JSONArray data = responseJSON.getJSONArray("data");

                setDateDerniereUpdate();

                if (data.length() > 0) {
                    for (int i = 0; i < data.length(); i++) {
                        Message updatedMessage = new Message(data.getJSONObject(i));

                        for (int j = 0; j < messages.size(); j++) {
                            if (messages.get(j).getId() == updatedMessage.getId()) {
                                if (updatedMessage.getDateHeureSupprime() != "null") {
                                    messages.remove(j);
                                }
                                else {
                                    messages.get(j).setTexte(updatedMessage.getTexte());
                                }

                                break;
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messageAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (responseJSON.has("ERREUR")) {
            Toast.makeText(context, responseJSON.getString("ERREUR"), Toast.LENGTH_LONG).show();
        }
    }
}