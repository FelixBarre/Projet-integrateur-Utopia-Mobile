/****************************************
 Fichier : changementMotDePasse.java
 @author : Max Belval-Michaud
 Fonctionnalité : M-CTE-5 changement de mot de passe
 Date de création: 2024-05-15
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

/**
 * Classe pour l'activité de changement de mot de passe
 */
public class changementMotDePasse extends AppCompatActivity {
    EditText inputOldPassword;
    EditText inputNewPassword;
    EditText inputConfirmPassword;
    TextView outputError;
    AlertDialog.Builder builderConfirm;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     * Fonction de création de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_changement_mot_de_passe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputNewPassword = (EditText) findViewById(R.id.newPasswordInput);
        inputOldPassword = (EditText) findViewById(R.id.oldPasswordInput);
        inputConfirmPassword = (EditText) findViewById(R.id.confirmNewPasswordInput);
        outputError = (TextView) findViewById(R.id.erreursOutput);
        builderConfirm = new AlertDialog.Builder(this);
        Button btnSubmit = (Button) findViewById(R.id.soumettreButton);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            /**
             *
             * @param v The view that was clicked.
             *
             * Fonction qui s'exécute lorsque le bouton "Soumettre" est appuyé (Envoi de la requête à l'API)
             */
            @Override
            public void onClick(View v) {
                ConnectionManager connectionManager = new ConnectionManager(changementMotDePasse.this);
                if (connectionManager.isConnected()) {
                    outputError.setText("");
                    Intent loadingHttp = new Intent(changementMotDePasse.this, LoadingHttp.class);
                    loadingHttp.putExtra("method", "PUT");
                    loadingHttp.putExtra("route", "passwordApi");
                    loadingHttp.putExtra("body", "{ \"current_password:api\" : \"" + inputOldPassword.getText() + "\", \"password\" : \"" + inputNewPassword.getText() + "\", \"password_confirmation\" : \"" + inputConfirmPassword.getText() + "\" }");
                    startActivityForResult(loadingHttp, RequestCodes.CHANGEMENT_MDP_REQUEST_CODE);
                } else {
                    builderConfirm.setMessage(getString(R.string.connexionFailedMessage));
                    builderConfirm.setPositiveButton(getString(R.string.retour), new DialogInterface.OnClickListener() {
                        /**
                         *
                         * @param dialog the dialog that received the click
                         * @param which the button that was clicked (ex.
                         *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                         *              of the item clicked
                         *
                         * Fonction qui s'exécute lorsqu'une action dans la fenêtre de dialogue est appuyée
                         */
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

    /**
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     * Fonction qui gère la réception des résultats des startActivityForResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case RequestCodes.CHANGEMENT_MDP_REQUEST_CODE:
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
                        } else if (response.has("SUCCÈS")) {
                            outputError.setTextColor(getColor(R.color.green));
                            outputError.setText(getString(R.string.succèsConfirmationMDP));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }
}