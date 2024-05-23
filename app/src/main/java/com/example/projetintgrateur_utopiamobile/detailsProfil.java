package com.example.projetintgrateur_utopiamobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

public class detailsProfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_profil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        UserManager userManager = new UserManager();
        User userAuth = userManager.getAuthUser();

        AlertDialog.Builder builderConfirm = new AlertDialog.Builder(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (!extras.getString("status").isEmpty()) {
                if (extras.getString("status").toString().equals("profile-updated")) {
                    TextView status = (TextView) findViewById(R.id.statusLabel);
                    status.setTextColor(getColor(R.color.green));
                    status.setText(getString(R.string.succesConfirmationEditProfil));
                }
            }
        }

        TextView output = (TextView) findViewById(R.id.prenomOutput);
        output.setText(userAuth.getPrenom());

        output = (TextView) findViewById(R.id.nomOutput);
        output.setText(userAuth.getNom());

        output = (TextView) findViewById(R.id.telephoneOutput);
        output.setText(userAuth.getTelephone());

        output = (TextView) findViewById(R.id.courrielOutput);
        output.setText(userAuth.getEmail());

        output = (TextView) findViewById(R.id.noPorteOutput);
        if (userAuth.getNoPorte() != "null") {
            output.setText(String.format("%s - ", userAuth.getNoPorte()));
        } else {
            LinearLayout layoutAdresse = (LinearLayout) findViewById(R.id.layoutAdresse);
            layoutAdresse.removeView(output);
        }

        output = (TextView) findViewById(R.id.noCiviqueOutput);
        output.setText(userAuth.getNoCivique());

        output = (TextView) findViewById(R.id.rueOutput);
        output.setText(userAuth.getRue());

        output = (TextView) findViewById(R.id.villeOutput);

        TextView finalOutput = output;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = HttpClient.instanceOfClient();
                    String idVille = String.valueOf(userAuth.getIdVille());
                    String responseGET = httpClient.get("villeApi/" + idVille);
                    JSONObject response = new JSONObject(responseGET);

                    JSONArray data = new JSONArray(response.get("data").toString());
                    JSONObject dataSpecific = new JSONObject(data.get(0).toString());

                    finalOutput.setText(dataSpecific.get("nom").toString());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        output.setText(finalOutput.getText());

        output = (TextView) findViewById(R.id.codePostalOutput);
        output.setText(userAuth.getCodePostal());

        ImageButton edit = (ImageButton) findViewById(R.id.editIcon);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionManager connectionManager = new ConnectionManager(detailsProfil.this);
                if (connectionManager.isConnected()) {
                    Intent intent = new Intent(detailsProfil.this, modifierProfil.class);
                    startActivity(intent);
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
}