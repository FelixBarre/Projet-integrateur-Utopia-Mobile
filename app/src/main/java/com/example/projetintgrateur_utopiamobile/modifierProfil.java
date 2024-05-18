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

public class modifierProfil extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modifier_profil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnConfirmation = (Button) findViewById(R.id.confirmerButton);
        Button btnAnnulation = (Button) findViewById(R.id.annulerButton);
        Button btnMDP = (Button) findViewById(R.id.changeMotDePasseButton);

        btnConfirmation.setOnClickListener(this);
        btnAnnulation.setOnClickListener(this);
        btnMDP.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirmerButton) {
            //Modifier profil
        } else if (v.getId() == R.id.annulerButton) {
            finish();
        } else if (v.getId() == R.id.changeMotDePasseButton){
            //Fonctionnalité de priorité 2 à implémenter ici: Popup pour confirmer le changement d'activité. (Sauvegarder les changements avant de changer de fenêtre)
            Intent intent = new Intent(modifierProfil.this, changementMotDePasse.class);
            startActivity(intent);
        }
    }
}