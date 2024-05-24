/*
 * Auteur(s):
 */
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
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class FactureActivity extends AppCompatActivity implements View.OnClickListener{

    public ArrayList<Fournisseur> fournisseurs= new ArrayList<>();

    Spinner spinnerFournisseur;

    private Button btnTermine;
    private Button btnAnnule;

    private Integer transactionType;
    private Integer transactionEtat;

    private Integer destinataireTransaction;
    private Integer expediteurTransaction;

    private Integer idFacture;

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

        btnTermine = (Button) findViewById(R.id.valideTransaction);
        btnAnnule = (Button) findViewById(R.id.annulerTransaction);

        btnTermine.setOnClickListener(this);
        btnAnnule.setOnClickListener(this);

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
                            fournisseur.setDescription(objJson.getString("description"));
                            fournisseurs.add(fournisseur);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                ArrayAdapter<Fournisseur> adapter = new ArrayAdapter<>(FactureActivity.this, android.R.layout.simple_spinner_item, fournisseurs);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerFournisseur.setAdapter(adapter);
                            }
                        });

                    }else{

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                TextView textViewMessage = findViewById(R.id.textViewMessage);
                                textViewMessage.setText("Aucun fournisseur disponible.");
                                textViewMessage.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }

    public void onClick(View v){

        Spinner fournisseurSpinner = (Spinner) findViewById(R.id.destinationTansaction);
        EditText montant = (EditText) findViewById(R.id.montantTransaction);

        String fournisseurTransaction = fournisseurSpinner.getSelectedItem().toString();
        String montantTransaction = montant.getText().toString();
        double transactionMontant = Double.parseDouble(montantTransaction);


        transactionType = 4;
        transactionEtat = 3;
        destinataireTransaction = 0;
        expediteurTransaction = UserManager.getAuthUser().getId();
        idFacture = 0;



        if (v.getId()==R.id.valideTransaction) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {

                        HttpClient httpClient = HttpClient.instanceOfClient();
                        String responsePOST = httpClient.post("transactionApi/new", "{ \"montant\": \""+ transactionMontant +"\", " +
                                "\"id_compte_envoyeur\":\""+expediteurTransaction + "\","+
                                "\"id_compte_receveur\":\""+destinataireTransaction +"\"," +
                                "\"id_type_transaction\":\""+transactionType+"\"," +
                                "\"id_etat_transaction\":\""+transactionEtat+"\"," +
                                "\"id_facture\":\""+idFacture+"\"" +
                                " }");

                        JSONObject Json = new JSONObject(responsePOST);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();

            Intent intent = new Intent(FactureActivity.this, accueil.class);
            startActivity(intent);


        } else if (v.getId()==R.id.annulerTransaction) {

            Intent intent = new Intent(FactureActivity.this, cancel_transaction.class);
            startActivity(intent);

        }

    }

}