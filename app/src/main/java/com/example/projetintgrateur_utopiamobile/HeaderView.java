/****************************************
 Fichier : HeaderView
 @author : Félix Barré, Mathis Leduc
 Date : 2024-05-23
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.core.app.ActivityCompat;

public class HeaderView extends RelativeLayout implements View.OnClickListener {
    private LayoutInflater inflater;
    private Context context;
    private SQLiteManager sqLiteManager;
    AlertDialog.Builder builderConfirm;

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header_layout, this, true);

        ((ImageButton)this.findViewById(R.id.sortirButton)).setOnClickListener(this);
        ((ImageButton)this.findViewById(R.id.utopiaButton)).setOnClickListener(this);
        ((ImageButton)this.findViewById(R.id.profilButton)).setOnClickListener(this);

        sqLiteManager = SQLiteManager.instanceOfDatabase(context);
        builderConfirm = new AlertDialog.Builder(context);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sortirButton) {
            Intent intent = new Intent(getContext(), Connexion.class);
            getContext().startActivity(intent);
            ActivityCompat.finishAffinity((Activity) getContext());
        }
        else if (v.getId() == R.id.utopiaButton) {
            Intent intent = new Intent(getContext(), accueil.class);
            getContext().startActivity(intent);
            ((Activity) context).finish();
        }
        else if (v.getId() == R.id.profilButton) {
            if (sqLiteManager.isVilleLoaded()) {
                Intent intent = new Intent(getContext(), DetailsProfil.class);
                getContext().startActivity(intent);
                if (!(getContext() instanceof accueil)) {
                    ((Activity) context).finish();
                }
            } else {
                builderConfirm.setMessage(R.string.resourceNotLoaded);
                builderConfirm.setPositiveButton(R.string.retour, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertConfirm = builderConfirm.create();
                alertConfirm.setTitle(R.string.attentionAlert);
                alertConfirm.show();
            }
        }
    }
}
