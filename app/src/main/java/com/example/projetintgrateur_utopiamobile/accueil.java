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
        btnVoirPlus.setOnClickListener(this);


    }

    public void onClick(View v){


        if (v.getId()==R.id.buttonCompte) {
            Intent intent = new Intent(accueil.this, ComptesBancairesActivity.class);
            startActivity(intent);
        }

    }
}