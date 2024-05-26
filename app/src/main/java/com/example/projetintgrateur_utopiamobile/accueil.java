/****************************************
 Fichier : Accueil
 @author : Joel Tidjane
 Date : 2024-05-23
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class accueil  extends AppCompatActivity implements View.OnClickListener{
    CompteBancaireManager compteManager = new CompteBancaireManager();
    private Button btnVoirPlus;
    private Button btnPayer;
    private Button btnDemande;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *      Fonction principale pour lancer l'activité acceuil
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accueil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /**
         * Initialise les objets de l'activité accueil
         */

        compteManager.initComptes(accueil.this);
        btnVoirPlus = (Button) findViewById(R.id.buttonCompte);
        btnPayer = (Button) findViewById(R.id.btnPaiement);
        btnDemande = (Button) findViewById(R.id.btnDemande);

        btnVoirPlus.setOnClickListener(this);
        btnPayer.setOnClickListener(this);
        btnDemande.setOnClickListener(this);
    }

    public void onClick(View v){
        /**
         * Action sur les differents boutons de l'activité
         */

        if (v.getId()==R.id.buttonCompte) {
            Intent intent = new Intent(accueil.this, ComptesBancairesActivity.class);
            startActivity(intent);
        } else if (v.getId()==R.id.btnPaiement) {
            Intent intent = new Intent(accueil.this, transaction.class);
            startActivity(intent);

        } else if (v.getId()==R.id.btnDemande) {
            Intent intent = new Intent(accueil.this, DemandePretActivity.class);
            startActivity(intent);
        }

    }
}