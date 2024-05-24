/****************************************
 Fichier : ConversationAdapter.java
 @author Félix Barré
 Fonctionnalité : Adapteur pour le ListView des conversations
 Date : 13 mai 2024
 Vérification :

 =========================================================
 Historique de modifications :

 =========================================================
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ConversationAdapter extends ArrayAdapter<Conversation> {
    private Context context;

    /**
     *
     * @param context Contexte de l'adapter
     * @param conversations Liste des converstions à afficher
     *
     * Constructeur du ConversationAdapter
     */
    public ConversationAdapter(Context context, List<Conversation> conversations) {
        super(context, 0, conversations);
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
     * Gère l'initialisation de chaque row de l'adapter de conversations
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Conversation conversation = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_conversation, parent, false);
        }

        TextView textViewInterlocuteur = convertView.findViewById(R.id.textViewInterlocuteur);
        TextView textViewMessager = convertView.findViewById(R.id.textViewMessager);
        TextView textViewMessage = convertView.findViewById(R.id.textViewMessage);
        TextView textViewHeureMessage = convertView.findViewById(R.id.textViewHeureMessage);

        Message dernierMessage = conversation.getDernierMessage();

        User interlocuteur;

        if (dernierMessage.getEnvoyeur().getId() == UserManager.getAuthUser().getId()) {
            interlocuteur = dernierMessage.getReceveur();
            textViewMessager.setText(getContext().getResources().getString(R.string.vous) + " : ");
        }
        else {
            interlocuteur = dernierMessage.getEnvoyeur();
            textViewMessager.setText(interlocuteur.getPrenom() + " : ");
        }

        textViewInterlocuteur.setText(interlocuteur.getPrenom() + " " + interlocuteur.getNom());
        textViewMessage.setText(dernierMessage.getTexte());

        textViewHeureMessage.setText(dernierMessage.getCreatedAtFormatted());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConversationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id_conversation", conversation.getId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
