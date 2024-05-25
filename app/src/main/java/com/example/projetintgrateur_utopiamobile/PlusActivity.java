/****************************************
 Fichier : PlusActivity
 @author : Mathis Leduc, Max Belval-Michaud, Joel Tidjane
 Date : 2024-05-23
 ****************************************/

package com.example.projetintgrateur_utopiamobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Classe pour l'activité du menu Plus
 */
public class PlusActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     * Fonction de création de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plus);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TableRow tableRow = (TableRow) findViewById(R.id.ongletDemandePret);
        tableRow.setOnClickListener(this);

        tableRow = (TableRow) findViewById(R.id.ongletPaiementfacture);
        tableRow.setOnClickListener(this);

        tableRow = (TableRow) findViewById(R.id.ongletDemandeDesactivation);
        tableRow.setOnClickListener(this);

        tableRow = (TableRow) findViewById(R.id.ongletDemandeDesactivationProfile);
        tableRow.setOnClickListener(this);
    }

    /**
     *
     * @param v The view that was clicked.
     *
     * Fonction qui gère l'envoi vers une activité lorsqu'une option du menu est appuyée
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ongletDemandePret) {
            finish();
            Intent intent = new Intent(PlusActivity.this, DemandePretActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.ongletPaiementfacture) {
            finish();
            Intent intent = new Intent(PlusActivity.this, FactureActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.ongletDemandeDesactivation) {
            finish();
            Intent intent = new Intent(PlusActivity.this, DesactivationCompte.class);
            startActivity(intent);
        } else if (v.getId() == R.id.ongletDemandeDesactivationProfile) {
            finish();
            Intent intent = new Intent(PlusActivity.this, DesactivationProfile.class);
            startActivity(intent);
        }
    }
}