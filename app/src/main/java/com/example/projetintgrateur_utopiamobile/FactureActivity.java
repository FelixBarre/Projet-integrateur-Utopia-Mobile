/****************************************
 Fichier : FactureActivity
 @author : Joel Tidjane
 Date : 2024-05-23
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

    /**
     * Déclarations des propriétés de la class FactureActivity
     */

    public ArrayList<Fournisseur> fournisseurs = new ArrayList<>();

    Spinner spinnerFournisseur;

    private Button btnTermine;
    private Button btnAnnule;

    private Integer transactionType;
    private Integer transactionEtat;
    private double montantTransaction;
    private Integer destinataireTransaction;
    private Integer expediteurTransaction;
    private Integer idFacture;

    private CheckBox verifInfos;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *  Création de l'activité Facture
     */
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

        /**
         * Initialisation des objets de l'activité
         */

        spinnerFournisseur = (Spinner) findViewById(R.id.fournisseurFacture);

        verifInfos= (CheckBox) findViewById(R.id.verifInfosTransaction);

        btnTermine = (Button) findViewById(R.id.valideTransaction);
        btnAnnule = (Button) findViewById(R.id.annulerTransaction);

        btnTermine.setOnClickListener(this);
        btnAnnule.setOnClickListener(this);

        /**
         * Réccupération des Fournisseur dépuis l'api FournisseurApi
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = HttpClient.instanceOfClient();
                    String responseGET = httpClient.get("fournisseursApi");
                    if (responseGET != null && !responseGET.isEmpty()) {
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
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView textViewMessage = findViewById(R.id.textViewMessage);
                                    textViewMessage.setText("Aucun fournisseur disponible.");
                                    textViewMessage.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Afficher un message d'erreur à l'utilisateur
                                //TextView textViewMessage = findViewById(R.id.textViewMessage);
                                //textViewMessage.setText("Erreur: réponse vide du serveur.");
                                //textViewMessage.setVisibility(View.VISIBLE);
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



        Fournisseur selectedFournisseur = (Fournisseur) spinnerFournisseur.getSelectedItem();

        if (selectedFournisseur == null) {
            return;
        }


        EditText montant = (EditText) findViewById(R.id.montantTransaction);
        String transactionMontant = montant.getText().toString().trim();


        transactionType = 4;
        transactionEtat = 3;
        destinataireTransaction = 0;
        expediteurTransaction = UserManager.getAuthUser().getId();
        idFacture = selectedFournisseur.getId();

        /**
         * Envoie des données vers l'api des Transaction
         */
        if (v.getId()==R.id.valideTransaction) {

            if (transactionMontant.isEmpty()) {
                montant.setError("Le montant est requis");
                return;
            } else if (!verifInfos.isChecked()) {
                verifInfos.setError("Veuillez cocher la case.");
                return;
            }

            montantTransaction = Double.parseDouble(transactionMontant);



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
                        Intent intent = new Intent(FactureActivity.this, accueil.class);
                        startActivity(intent);


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();


            /**
             * Retour à l'activité principale Acceuil
             */
        } else if (v.getId()==R.id.annulerTransaction) {

            Intent intent = new Intent(FactureActivity.this, accueil.class);
            startActivity(intent);

        }

    }

}