package com.example.projetintgrateur_utopiamobile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Note: Code pour la boite de dialogue inspiré de: https://www.geeksforgeeks.org/how-to-implement-custom-searchable-spinner-in-android/
 */

public class modifierProfil extends AppCompatActivity implements View.OnClickListener{
    TextView inputVille;
    Dialog searchVilleWindow;
    ArrayList<String> villesNom;
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

        inputVille = (TextView) findViewById(R.id.inputVille);
        searchVilleWindow = new Dialog(modifierProfil.this);
        villesNom = new ArrayList<>();

        Button btnConfirmation = (Button) findViewById(R.id.confirmerButton);
        Button btnAnnulation = (Button) findViewById(R.id.annulerButton);
        Button btnMDP = (Button) findViewById(R.id.changeMotDePasseButton);

        btnConfirmation.setOnClickListener(this);
        btnAnnulation.setOnClickListener(this);
        btnMDP.setOnClickListener(this);

        UserManager userManager = new UserManager();
        User userAuth = userManager.getAuthUser();

        TextView output = findViewById(R.id.prenomOutput);
        output.setText(userAuth.getPrenom());
        output = findViewById(R.id.nomOutput);
        output.setText(userAuth.getNom());

        EditText input = findViewById(R.id.courrielInput);
        input.setText(userAuth.getEmail());

        input = findViewById(R.id.telephoneInput);
        input.setText(userAuth.getTelephone());

        input = findViewById(R.id.noPorteInput);
        if (userAuth.getNoPorte() != "null") {
            input.setText(userAuth.getNoPorte());
        } else {
            input.setText("");
        }

        input = findViewById(R.id.noCiviqueInput);
        input.setText(userAuth.getNoCivique());

        input = findViewById(R.id.rueInput);
        input.setText(userAuth.getRue());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = HttpClient.instanceOfClient();
                    String responseGET = httpClient.get("villesApi");
                    JSONObject response = new JSONObject(responseGET);

                    JSONArray data = new JSONArray(response.get("data").toString());
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject dataSpecific = new JSONObject(data.get(i).toString());
                        villesNom.add(dataSpecific.get("nom").toString());
                        if (dataSpecific.get("id").equals(userAuth.getIdVille())) {
                            inputVille.setText(dataSpecific.get("nom").toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        inputVille.setOnClickListener(this);

        input = findViewById(R.id.codePostalInput);
        input.setText(userAuth.getCodePostal());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirmerButton) {
            //Modifier profil
        } else if (v.getId() == R.id.annulerButton) {
            finish();
        } else if (v.getId() == R.id.changeMotDePasseButton){
            //TODO: Fonctionnalité de priorité 2 à implémenter ici: Popup pour confirmer le changement d'activité. (Sauvegarder les changements avant de changer de fenêtre)
            finish();
            Intent intent = new Intent(modifierProfil.this, changementMotDePasse.class);
            startActivity(intent);
        } else if (v.getId() == R.id.inputVille) {
            searchVilleWindow.setContentView(R.layout.dialog_searchable_spinner_ville);
            searchVilleWindow.getWindow().setLayout(650, 800);
            searchVilleWindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            searchVilleWindow.show();

            EditText searchBarVilles = searchVilleWindow.findViewById(R.id.searchVille);
            ListView listeVilles = searchVilleWindow.findViewById(R.id.listeVilles);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(modifierProfil.this, android.R.layout.simple_list_item_1, villesNom);
            listeVilles.setAdapter(adapter);

            searchBarVilles.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listeVilles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    inputVille.setText(adapter.getItem(position));

                    searchVilleWindow.dismiss();
                }
            });
        }
    }
}