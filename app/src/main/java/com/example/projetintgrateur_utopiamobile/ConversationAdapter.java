package com.example.projetintgrateur_utopiamobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

        TextView idConversation = convertView.findViewById(R.id.textViewIdConversation);

        idConversation.setText(Integer.toString(conversation.getId()));

        return convertView;
    }
}
