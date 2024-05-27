/****************************************
 Fichier : Accueil
 @author : Joel Tidjane
 Date : 2024-05-23
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

    private ListView listeCompte;
    private ListView creditCompte;
    private ArrayList<String> comptesDisplay = new ArrayList<>();
    private ArrayList<String> creditCompteDisplay = new ArrayList<>();
    private ArrayAdapter<String> adapter;

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

        listeCompte = (ListView) findViewById(R.id.listeCompte);
        creditCompte = (ListView) findViewById(R.id.creditCompte);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_2, android.R.id.text1, comptesDisplay);
        listeCompte.setAdapter(adapter);


        btnVoirPlus = (Button) findViewById(R.id.buttonCompte);
        btnPayer = (Button) findViewById(R.id.btnPaiement);
        btnDemande = (Button) findViewById(R.id.btnDemande);

        btnVoirPlus.setOnClickListener(this);
        btnPayer.setOnClickListener(this);
        btnDemande.setOnClickListener(this);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new  HttpClient();
                    String responseGET = httpClient.get("comptesBancaires");
                    JSONObject jsonResponse = new JSONObject(responseGET);

                    if (jsonResponse.has("data") && jsonResponse.getJSONArray("data").length() > 0) {
                        JSONArray arrayJson = jsonResponse.getJSONArray("data");

                        for (int i = 0; i < arrayJson.length(); i++) {
                            JSONObject objJson = arrayJson.getJSONObject(i);

                            int userId = objJson.getInt("id_user");
                            User usermanager = UserManager.getAuthUser();
                            int connectedUserId = (usermanager != null) ? usermanager.getId() : 0;

                            if (userId == connectedUserId) {
                                String nom = objJson.getString("nom");
                                double solde = objJson.getDouble("solde");

                                String displayText = nom + " " + solde + "$";
                                comptesDisplay.add(displayText);

                                if (nom.equals("carte de crédit")) {
                                    creditCompteDisplay.add(displayText);
                                }
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                ArrayAdapter<String> creditAdapter = new ArrayAdapter<>(accueil.this, android.R.layout.simple_list_item_1, android.R.id.text1, creditCompteDisplay);
                                creditCompte.setAdapter(creditAdapter);
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


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