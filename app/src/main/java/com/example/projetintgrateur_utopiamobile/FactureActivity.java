package com.example.projetintgrateur_utopiamobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

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

public class FactureActivity extends AppCompatActivity {

    public static ArrayList<Fournisseur> fournisseurs= new ArrayList<>();

    Spinner spinnerFournisseur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_facture);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinnerFournisseur = (Spinner) findViewById(R.id.fournisseurFacture);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = HttpClient.instanceOfClient();
                    String responseGET = httpClient.get("fournisseursApi");
                    JSONObject Json = new JSONObject(responseGET);

                    if (Json.has("data") && Json.getJSONArray("data").length() > 0) {
                        JSONArray arrayJson = Json.getJSONArray("data");

                        for (int i = 0; i < arrayJson.length(); i++) {
                            JSONObject objJson = arrayJson.getJSONObject(i);
                            Fournisseur fournisseur = new Fournisseur();
                            fournisseur.setId(objJson.getInt("id"));
                            fournisseur.setNom(objJson.getString("nom"));
                            fournisseur.setNom(objJson.getString("description"));
                            fournisseurs.add(fournisseur);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                            }
                        });

                    }else{


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }
}