/****************************************
 Fichier : MessageAdapter.java
 @author Félix Barré
 Fonctionnalité : Adapter pour le ListView des messages d'une conversation
 Date : 13 mai 2024
 Vérification :

 =========================================================
 Historique de modifications :

 =========================================================
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private Context context;

    /**
     *
     * @param context Contexte de l'adapter
     * @param messages Liste des messages à afficher
     *
     * Constructeur du MessageAdapter
     */
    public MessageAdapter(Context context, List<Message> messages) {
        super(context, 0, messages);
        this.context = context;
    }

    /**
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return La vue qui corresponde à la row
     *
     * Gère l'initialisation de chaque row de l'adapter de messages
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message message = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_message, parent, false);
        }

        LinearLayout layoutContenuMessage = (LinearLayout) convertView.findViewById(R.id.layoutContenuMessage);
        LinearLayout layoutMessage = (LinearLayout) convertView.findViewById(R.id.layoutMessage);
        LinearLayout layoutButtonsMessage = (LinearLayout) convertView.findViewById(R.id.layoutButtonsMessage);
        TextView textHeureMessage = (TextView) convertView.findViewById(R.id.textHeureMessage);
        TextView textMessage = (TextView) convertView.findViewById(R.id.textMessage);
        ImageButton imageButtonEditMessage = (ImageButton) convertView.findViewById(R.id.imageButtonEditMessage);
        ImageButton imageButtonDeleteMessage = (ImageButton) convertView.findViewById(R.id.imageButtonDeleteMessage);
        ImageView imageViewPieceJointe = (ImageView) convertView.findViewById(R.id.imageViewPieceJointe);
        TextView textViewPieceJointe = (TextView) convertView.findViewById(R.id.textViewPieceJointe);

        if (message.getEnvoyeur().getId() == UserManager.getAuthUser().getId()) {
            layoutMessage.setGravity(Gravity.RIGHT);
            layoutContenuMessage.setBackgroundColor(context.getColor(R.color.utopia_turquoise));
            layoutButtonsMessage.setVisibility(View.VISIBLE);
        }
        else {
            layoutContenuMessage.setBackgroundColor(context.getColor(R.color.utopia_turquoise_fonce));
            layoutMessage.setGravity(Gravity.LEFT);
            layoutButtonsMessage.setVisibility(View.GONE);
        }

        textHeureMessage.setText(message.getCreatedAtFormatted());
        textMessage.setText(message.getTexte());

        imageButtonEditMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ConversationActivity) context).updateMessage(message);
            }
        });
        imageButtonDeleteMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpClient httpClient = HttpClient.instanceOfClient();

                            String response = "";

                            response = httpClient.delete("messages/" + message.getId());

                            JSONObject responseJSON = new JSONObject(response);

                            if (responseJSON.has("SUCCÈS")) {
                                remove(message);
                                notifyDataSetChanged();
                            } else if (responseJSON.has("ERREUR")) {
                                Toast.makeText(context, responseJSON.getString("ERREUR"), Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        imageViewPieceJointe.setVisibility(View.GONE);
        textViewPieceJointe.setVisibility(View.GONE);

        if (message.getCheminDuFichier() != "null") {
            String chemin = message.getCheminDuFichier();

            if (chemin.endsWith("jpg") || chemin.endsWith("jpeg") || chemin.endsWith("png")) {
                imageViewPieceJointe.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Drawable image = Drawable.createFromStream((InputStream) new URL(HttpClient.urlSite + chemin).getContent(), chemin);

                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {
                                    imageViewPieceJointe.setImageDrawable(image);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            else {
                textViewPieceJointe.setVisibility(View.VISIBLE);
                textViewPieceJointe.setText(chemin.substring(chemin.lastIndexOf("/") + 1));

                textViewPieceJointe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(HttpClient.urlSite + chemin));
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        context.startActivity(intent);
                    }
                });
            }
        }

        return convertView;
    }
}