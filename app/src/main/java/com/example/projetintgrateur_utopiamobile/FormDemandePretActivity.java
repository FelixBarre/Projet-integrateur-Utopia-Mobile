/****************************************
 Fichier : FormDemandePretActivity
 @author : Mathis Leduc
 Fonctionnalité : M-CTE-11 Ajouter une demande de prêt
 Date : 2024-05-23
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormDemandePretActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_demande_pret);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editRaison = (EditText) findViewById(R.id.editTextRaison);
        EditText editMontant = (EditText) findViewById(R.id.editTextMontant);
        Button ajoutButton = (Button) findViewById(R.id.ajoutDemandebutton);
        ajoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String raison = editRaison.getText().toString();
                String montantStr = editMontant.getText().toString();
                String regex = "^-?\\d+\\.\\d{2}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(montantStr);
                boolean match = matcher.find();

                if (raison.isEmpty()) {
                    editRaison.setError("Ce champ est obligatoire");
                } else if (montantStr.isEmpty()) {
                    editMontant.setError("Ce champ est obligatoire");
                }  else if (!match) {
                    editMontant.setError("Le montant doit avoir deux chiffres après le point.");
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HttpClient httpClient = HttpClient.instanceOfClient();
                                String responsePOST = httpClient.post("creation/demande_de_pret", "{ \"raison\": \"" + raison + "\", \"montant\": \"" + montantStr + "\"}");
                                JSONObject Json = new JSONObject(responsePOST);

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            Intent intent = new Intent(FormDemandePretActivity.this, DemandePretActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).start();
                }
            }
        });
    }
}