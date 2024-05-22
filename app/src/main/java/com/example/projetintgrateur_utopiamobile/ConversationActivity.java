package com.example.projetintgrateur_utopiamobile;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
    private HttpClient httpClient;
    private TextView titreConversation;
    private EditText editTextMessage;
    private Button buttonEnvoyerMessage;
    private ImageButton imageButtonCamera;
    private ListView messagesListView;
    private MessageAdapter messageAdapter;
    private Conversation conversation;
    private User interlocuteur;
    private String dateDerniereUpdate;
    private int idMessageUpdating = 0;
    private DateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.CANADA_FRENCH);
    private DateFormat anneeMoisJour = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA_FRENCH);

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
        httpClient = HttpClient.instanceOfClient();
        titreConversation = (TextView) findViewById(R.id.titreConversation);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        buttonEnvoyerMessage = (Button) findViewById(R.id.buttonEnvoyerMessage);
        imageButtonCamera = (ImageButton) findViewById(R.id.imageButtonCamera);

        buttonEnvoyerMessage.setOnClickListener(this);
        imageButtonCamera.setOnClickListener(this);

        if (conversation != null) {
            titreConversation.setText(interlocuteur.getPrenom() + " " + interlocuteur.getNom());
            messagesListView = (ListView) findViewById(R.id.messagesListView);
            setMessageAdapter();
            messagesListView.setSelection(messageAdapter.getCount() - 1);
        } else {
            finish();
        }
    }

    private void setMessageAdapter() {
        messageAdapter = new MessageAdapter(this, conversation.getMessages());
        messagesListView.setAdapter(messageAdapter);
    }

    public void updateMessage(Message message) {
        idMessageUpdating = message.getId();
        editTextMessage.setText(message.getTexte());
        buttonEnvoyerMessage.setText(getResources().getString(R.string.modifier));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonEnvoyerMessage) {
            if (buttonEnvoyerMessage.getText() == getResources().getString(R.string.envoyer)) {
                envoyerMessage();
            }
            else if (buttonEnvoyerMessage.getText() == getResources().getString(R.string.modifier)) {
                modifierMessage();
            }
        }
        else if (v.getId() == R.id.imageButtonCamera) {
            askCameraPermissions();
        }
    }

    private void setDateDerniereUpdate() {
        try {
            this.dateDerniereUpdate = anneeMoisJour.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDateDerniereUpdate(String date) {
        this.dateDerniereUpdate = date;
    }

    private void startBackgroundThreads() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        getNewMessages();

                        Thread.sleep(500);

                        getUpdatedMessages();

                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void getNewMessages() throws Exception {
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
                            messageAdapter.addAll(newMessages);
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
        String requestTime = anneeMoisJour.format(new Date());
        String response = httpClient.get("messages/updated/" + conversation.getId() + "/" + dateDerniereUpdate);

        JSONObject responseJSON = new JSONObject(response);

        if (responseJSON.has("data")) {
            setDateDerniereUpdate(requestTime);

            try {
                JSONArray data = responseJSON.getJSONArray("data");

                if (data.length() > 0) {
                    Date dateLastMessageUpdate = anneeMoisJour.parse(dateDerniereUpdate);

                    for (int i = 0; i < data.length(); i++) {
                        Message updatedMessage = new Message(data.getJSONObject(i));

                        Date dateUpdatedMessage = isoFormat.parse(updatedMessage.getUpdatedAt());

                        if (dateUpdatedMessage.getTime() > dateLastMessageUpdate.getTime()) {
                            dateLastMessageUpdate = dateUpdatedMessage;
                        }

                        for (int j = 0; j < messageAdapter.getCount(); j++) {
                            if (messageAdapter.getItem(j).getId() == updatedMessage.getId()) {
                                if (updatedMessage.getDateHeureSupprime() != "null") {
                                    messageAdapter.remove(messageAdapter.getItem(j));
                                }
                                else {
                                    messageAdapter.getItem(j).setTexte(updatedMessage.getTexte());
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

    private void envoyerMessage() {
        String messageString = editTextMessage.getText().toString();

        if (messageString.length() > 255) {
            Toast.makeText(this, getResources().getString(R.string.erreur255), Toast.LENGTH_LONG).show();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String body = "{ \"texte\" : \"" + messageString + "\", \"id_conversation\" : " + conversation.getId() + " }";
                        String response = "";

                        response = httpClient.post("messages", body);

                        JSONObject responseJSON = new JSONObject(response);

                        if (responseJSON.has("SUCCÈS")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    editTextMessage.setText("");
                                }
                            });
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

    private void modifierMessage() {
        if (idMessageUpdating != 0) {
            String messageString = editTextMessage.getText().toString();

            if (messageString.length() > 255) {
                Toast.makeText(this, getResources().getString(R.string.erreur255), Toast.LENGTH_LONG).show();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String body = "{ \"texte\" : \"" + messageString + "\" }";
                            String response = "";

                            response = httpClient.put("messages/" + idMessageUpdating, body);

                            JSONObject responseJSON = new JSONObject(response);

                            if (responseJSON.has("SUCCÈS")) {
                                idMessageUpdating = 0;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        editTextMessage.setText("");
                                        buttonEnvoyerMessage.setText(getResources().getString(R.string.envoyer));
                                    }
                                });
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

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.CAMERA}, RequestCodes.CAMERA_PERM_CODE);
        }
        else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, RequestCodes.CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RequestCodes.CAMERA_REQUEST_CODE) {
            Bitmap image = (Bitmap) data.getExtras().get("data");

            //Utiliser l'image
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RequestCodes.CAMERA_PERM_CODE) {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
            else {
                Toast.makeText(this, getResources().getString(R.string.permissionCameraRequise), Toast.LENGTH_SHORT).show();
            }
        }
    }
}