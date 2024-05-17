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

public class HeaderView extends RelativeLayout implements View.OnClickListener {
    private LayoutInflater inflater;

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header_layout, this, true);

        ((ImageButton)this.findViewById(R.id.sortirButton)).setOnClickListener(this);
        ((ImageButton)this.findViewById(R.id.utopiaButton)).setOnClickListener(this);
        ((ImageButton)this.findViewById(R.id.profilButton)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sortirButton) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            getContext().startActivity(intent);
            ActivityCompat.finishAffinity((Activity) getContext());
        }
        else if (v.getId() == R.id.utopiaButton) {
            Intent intent = new Intent(getContext(), accueil.class);
            getContext().startActivity(intent);
        }
        else if (v.getId() == R.id.profilButton) {
            Intent intent = new Intent(getContext(), detailsProfil.class);
            getContext().startActivity(intent);
        }
    }
}
