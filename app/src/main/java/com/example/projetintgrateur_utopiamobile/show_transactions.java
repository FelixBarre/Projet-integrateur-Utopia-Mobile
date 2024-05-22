package com.example.projetintgrateur_utopiamobile;

import android.os.Bundle;
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

public class show_transactions extends AppCompatActivity {
    ArrayList<TransactionClass> transactions = new ArrayList<>();
    RecyclerView recyclerViewTransaction;

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

        recyclerViewTransaction = (RecyclerView) findViewById(R.id.recyclerViewTransaction);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = HttpClient.instanceOfClient();
                    String responseGET = httpClient.get("transactionsApiAll");
                    JSONObject Json = new JSONObject(responseGET);
                    JSONArray arrayJson = Json.getJSONArray("data");

                    for (int i = 0; i < arrayJson.length(); i++) {
                        JSONObject objJson = arrayJson.getJSONObject(i);
                        TransactionClass  transaction = new TransactionClass();
                        transaction.setId(objJson.getInt("id"));
                        transaction.setMontant(objJson.getDouble("montant"));
                        if (objJson.getString("id_compte_envoyeur").equals(null)){
                            transaction.setCompteEnvoyeur(objJson.getString("id_compte_receveur"));
                        } else if (objJson.getString("id_compte_receveur").equals(null)) {
                            transaction.setCompteReceveur(objJson.getString("id_compte_envoyeur"));
                        }else{
                            transaction.setCompteEnvoyeur(objJson.getString("id_compte_envoyeur"));
                            transaction.setCompteReceveur(objJson.getString("id_compte_receveur"));
                        }

                        transaction.setTypeTransaction(objJson.getString("id_type_transaction"));
                        transaction.setEtatTransaction(objJson.getString("id_etat_transaction"));
                        transaction.setDateTransaction(objJson.getString("created_at"));
                        transaction.setDateTransactionModifie(objJson.getString("updated_at"));
                        transactions.add(transaction);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Adapter_transaction adapterTransaction = new Adapter_transaction(show_transactions.this, transactions);
                            recyclerViewTransaction.setAdapter(adapterTransaction);
                            recyclerViewTransaction.setLayoutManager(new LinearLayoutManager(show_transactions.this));


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