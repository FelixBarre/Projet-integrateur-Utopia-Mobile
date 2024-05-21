package com.example.projetintgrateur_utopiamobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.core.app.ActivityCompat;

public class FooterView extends RelativeLayout implements View.OnClickListener {
    private LayoutInflater inflater;

    public FooterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.footer_layout, this, true);

        ((ImageButton)this.findViewById(R.id.accueilButton)).setOnClickListener(this);
        ((ImageButton)this.findViewById(R.id.actionButton)).setOnClickListener(this);
        ((ImageButton)this.findViewById(R.id.messageButton)).setOnClickListener(this);
        ((ImageButton)this.findViewById(R.id.comptesButton)).setOnClickListener(this);
        ((ImageButton)this.findViewById(R.id.plusButton)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.accueilButton) {
            Intent intent = new Intent(getContext(), accueil.class);
            getContext().startActivity(intent);
        }
        else if (v.getId() == R.id.actionButton) {
            Intent intent = new Intent(getContext(), transaction.class);
            getContext().startActivity(intent);
        }
        else if (v.getId() == R.id.messageButton) {
            Intent intent = new Intent(getContext(), Conversations.class);
            getContext().startActivity(intent);
        }
        else if (v.getId() == R.id.comptesButton) {
            Intent intent = new Intent(getContext(), ComptesBancairesActivity.class);
            getContext().startActivity(intent);
        }
        else if (v.getId() == R.id.plusButton) {
            Intent intent = new Intent(getContext(), PlusActivity.class);
            getContext().startActivity(intent);

        }
    }
}
