/****************************************
 Fichier : DemandePretActivity
 @author : Mathis Leduc
 Fonctionnalité : M-CTE-13 consulter les demandes de prêt
 Date : 2024-05-23
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

import java.io.IOException;
import java.util.ArrayList;

public class DemandePretActivity extends AppCompatActivity {

    ArrayList<DemandePret> demandes = new ArrayList<>();
    RecyclerView recyclerViewDemandes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_demande_pret);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewDemandes = (RecyclerView) findViewById(R.id.recyclerViewDemandePret);
        ImageView addDemande = (ImageView) findViewById(R.id.addDemandePret);
        addDemande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemandePretActivity.this, FormDemandePretActivity.class);
                startActivity(intent);
                finish();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = HttpClient.instanceOfClient();
                    String responseGET = httpClient.get("demandes_de_pret/");
                    JSONObject Json = new JSONObject(responseGET);
                    JSONArray arrayJson = Json.getJSONArray("data");

                    for (int i = 0; i < arrayJson.length(); i++) {
                        JSONObject objJson = arrayJson.getJSONObject(i);
                        DemandePret demande = new DemandePret();
                        demande.setId_demande(objJson.getInt("id"));
                        demande.setDate_demande(objJson.getString("date_demande"));
                        demande.setDate_traitement(objJson.getString("date_traitement"));
                        demande.setRaison(objJson.getString("raison"));
                        demande.setMontant(objJson.getDouble("montant"));
                        demande.setId_etat_demande(objJson.getInt("id_etat_demande"));
                        demande.setId_demandeur(objJson.getInt("id_demandeur"));
                        demandes.add(demande);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Adapter_demandePret adapterDemandePret = new Adapter_demandePret(DemandePretActivity.this, demandes);
                            recyclerViewDemandes.setAdapter(adapterDemandePret);
                            recyclerViewDemandes.setLayoutManager(new LinearLayoutManager(DemandePretActivity.this));
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }
}