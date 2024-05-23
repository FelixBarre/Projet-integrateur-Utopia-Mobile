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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class EditCompteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_compte);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(EditCompteActivity.this);
        EditText editNomCompte = (EditText) findViewById(R.id.editTextEditNomCompte);
        Button modifButton = (Button) findViewById(R.id.editComptebutton);
        modifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomCompte = editNomCompte.getText().toString();
                if (nomCompte.isEmpty()) {
                    editNomCompte.setError("Ce champ est obligatoire");
                } else {
                    Intent intent = getIntent();
                    int idCompte = intent.getIntExtra("id_compte", 0);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HttpClient httpClient = HttpClient.instanceOfClient();
                                String responsePOST = httpClient.post("modification/compteBancaire", "{ \"id\": \"" + idCompte + "\", \"nom\": \"" + nomCompte + "\", \"token_name\": \"tokenAPI\" }");
                                JSONObject Json = new JSONObject(responsePOST);
                                sqLiteManager.updateNomCompteDB(idCompte, nomCompte);

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            Intent intent = new Intent(EditCompteActivity.this, ComptesBancairesActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).start();
                }
            }
        });
    }
}