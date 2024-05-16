package com.example.projetintgrateur_utopiamobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText inputCourriel = (EditText) findViewById(R.id.courrielInput);
        EditText inputPassword = (EditText) findViewById(R.id.passwordInput);
        TextView outputError = (TextView) findViewById(R.id.erreursOutput);

        Button btnConnexion = (Button) findViewById(R.id.btnConnexion);
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputError.setText("");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpClient httpClient = HttpClient.instanceOfClient();
                            String responsePOST = httpClient.post("token", "{ \"email\": \"" + inputCourriel.getText() + "\", \"password\": \"" + inputPassword.getText() + "\", \"token_name\": \"tokenAPI\" }");
                            JSONObject response = new JSONObject(responsePOST);
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
                            } else {
                                outputError.setText(response.toString());
                            }

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                //store token in a local file
                /*
                String filename = "token";
                String fileContents = ""; //TODO: add token
                try (FileOutputStream fos)
                */




                /*
                Intent intent = new Intent(MainActivity.this, detailsProfil.class);
                startActivity(intent);
                 */

                /*
                Intent intent = new Intent(MainActivity.this, transaction.class);
                startActivity(intent);
                 */


                //Intent intent = new Intent(MainActivity.this, detailsProfil.class);
                //startActivity(intent);

            }
        });
    }
}