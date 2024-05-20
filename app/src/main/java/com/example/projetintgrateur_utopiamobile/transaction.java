package com.example.projetintgrateur_utopiamobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class transaction extends AppCompatActivity implements View.OnClickListener{

    private Button btnTermine;
    private Button btnAnnule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });

        Spinner spinner = (Spinner) findViewById(R.id.typeTransaction);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.transactionType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner destinataire = (Spinner) findViewById(R.id.destinationTansaction);

        ArrayList<String> nomComptes = new ArrayList<>();
        for (CompteBancaire compte : ComptesBancairesActivity.comptes) {
            nomComptes.add(compte.getNom());
        }

        ArrayAdapter<String> destinataireAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nomComptes);
        destinataireAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinataire.setAdapter(destinataireAdapter);


        btnTermine = (Button) findViewById(R.id.valideTransaction);
        btnAnnule = (Button) findViewById(R.id.annulerTransaction);

        btnTermine.setOnClickListener(this);
        btnAnnule.setOnClickListener(this);



    }

    public void onClick(View v){

        Spinner type = (Spinner) findViewById(R.id.typeTransaction);
        EditText Montant = (EditText) findViewById(R.id.montantTransaction);


        if (v.getId()==R.id.valideTransaction) {
            Intent intent = new Intent(transaction.this, ComptesBancairesActivity.class);
            startActivity(intent);
        } else if (v.getId()==R.id.annulerTransaction) {
            Intent intent = new Intent(transaction.this, cancel_transaction.class);

            startActivity(intent);
        }

    }
}