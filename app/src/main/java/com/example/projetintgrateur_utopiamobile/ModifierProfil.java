package com.example.projetintgrateur_utopiamobile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Note: Code pour la boite de dialogue inspiré de: https://www.geeksforgeeks.org/how-to-implement-custom-searchable-spinner-in-android/
 */

public class ModifierProfil extends AppCompatActivity implements View.OnClickListener{
    TextView errorsOuput;
    Dialog searchVilleWindow;
    ArrayList<String> villesNom;
    TextView inputVille;
    EditText inputEmail;
    EditText inputTelephone;
    EditText inputNoPorte;
    EditText inputNoCivique;
    EditText inputRue;
    EditText inputCodePostal;
    AlertDialog.Builder builderConfirm;
    User userAuth;
    SQLiteManager sqLiteManager;
    ArrayAdapter<String> adapterVilles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modifier_profil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sqLiteManager = SQLiteManager.instanceOfDatabase(ModifierProfil.this);

        inputVille = (TextView) findViewById(R.id.inputVille);
        searchVilleWindow = new Dialog(ModifierProfil.this);
        villesNom = sqLiteManager.getNomsVilles();
        builderConfirm = new AlertDialog.Builder(this);

        Button btnConfirmation = (Button) findViewById(R.id.confirmerButton);
        Button btnAnnulation = (Button) findViewById(R.id.annulerButton);
        Button btnMDP = (Button) findViewById(R.id.changeMotDePasseButton);

        btnConfirmation.setOnClickListener(this);
        btnAnnulation.setOnClickListener(this);
        btnMDP.setOnClickListener(this);

        UserManager userManager = new UserManager();
        userAuth = userManager.getAuthUser();

        errorsOuput = findViewById(R.id.erreursOutput);
        errorsOuput.setText("");

        TextView output = findViewById(R.id.prenomOutput);
        output.setText(userAuth.getPrenom());
        output = findViewById(R.id.nomOutput);
        output.setText(userAuth.getNom());

        inputEmail = findViewById(R.id.courrielInput);
        inputEmail.setText(userAuth.getEmail());

        inputTelephone = findViewById(R.id.telephoneInput);
        inputTelephone.setText(userAuth.getTelephone());

        inputNoPorte = findViewById(R.id.noPorteInput);
        if (userAuth.getNoPorte() != "null") {
            inputNoPorte.setText(userAuth.getNoPorte());
        } else {
            inputNoPorte.setText("");
        }

        inputNoCivique = findViewById(R.id.noCiviqueInput);
        inputNoCivique.setText(userAuth.getNoCivique());

        inputRue = findViewById(R.id.rueInput);
        inputRue.setText(userAuth.getRue());

        if (Objects.equals(villesNom.get(0), "unloaded")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    performPostDelayTask();
                }
            }, 800);
        } else {
            inputVille.setText(sqLiteManager.getVilleById(userAuth.getIdVille()));
            inputVille.setTag(userAuth.getIdVille());
        }

        inputVille.setOnClickListener(this);

        inputCodePostal = findViewById(R.id.codePostalInput);
        inputCodePostal.setText(userAuth.getCodePostal());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
                case RequestCodes.MODIFIER_PROFILE_REQUEST_CODE:
                    SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(ModifierProfil.this);
                    try {
                        String responsePUT = data.getStringExtra("response");
                        JSONObject response = new JSONObject(responsePUT);
                        JSONObject erreur = null;

                        if (response.has("ERREUR")) {
                            if (response.get("ERREUR").getClass() == JSONObject.class) {
                                erreur = new JSONObject(response.get("ERREUR").toString());
                                for (int i = 0; i < erreur.length(); i++) {
                                    String erreurString = erreur.getString(erreur.names().get(i).toString());
                                    String correctedErreurString = erreurString.replaceAll("[]\"\\[]", "") + "\n";
                                    errorsOuput.append(correctedErreurString);
                                }
                            } else {
                                errorsOuput.setText(response.get("ERREUR").toString());
                            }
                        } else if (response.has("SUCCÈS")) {
                            UserManager.getAuthUser().setEmail(inputEmail.getText().toString());
                            UserManager.getAuthUser().setTelephone(inputTelephone.getText().toString());
                            if (inputNoPorte.getText().toString().isEmpty()) {
                                UserManager.getAuthUser().setNoPorte("null");
                            } else {
                                UserManager.getAuthUser().setNoPorte(inputNoPorte.getText().toString());
                            }
                            UserManager.getAuthUser().setNoCivique(inputNoCivique.getText().toString());
                            UserManager.getAuthUser().setRue(inputRue.getText().toString());
                            UserManager.getAuthUser().setIdVille(Integer.parseInt(inputVille.getTag().toString()));
                            UserManager.getAuthUser().setCodePostal(inputCodePostal.getText().toString());

                            sqLiteManager.updateUserDB(UserManager.getAuthUser());

                            finish();
                            Intent intent = new Intent(ModifierProfil.this, DetailsProfil.class);
                            intent.putExtra("status", "profile-updated");
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirmerButton) {
            ConnectionManager connectionManager = new ConnectionManager(ModifierProfil.this);
            if (connectionManager.isConnected()) {
                Intent loadingHttp = new Intent(ModifierProfil.this, LoadingHttp.class);
                loadingHttp.putExtra("method", "PUT");
                loadingHttp.putExtra("route", "modification/profileApi");

                String bodyRequest = "{ ";
                bodyRequest += "\"id\" : " + String.valueOf(UserManager.getAuthUser().getId()) + ",";
                bodyRequest += "\"prenom\" : \"" + UserManager.getAuthUser().getPrenom() + "\",";
                bodyRequest += "\"nom\" : \"" + UserManager.getAuthUser().getNom() + "\",";
                bodyRequest += "\"courriel\" : \"" + inputEmail.getText().toString() + "\",";
                bodyRequest += "\"telephone\" : \"" + inputTelephone.getText().toString() + "\",";
                bodyRequest += "\"appt\" : \"" + inputNoPorte.getText().toString() + "\",";
                bodyRequest += "\"noCivique\" : \"" + inputNoCivique.getText().toString() + "\",";
                bodyRequest += "\"rue\" : \"" + inputRue.getText().toString() + "\",";
                bodyRequest += "\"id_ville\" : \"" + (String) inputVille.getTag().toString() + "\",";
                bodyRequest += "\"codePostal\" : \"" + inputCodePostal.getText().toString() + "\"";
                bodyRequest += "}";

                loadingHttp.putExtra("body", bodyRequest);

                startActivityForResult(loadingHttp, RequestCodes.MODIFIER_PROFILE_REQUEST_CODE);
            } else {
                builderConfirm.setMessage(getString(R.string.connexionFailedMessage));
                builderConfirm.setPositiveButton(getString(R.string.retour), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertConfirm = builderConfirm.create();
                alertConfirm.setTitle(getString(R.string.attentionAlert));
                alertConfirm.show();
            }
        } else if (v.getId() == R.id.annulerButton) {
            finish();
        } else if (v.getId() == R.id.changeMotDePasseButton){
            builderConfirm.setMessage(R.string.confirmationContinuationVersMDPLabel);
            builderConfirm.setPositiveButton(R.string.confirmer, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    Intent intent = new Intent(ModifierProfil.this, changementMotDePasse.class);
                    startActivity(intent);
                }
            });

            builderConfirm.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alertConfirm = builderConfirm.create();
            alertConfirm.setTitle(R.string.attentionAlert);
            alertConfirm.show();
        } else if (v.getId() == R.id.inputVille) {
            searchVilleWindow.setContentView(R.layout.dialog_searchable_spinner_ville);
            searchVilleWindow.getWindow().setLayout(650, 800);
            searchVilleWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            searchVilleWindow.show();

            EditText searchBarVilles = searchVilleWindow.findViewById(R.id.searchVille);
            ListView listeVilles = searchVilleWindow.findViewById(R.id.listeVilles);

            if (!Objects.equals(villesNom.get(0), "unloaded")) {
                adapterVilles = new ArrayAdapter<>(ModifierProfil.this, android.R.layout.simple_list_item_1, villesNom);
                listeVilles.setAdapter(adapterVilles);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        performPostDelayTask();
                    }
                }, 800);
            }


            searchBarVilles.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapterVilles.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            listeVilles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    inputVille.setText(adapterVilles.getItem(position));
                    inputVille.setTag(sqLiteManager.getIdVille(adapterVilles.getItem(position)));
                    searchVilleWindow.dismiss();
                }
            });
        }
    }

    private void performPostDelayTask() {
        villesNom = sqLiteManager.getNomsVilles();
        adapterVilles = new ArrayAdapter<>(ModifierProfil.this, android.R.layout.simple_list_item_1, villesNom);
    }
}