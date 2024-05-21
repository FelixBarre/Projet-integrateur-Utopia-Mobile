package com.example.projetintgrateur_utopiamobile;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private Context context;
    public MessageAdapter(Context context, List<Message> messages) {
        super(context, 0, messages);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message message = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_message, parent, false);
        }

        LinearLayout layoutMessage = (LinearLayout) convertView.findViewById(R.id.layoutMessage);
        TextView textHeureMessage = (TextView) convertView.findViewById(R.id.textHeureMessage);
        TextView textMessage = (TextView) convertView.findViewById(R.id.textMessage);

        if (message.getEnvoyeur().getId() == UserManager.getAuthUser().getId()) {
            layoutMessage.setGravity(Gravity.RIGHT);
            textMessage.setBackgroundColor(context.getColor(R.color.utopia_turquoise));
        }
        else {
            textMessage.setBackgroundColor(context.getColor(R.color.utopia_turquoise_fonce));
            layoutMessage.setGravity(Gravity.LEFT);
        }

        textHeureMessage.setText(message.getCreatedAtFormatted());
        textMessage.setText(message.getTexte());

        return convertView;
    }
}