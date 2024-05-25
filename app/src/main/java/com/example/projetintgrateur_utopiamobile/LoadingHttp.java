/****************************************
 Fichier : LoadingHttp.java
 @author Félix Barré
 Fonctionnalité : Page d'écran de chargement pour réaliser une requête Http
 Date : 13 mai 2024
 Vérification :

 =========================================================
 Historique de modifications :

 =========================================================
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

/**
 * Classe pour envoyer des requêtes à l'API avec HttpClient.java avec un écran de chargement
 */
public class LoadingHttp extends AppCompatActivity {
    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     * Fonction principale lancée au départ de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading_http);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intentData = getIntent();

                    HttpClient httpClient = new HttpClient();

                    String response = "";

                    Intent result = new Intent();

                    if (intentData.hasExtra("method") && intentData.hasExtra("route")) {
                        String method = intentData.getStringExtra("method");
                        String route = intentData.getStringExtra("route");
                        String body = intentData.getStringExtra("body");
                        switch (method) {
                            case "GET":
                                try {
                                    response = httpClient.get(route);
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "POST":
                                try {
                                    response = httpClient.post(route, body);
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "PUT":
                                try {
                                    response = httpClient.put(route, body);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }

                        result.putExtra("response", response);
                        setResult(Activity.RESULT_OK, result);
                        finish();
                    }
                    else {
                        setResult(Activity.RESULT_CANCELED, result);
                        finish();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}