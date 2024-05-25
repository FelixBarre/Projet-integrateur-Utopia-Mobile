/****************************************
 Fichier : ConversationActivity.java
 @author Félix Barré
 Fonctionnalité : Page qui affiche les messages d'une conversation
 Date : 13 mai 2024
 Vérification :

 =========================================================
 Historique de modifications :

 =========================================================
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
    private HttpClient httpClientNew;
    private HttpClient httpClientUpdated;
    private HttpClient httpClientEnvoi;
    private TextView titreConversation;
    private EditText editTextMessage;
    private Button buttonEnvoyerMessage;
    private ImageButton imageButtonCamera;
    private Bitmap imagePieceJointe;
    private ListView messagesListView;
    private MessageAdapter messageAdapter;
    private Conversation conversation;
    private User interlocuteur;
    private String dateDerniereUpdate;
    private int idMessageUpdating = 0;
    private DateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.CANADA_FRENCH);
    private DateFormat anneeMoisJour = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA_FRENCH);
    private SQLiteManager sqLiteManager;
    private ConnectionManager connectionManager;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     * Fonction principale lancée au départ de l'activité
     */
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

        askNotificationPermissions();

        setDateDerniereUpdate();
        getConversation();
        initWidgets();
        sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        connectionManager = new ConnectionManager(this);
        startBackgroundThreads();
    }

    /**
     * Va chercher l'objet conversation qui correspond à l'Id passé à l'activité
     */
    private void getConversation() {
        Intent intentData = getIntent();
        int id_conversation = intentData.getIntExtra("id_conversation", 0);

        if (id_conversation != 0) {
            conversation = Conversation.getConversationById(id_conversation);

            if (conversation != null) {
                interlocuteur = conversation.getInterlocuteur();
            }
            else {
                finish();
            }
        }
    }

    /**
     * Initialise les widgets de l'activité
     */
    private void initWidgets() {
        httpClientNew = new HttpClient();
        httpClientUpdated = new HttpClient();
        httpClientEnvoi = new HttpClient();
        titreConversation = (TextView) findViewById(R.id.titreConversation);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        buttonEnvoyerMessage = (Button) findViewById(R.id.buttonEnvoyerMessage);
        imageButtonCamera = (ImageButton) findViewById(R.id.imageButtonCamera);

        buttonEnvoyerMessage.setOnClickListener(this);
        imageButtonCamera.setOnClickListener(this);

        if (conversation != null) {
            if (interlocuteur != null) {
                titreConversation.setText(interlocuteur.getPrenom() + " " + interlocuteur.getNom());
            }
            else {
                titreConversation.setText(getString(R.string.conversationVide));
            }

            messagesListView = (ListView) findViewById(R.id.messagesListView);
            setMessageAdapter();

            if (messageAdapter.getCount() > 0) {
                messagesListView.setSelection(messageAdapter.getCount() - 1);
            }
        } else {
            finish();
        }
    }

    /**
     * Initialise l'adapter qui liste les message de la conversation
     */
    private void setMessageAdapter() {
        messageAdapter = new MessageAdapter(this, conversation.getMessages());
        messagesListView.setAdapter(messageAdapter);
    }

    /**
     *
     * @param message L'objet message qu'on désire modifier
     *
     * Met en place les widgets pour démarrer la modification d'un message
     */
    public void updateMessage(Message message) {
        idMessageUpdating = message.getId();
        editTextMessage.setText(message.getTexte());
        buttonEnvoyerMessage.setText(getResources().getString(R.string.modifier));
    }

    /**
     *
     * @param v La vue qui a été cliquée.
     *
     * Override du onClick pour gérer le click des boutons
     */
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

    /**
     * Set la valeur de la date de la dernière update à l'heure actuelle (utilisée pour aller chercher les modifications des messages par l'api)
     */
    private void setDateDerniereUpdate() {
        try {
            this.dateDerniereUpdate = anneeMoisJour.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param date La date qu'on veut setter comme date de dernière update
     *
     * Setter pour la date de dernière update (utilisée pour aller chercher les modifications des messages par l'api)
     */
    private void setDateDerniereUpdate(String date) {
        this.dateDerniereUpdate = date;
    }

    /**
     * Démare un thread pour les tâches d'arrière-plan
     */
    private void startBackgroundThreads() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (connectionManager.isConnected()) {
                            postLocalMessages();

                            Thread.sleep(250);

                            getNewMessages();

                            Thread.sleep(250);

                            getUpdatedMessages();

                            Thread.sleep(250);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * Tente d'envoyer les messages stockés sur la base de données locale à l'api
     */
    private void postLocalMessages() {
        sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateMessagesLocauxArrayList();

        for (int i = 0; i < MessageLocal.messagesLocauxArrayList.size(); i++) {
            MessageLocal messageLocal = MessageLocal.messagesLocauxArrayList.get(i);

            sqLiteManager.deleteMessageLocal(messageLocal.getId());

            String response;

            try {
                response = postMessage(messageLocal.getTexte(), messageLocal.getIdConversation(), messageLocal.getImage());
            }
            catch (Exception e) {
                sqLiteManager.addMessageLocalDB(messageLocal);
                continue;
            }

            JSONObject responseJSON;

            try {
                responseJSON = new JSONObject(response);

                if (responseJSON.has("SUCCÈS")) {
                    imagePieceJointe = null;
                    imageButtonCamera.setBackgroundColor(getResources().getColor(R.color.utopia_turquoise_moyen));
                } else if (responseJSON.has("ERREUR")) {
                    Toast.makeText(context, responseJSON.getString("ERREUR"), Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param texte Le texte du message
     * @param id_conversation L'id de la conversation du message
     * @param image L'image (optionnelle) qui a été prise avec la caméra
     * @return La réponse de la requête Http
     * @throws Exception Exception levée lors de la requête Http
     *
     * Tente d'envoyer un message à l'api
     */
    private String postMessage(String texte, int id_conversation, Bitmap image) throws Exception {
        String body = "{ \"texte\" : \"" + texte + "\", \"id_conversation\" : " + id_conversation + " }";
        String response = "";

        if (image == null) {
            response = httpClientEnvoi.post("messages", body);
        }
        else {
            response = httpClientEnvoi.postMessageWithImage(body, image);
        }

        return response;
    }

    /**
     *
     * @throws Exception Exception levée lors de la requête Http
     *
     * Tente d'obtenir les nouveaux messages créés depuis le dernier message de la conversation affiché
     */
    private void getNewMessages() throws Exception {
        String response = "";

        int idDernierMessage = 0;

        if (!messageAdapter.isEmpty()) {
            idDernierMessage = messageAdapter.getItem(messageAdapter.getCount() - 1).getId();
        }

        response = httpClientNew.get("messages/" + conversation.getId() + "/" + idDernierMessage);

        JSONObject responseJSON = new JSONObject(response);

        if (responseJSON.has("data")) {
            try {
                JSONArray data = responseJSON.getJSONArray("data");

                if (data.length() > 0) {
                    ArrayList<Message> newMessages = new ArrayList<>();

                    for (int i = 0; i < data.length(); i++) {
                        Message newMessage = new Message(data.getJSONObject(i));
                        newMessages.add(newMessage);

                        if (newMessage.getEnvoyeur().getId() != UserManager.getAuthUser().getId()) {
                            UtopiaNotificationManager.postNotification(getApplicationContext(), newMessage.getEnvoyeur().getPrenom(), newMessage.getTexte(), conversation.getId());
                        }
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

    /**
     *
     * @throws Exception Exception levée lors de la requête Http
     *
     * Tente d'obtenir les messages modifiés depuis la dernière modification
     */
    private void getUpdatedMessages() throws Exception {
        String requestTime = anneeMoisJour.format(new Date());
        String response = httpClientUpdated.get("messages/updated/" + conversation.getId() + "/" + dateDerniereUpdate);

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

    /**
     * Prépare et lance l'envoi du message qui a été entré
     */
    private void envoyerMessage() {
        String messageString = editTextMessage.getText().toString().trim();

        if (messageString.isEmpty()) {
            Toast.makeText(context, getResources().getString(R.string.erreurVide), Toast.LENGTH_LONG).show();
            return;
        }

        if (messageString.length() > 255) {
            Toast.makeText(this, getResources().getString(R.string.erreur255), Toast.LENGTH_LONG).show();
        } else {
            editTextMessage.setText("");

            sqLiteManager.addMessageLocalDB(new MessageLocal(0, conversation.getId(), imagePieceJointe, messageString));
        }
    }

    /**
     * Prépare et lance la modification du message qui a été entrée
     */
    private void modifierMessage() {
        if (idMessageUpdating != 0) {
            String messageString = editTextMessage.getText().toString();
            editTextMessage.setText("");
            buttonEnvoyerMessage.setText(getResources().getString(R.string.envoyer));

            if (messageString.length() > 255) {
                Toast.makeText(this, getResources().getString(R.string.erreur255), Toast.LENGTH_LONG).show();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String body = "{ \"texte\" : \"" + messageString + "\" }";
                            String response = "";

                            response = httpClientEnvoi.put("messages/" + idMessageUpdating, body);

                            JSONObject responseJSON = new JSONObject(response);

                            if (responseJSON.has("SUCCÈS")) {
                                idMessageUpdating = 0;
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

    /**
     * Vérifie les permissions de la caméra. Si on ne les a pas, on les demande. Sinon, on ouvre la caméra
     */
    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.CAMERA}, RequestCodes.CAMERA_PERM_CODE);
        }
        else {
            openCamera();
        }
    }

    /**
     * Demande les permissions de notifications si on ne les a pas
     */
    private void askNotificationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.POST_NOTIFICATIONS }, RequestCodes.NOTIFICATION_PERM_CODE);
        }
    }

    /**
     * Ouvre l'activité caméra
     */
    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, RequestCodes.CAMERA_REQUEST_CODE);
    }

    /**
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     * Gére la réception des résultats des startActivityForResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCodes.CAMERA_REQUEST_CODE) {
            imagePieceJointe = (Bitmap) data.getExtras().get("data");
            imageButtonCamera.setBackgroundColor(getResources().getColor(R.color.utopia_turquoise_fonce));
        }
    }

    /**
     *
     * @param requestCode The request code passed in requestPermissions
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     * Gère la réception des approbations de permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestCodes.CAMERA_PERM_CODE) {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, getResources().getString(R.string.permissionCameraRequise), Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == RequestCodes.NOTIFICATION_PERM_CODE) {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission accordée
            }
            else {
                Toast.makeText(this, getResources().getString(R.string.permissionNotifRequise), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}