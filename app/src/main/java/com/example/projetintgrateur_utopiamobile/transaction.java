/*
 * Auteur(s):
 */
package com.example.projetintgrateur_utopiamobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class transaction extends AppCompatActivity implements View.OnClickListener{


    private Button btnTermine;
    private Button btnAnnule;

    private Integer transactionType;
    private Integer transactionEtat;

    private Integer destinataireTransaction;
    private Integer expediteurTransaction;

    private Integer idFacture;
    private String message;
    private TextView messageJson;
    private TextView titleDestinationTransaction;
    private Spinner spinnerDestinataire;
    
    private CheckBox verifInfos;

    private ArrayList<CompteBancaire> comptesBancaires;

    private int currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });

        Spinner spinner = (Spinner) findViewById(R.id.typeTransaction);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.transactionType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinnerDestinataire = (Spinner) findViewById(R.id.destinationTansaction);
        titleDestinationTransaction = (TextView) findViewById(R.id.titleDestinationTransaction);
        titleDestinationTransaction.setVisibility(View.INVISIBLE);

        comptesBancaires = new ArrayList<>(CompteBancaireManager.comptes);
        ArrayList<String> nomComptes = new ArrayList<>();
        User currentUser = UserManager.getAuthUser();
        currentUserId =  (currentUser != null) ? currentUser.getId() : 0;

        for (CompteBancaire compte : comptesBancaires) {
            if (compte.getId_compte() != currentUserId) {
                nomComptes.add(compte.getNom());
            }
        }

        ArrayAdapter<String> destinataireAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nomComptes);
        destinataireAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDestinataire.setAdapter(destinataireAdapter);

        messageJson = (TextView) findViewById(R.id.titleCreateTransaction);
        
        verifInfos = (CheckBox) findViewById(R.id.verifInfosTransaction);

        btnTermine = (Button) findViewById(R.id.valideTransaction);
        btnAnnule = (Button) findViewById(R.id.annulerTransaction);

        btnTermine.setOnClickListener(this);
        btnAnnule.setOnClickListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = (String) parent.getItemAtPosition(position);
                if ("Virement".equals(selectedType)) {
                    spinnerDestinataire.setVisibility(View.VISIBLE);
                    titleDestinationTransaction.setVisibility(View.VISIBLE);
                } else {
                    spinnerDestinataire.setVisibility(View.GONE);
                    titleDestinationTransaction.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    public void onClick(View v){

        Spinner type = (Spinner) findViewById(R.id.typeTransaction);
        EditText montant = (EditText) findViewById(R.id.montantTransaction);
        Spinner destinataire = (Spinner) findViewById(R.id.destinationTansaction);


        String selectedCompteNom = (String) destinataire.getSelectedItem();
        CompteBancaire selectedCompteBancaire = null;
        for (CompteBancaire compte : comptesBancaires) {
            if (compte.getNom().equals(selectedCompteNom)) {
                selectedCompteBancaire = compte;
                break;
            }
        }

        String typeTransaction = type.getSelectedItem().toString();

        if(typeTransaction.equals("Dépôt")){
            transactionType = 1;
            transactionEtat = 3;
            destinataireTransaction = currentUserId;
            expediteurTransaction = 0;
            idFacture = 0;
        } else if (typeTransaction.equals("Rétrait")) {
            transactionType = 2;
            transactionEtat = 3;
            destinataireTransaction = 0;
            expediteurTransaction = currentUserId;
            idFacture = 0;

        } else if (typeTransaction.equals("Virement")) {
            transactionType = 3;
            transactionEtat = 1;
            if (selectedCompteBancaire != null) {
                destinataireTransaction = selectedCompteBancaire.getId_compte();
            } else {

                destinataireTransaction = 0;
            }
            expediteurTransaction = currentUserId;
            idFacture = 0;
        }


        String montantTransaction = montant.getText().toString().trim();




        if (v.getId()==R.id.valideTransaction) {

            if (montantTransaction.isEmpty()) {
                montant.setError("Le montant est requis");
                return;
            } else if (!verifInfos.isChecked()) {
                verifInfos.setError("Veuillez cocher la case.");
                return;
            }
            double transactionMontant = Double.parseDouble(montantTransaction);
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
                        if (!responsePOST.isEmpty()) {
                            JSONObject Json = new JSONObject(responsePOST);
                            Toast.makeText(transaction.this, "Aucune donnée n'a été trouvée", Toast.LENGTH_SHORT).show();
                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    messageJson.setText(message);
                                    messageJson.setVisibility(View.VISIBLE);
                                    Toast.makeText(transaction.this, "Transaction effectuée avec succès.", Toast.LENGTH_SHORT).show();
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

            Intent intent = new Intent(transaction.this, accueil.class);
            startActivity(intent);
            finish();

        } else if (v.getId()==R.id.annulerTransaction) {

            if (montantTransaction.isEmpty()) {
                Intent intent = new Intent(transaction.this, accueil.class);
                startActivity(intent);
                finish();
            }else{

            Intent intent = new Intent(transaction.this, cancel_transaction.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putString("typeTransaction", type.getSelectedItem().toString());
            bundle.putString("montantTransaction", montantTransaction);
            bundle.putString("expediteurTransaction", destinataire.getSelectedItem().toString());
            bundle.putString("destinataireTransaction", destinataire.getSelectedItem().toString());
            intent.putExtras(bundle);
            startActivity(intent);
            finish();

            }

        }

    }
}