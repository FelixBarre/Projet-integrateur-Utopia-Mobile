package com.example.projetintgrateur_utopiamobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

public class changementMotDePasse extends AppCompatActivity {

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

        EditText inputOldPassword = (EditText) findViewById(R.id.oldPasswordInput);
        EditText inputNewPassword = (EditText) findViewById(R.id.newPasswordInput);
        EditText inputConfirmPassword = (EditText) findViewById(R.id.confirmNewPasswordInput);
        TextView outputError = (TextView) findViewById(R.id.erreursOutput);
        Button btnSubmit = (Button) findViewById(R.id.soumettreButton);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputError.setText("");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpClient httpClient = HttpClient.instanceOfClient();
                            String responsePUT = httpClient.put("passwordApi", "{ \"current_password:api\" : \"" + inputOldPassword.getText() + "\", \"password\" : \"" + inputNewPassword.getText() + "\", \"password_confirmation\" : \"" + inputConfirmPassword.getText() + "\" }");
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
                }).start();
            }
        });
    }
}