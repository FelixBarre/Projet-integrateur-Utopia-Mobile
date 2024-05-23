/*
 * Auteur(s):
 */
package com.example.projetintgrateur_utopiamobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class LoadingHttp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading_http);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intentData = getIntent();

                    HttpClient httpClient = HttpClient.instanceOfClient();

                    String response = "";

                    Intent result = new Intent();

                    if (intentData.hasExtra("method") && intentData.hasExtra("route")) {
                        String method = intentData.getStringExtra("method");
                        String route = intentData.getStringExtra("route");
                        String body = intentData.getStringExtra("body");
                        switch (method) {
                            case "GET":
                                try {
                                    response = httpClient.get(route);
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "POST":
                                try {
                                    response = httpClient.post(route, body);
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "PUT":
                                try {
                                    response = httpClient.put(route, body);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }

                        result.putExtra("response", response);
                        setResult(Activity.RESULT_OK, result);
                        finish();
                    }
                    else {
                        setResult(Activity.RESULT_CANCELED, result);
                        finish();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}