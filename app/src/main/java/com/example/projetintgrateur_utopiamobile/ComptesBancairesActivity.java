/****************************************
 Fichier : ComptesBancairesActivity
 @author : Mathis Leduc
 Fonctionnalité : M-CTE-8 consulter les comptes bancaires
 Date : 2024-05-23
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class ComptesBancairesActivity extends AppCompatActivity {
    CompteBancaireManager compteManager = new CompteBancaireManager();
    public ArrayList<CompteBancaire> comptes = CompteBancaireManager.comptes;
    public ArrayList<CompteBancaire> prets = CompteBancaireManager.prets;
    public ArrayList<CompteBancaire> credits = CompteBancaireManager.credits;
    RecyclerView recyclerViewCompte;
    RecyclerView recyclerViewPret;
    RecyclerView recyclerViewCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comptes_bancaires);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerViewCompte = (RecyclerView) findViewById(R.id.recyclerViewCompte);
        recyclerViewPret = (RecyclerView) findViewById(R.id.recyclerViewPret);
        recyclerViewCredit = (RecyclerView) findViewById(R.id.recyclerViewCredit);
        ImageView addCompte = (ImageView) findViewById(R.id.addCompte);
        addCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComptesBancairesActivity.this, FormCompteActivity.class);
                startActivity(intent);
            }
        });

        compteManager.initComptes(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                performPostDelayTask();
            }
        }, 800);
    }

    private void performPostDelayTask() {
        if (prets.isEmpty()) {
            TextView titlePret = (TextView) findViewById(R.id.titlePret);
            titlePret.setVisibility(View.GONE);
        }
        if (credits.isEmpty()) {
            TextView titleCredit = (TextView) findViewById(R.id.titleCredit);
            titleCredit.setVisibility(View.GONE);
        }
        Adapter_compte adapterCompte = new Adapter_compte(ComptesBancairesActivity.this, comptes);
        recyclerViewCompte.setAdapter(adapterCompte);
        recyclerViewCompte.setLayoutManager(new LinearLayoutManager(ComptesBancairesActivity.this));
        Adapter_compte adapterCredit = new Adapter_compte(ComptesBancairesActivity.this, credits);
        recyclerViewCredit.setAdapter(adapterCredit);
        recyclerViewCredit.setLayoutManager(new LinearLayoutManager(ComptesBancairesActivity.this));
        Adapter_compte adapterPret = new Adapter_compte(ComptesBancairesActivity.this, prets);
        recyclerViewPret.setAdapter(adapterPret);
        recyclerViewPret.setLayoutManager(new LinearLayoutManager(ComptesBancairesActivity.this));
    }
}