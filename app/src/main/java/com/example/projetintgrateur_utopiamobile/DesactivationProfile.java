package com.example.projetintgrateur_utopiamobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.util.ArrayList;

public class DesactivationProfile extends AppCompatActivity {
    SQLiteManager sqLiteManager;
    AlertDialog.Builder builderConfirm;
    TextView outputError;
    Spinner spinnerRaison;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_desactivation_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sqLiteManager = SQLiteManager.instanceOfDatabase(DesactivationProfile.this);
        builderConfirm = new AlertDialog.Builder(this);
        outputError = (TextView) findViewById(R.id.erreursOutput);

        spinnerRaison = (Spinner) findViewById(R.id.spinnerRaisonDesactivation);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.raisonsDesactivationProfil, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRaison.setAdapter(adapter);

        Button btnSoumettre = (Button) findViewById(R.id.soumettreButton);

        btnSoumettre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionManager connectionManager = new ConnectionManager(DesactivationProfile.this);
                if (connectionManager.isConnected()) {
                    outputError.setText("");
                    Intent loadingHttp = new Intent(DesactivationProfile.this, LoadingHttp.class);
                    String raison = spinnerRaison.getSelectedItem().toString();
                    String idUser = String.valueOf(UserManager.getAuthUser().getId());

                    loadingHttp.putExtra("method", "POST");
                    loadingHttp.putExtra("route", "creation/demande_de_desactivation");
                    loadingHttp.putExtra("body", "{ \"raison\" : \"" + raison + "\", \"id_demandeur\" : \"" + idUser + "\" }");
                    startActivityForResult(loadingHttp, RequestCodes.DESACTIVATION_PROFILE_REQUEST_CODE);
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
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case RequestCodes.DESACTIVATION_PROFILE_REQUEST_CODE:
                outputError.setText("");
                if (resultCode == Activity.RESULT_OK) {
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
                                    outputError.append(correctedErreurString);
                                }
                            } else {
                                outputError.setText(response.get("ERREUR").toString());
                            }
                        } else if (response.has("SUCCES")) {
                            outputError.setTextColor(getColor(R.color.green));
                            outputError.setText(getString(R.string.succèsEnvoiDesactivation));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }
}