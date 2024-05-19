package com.example.projetintgrateur_utopiamobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;

public class ConversationAdapter extends ArrayAdapter<Conversation> {
    public ConversationAdapter(Context context, List<Conversation> conversations) {
        super(context, 0, conversations);
    }

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

        return convertView;
    }
}
