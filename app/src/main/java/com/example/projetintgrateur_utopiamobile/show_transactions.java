/****************************************
 Fichier : show_transactions
 @author : Joel Tidjane
 Date : 2024-05-24
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Création de la classe show_transactions
 * Affiche les transactions d'un compte
 */

public class show_transactions extends AppCompatActivity {
    ArrayList<TransactionClass> transactions = new ArrayList<>();
    RecyclerView recyclerViewTransaction;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *Créaction de l'activité show_transactions
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_transactions);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /**
         * Iniitialisation des objets de la classe
         */

        Intent intent = getIntent();
        int idCompte = intent.getIntExtra("id_compte", 0);

        recyclerViewTransaction = (RecyclerView) findViewById(R.id.recyclerViewTransaction);

        /**
         * Recupération des transactions d'un compte depuis l'Api
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = HttpClient.instanceOfClient();
                    String responseGET = httpClient.get("transactionsApiAll/"+idCompte);
                    JSONObject Json = new JSONObject(responseGET);

                    if (Json.has("data") && Json.getJSONArray("data").length() > 0) {
                        JSONArray arrayJson = Json.getJSONArray("data");

                        for (int i = 0; i < arrayJson.length(); i++) {
                            JSONObject objJson = arrayJson.getJSONObject(i);
                            TransactionClass  transaction = new TransactionClass();
                            transaction.setId(objJson.getInt("id"));
                            transaction.setMontant(objJson.getDouble("montant"));
                            transaction.setCompteEnvoyeur(objJson.getString("id_compte_envoyeur"));
                            transaction.setCompteReceveur(objJson.getString("id_compte_receveur"));
                            transaction.setTypeTransaction(objJson.getString("id_type_transaction"));
                            transaction.setEtatTransaction(objJson.getString("id_etat_transaction"));
                            transaction.setDateTransaction(formatDate(objJson.getString("created_at")));
                            transaction.setDateTransactionModifie(objJson.getString("updated_at"));
                            transactions.add(transaction);
                        }

                        /**
                         * Initialisation de l'adapter pour la recycleView
                         */
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Adapter_transaction adapterTransaction = new Adapter_transaction(show_transactions.this, transactions);
                                recyclerViewTransaction.setAdapter(adapterTransaction);
                                recyclerViewTransaction.setLayoutManager(new LinearLayoutManager(show_transactions.this));


                            }
                        });

                    }else{

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        TextView messageErreur= findViewById(R.id.textError);
                                        messageErreur.setText("Désolé, il y a aucune transaction pour ce compte");
                                        messageErreur.setVisibility(View.VISIBLE);
                                    }
                                });


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

    private String formatDate(String dateString) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        try {
            Date date = originalFormat.parse(dateString);
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString; // En cas d'erreur, renvoie simplement la chaîne d'origine
        }
    }
}