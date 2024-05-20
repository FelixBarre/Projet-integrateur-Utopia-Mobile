package com.example.projetintgrateur_utopiamobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.io.IOException;
import java.util.ArrayList;

public class ComptesBancairesActivity extends AppCompatActivity {

    public static ArrayList<CompteBancaire> comptes = new ArrayList<>();
    RecyclerView recyclerViewCompte;

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = HttpClient.instanceOfClient();
                    String responseGET = httpClient.get("comptesBancaires");
                    JSONObject Json = new JSONObject(responseGET);
                    JSONArray arrayJson = Json.getJSONArray("data");

                    for (int i = 0; i < arrayJson.length(); i++) {
                        JSONObject objJson = arrayJson.getJSONObject(i);
                        CompteBancaire compte = new CompteBancaire();
                        compte.setId_compte(objJson.getInt("id"));
                        compte.setNom(objJson.getString("nom"));
                        compte.setSolde(objJson.getDouble("solde"));
                        compte.setTaux_interet(objJson.getDouble("taux_interet"));
                        compte.setId_user(objJson.getInt("id_user"));
                        if (objJson.getInt("est_valide") == 1) {
                            compte.setEst_valide(true);
                        } else {
                            compte.setEst_valide(false);
                        }
                        comptes.add(compte);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Adapter_compte adapterCompte = new Adapter_compte(ComptesBancairesActivity.this, comptes);
                            recyclerViewCompte.setAdapter(adapterCompte);
                            recyclerViewCompte.setLayoutManager(new LinearLayoutManager(ComptesBancairesActivity.this));

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