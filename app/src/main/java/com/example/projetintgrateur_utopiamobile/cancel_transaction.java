/*
 * Auteur(s):
 */
package com.example.projetintgrateur_utopiamobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class cancel_transaction extends AppCompatActivity implements View.OnClickListener{

    private Integer transactionType;
    private Integer transactionEtat;

    private Integer destinataireTransaction;
    private Integer expediteurTransaction;

    private double transactionMontant;

    private Button btnConfirm;
    private Button btnAnnuler;

    private Integer idFacture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cancel_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnConfirm = (Button) findViewById(R.id.confirmCancel);
        btnAnnuler = (Button) findViewById(R.id.cancelTransaction);

        btnConfirm.setOnClickListener(this);
        btnAnnuler.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();

        String typeTransaction = bundle.getString("typeTransaction");
        String montantTransaction = bundle.getString("montantTransaction");
        String destinataireTransaction = bundle.getString("destinataireTransaction");
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        TextView date = (TextView) findViewById(R.id.dateTransaction);
        TextView descriptionTransaction = (TextView) findViewById(R.id.descriptionTransaction);

        date.setText(currentDate);
        descriptionTransaction.setText(typeTransaction+ " de "+ montantTransaction + " vers "+ destinataireTransaction);
    }

    public void onClick(View v){
        if(v.getId()==R.id.confirmCancel) {


            Bundle bundle = getIntent().getExtras();

            String montant = bundle.getString("montantTransaction");
            String type = bundle.getString("typeTransaction");

            transactionMontant = Double.parseDouble(montant);
            transactionEtat = 2;


            if(type.equals("Dépôt")){
                transactionType = 1;
                destinataireTransaction = UserManager.getAuthUser().getId();
                expediteurTransaction = 0;
                idFacture = 0;
            } else if (type.equals("Rétrait")) {
                transactionType = 2;
                destinataireTransaction = 0;
                expediteurTransaction = UserManager.getAuthUser().getId();
                idFacture = 0;
            } else if (type.equals("Virement")) {
                transactionType = 3;
                destinataireTransaction = 2;
                expediteurTransaction = UserManager.getAuthUser().getId();
                idFacture = 0;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpClient httpClient = HttpClient.instanceOfClient();
                        String responsePOST = httpClient.post("transactionApi/new", "{ \"montant\": \""+ montant +"\", " +
                                "\"id_compte_envoyeur\":\""+expediteurTransaction + "\","+
                                "\"id_compte_receveur\":\""+destinataireTransaction +"\"," +
                                "\"id_type_transaction\":\""+transactionType+"\"," +
                                "\"id_etat_transaction\":\""+transactionEtat+"\"" +
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

            Intent intent = new Intent(cancel_transaction.this, accueil.class);
            startActivity(intent);

        } else if (v.getId()==R.id.cancelTransaction) {
            Intent intent = new Intent(cancel_transaction.this, transaction.class);
            startActivity(intent);
        }
    }

}