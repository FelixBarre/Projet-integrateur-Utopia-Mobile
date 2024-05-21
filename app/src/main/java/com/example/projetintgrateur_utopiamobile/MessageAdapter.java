package com.example.projetintgrateur_utopiamobile;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

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
        LinearLayout layoutButtonsMessage = (LinearLayout) convertView.findViewById(R.id.layoutButtonsMessage);
        TextView textHeureMessage = (TextView) convertView.findViewById(R.id.textHeureMessage);
        TextView textMessage = (TextView) convertView.findViewById(R.id.textMessage);
        ImageButton imageButtonEditMessage = (ImageButton) convertView.findViewById(R.id.imageButtonEditMessage);
        ImageButton imageButtonDeleteMessage = (ImageButton) convertView.findViewById(R.id.imageButtonDeleteMessage);

        if (message.getEnvoyeur().getId() == UserManager.getAuthUser().getId()) {
            layoutMessage.setGravity(Gravity.RIGHT);
            textMessage.setBackgroundColor(context.getColor(R.color.utopia_turquoise));
            layoutButtonsMessage.setVisibility(View.VISIBLE);
        }
        else {
            textMessage.setBackgroundColor(context.getColor(R.color.utopia_turquoise_fonce));
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

        return convertView;
    }
}