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

public class PlusActivity extends AppCompatActivity implements View.OnClickListener{

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

        tableRow = (TableRow) findViewById(R.id.ongletDemandeDesactivation);
        tableRow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ongletDemandePret) {
            finish();
            Intent intent = new Intent(PlusActivity.this, DemandePretActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.ongletDemandeDesactivation) {
            finish();
            Intent intent = new Intent(PlusActivity.this, DesactivationCompte.class);
            startActivity(intent);
        }
    }
}