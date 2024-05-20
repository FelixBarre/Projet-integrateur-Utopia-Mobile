package com.example.projetintgrateur_utopiamobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FormCompteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_compte);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editNomCompte = (EditText) findViewById(R.id.editTextNomCompte);
        Button ajoutButton = (Button) findViewById(R.id.ajoutComptebutton);
        ajoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomCompte = editNomCompte.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpClient httpClient = HttpClient.instanceOfClient();
                            String responsePOST = httpClient.post("creation/compteBancaire", "{ \"nom\": \"" + nomCompte + "\", \"token_name\": \"tokenAPI\" }");
                            JSONObject Json = new JSONObject(responsePOST);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Intent intent = new Intent(FormCompteActivity.this, ComptesBancairesActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();
            }
        });
    }
}