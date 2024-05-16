package com.example.projetintgrateur_utopiamobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ComptesBancairesActivity extends AppCompatActivity {

    ArrayList<CompteBancaire> comptes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comptes_bancaires);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView test = (TextView) findViewById(R.id.titleCompte);
        Button button = (Button) findViewById(R.id.button2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient api = HttpClient.instanceOfClient();
                    String responseGET = api.get("comptesBancaires");
                    test.setText("responseGET");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}