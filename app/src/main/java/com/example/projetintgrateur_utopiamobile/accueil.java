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

public class accueil  extends AppCompatActivity implements View.OnClickListener{

    private Button btnVoirPlus;
    private Button btnPayer;
    private Button btnDemande;


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

        btnVoirPlus = (Button) findViewById(R.id.buttonCompte);
        btnPayer = (Button) findViewById(R.id.btnPaiement);
        btnDemande = (Button) findViewById(R.id.btnDemande);

        btnVoirPlus.setOnClickListener(this);
        btnPayer.setOnClickListener(this);
        btnDemande.setOnClickListener(this);


    }

    public void onClick(View v){


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