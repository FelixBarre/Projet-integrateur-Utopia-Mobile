package com.example.projetintgrateur_utopiamobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
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
    AlertDialog.Builder builderConfirm;
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

        Context context = MainActivity.this;
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(MainActivity.this);
        sqLiteManager.close();
        context.deleteDatabase("BanqueUtopia");

        builderConfirm = new AlertDialog.Builder(this);

        EditText inputCourriel = (EditText) findViewById(R.id.courrielInput);
        EditText inputPassword = (EditText) findViewById(R.id.passwordInput);
        TextView outputError = (TextView) findViewById(R.id.erreursOutput);
        Button btnConnexion = (Button) findViewById(R.id.btnConnexion);

        TextView linkForgotPassword = (TextView) findViewById(R.id.linkForgotPassword);
        linkForgotPassword.setMovementMethod(LinkMovementMethod.getInstance());
        linkForgotPassword.setLinkTextColor(getResources().getColor(R.color.white));

        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionManager connectionManager = new ConnectionManager(MainActivity.this);
                if (connectionManager.isConnected()) {
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
                                } else if (response.has("SUCCÈS")) {
                                    httpClient.setTokenApi(response.get("SUCCÈS").toString());
                                    UserManager userManager = new UserManager();
                                    if (userManager.checkUserIsUser(inputCourriel.getText().toString())) {
                                        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(MainActivity.this);

                                        User user = userManager.getUser(inputCourriel.getText().toString());
                                        UserManager.setAuthUser(user);

                                        if (sqLiteManager.loadUserIntoDatabase(user)) {
                                            Intent intent = new Intent(MainActivity.this, accueil.class);
                                            startActivity(intent);
                                        } else {
                                            outputError.setText(getString(R.string.erreurChargementUser));
                                        }
                                    } else {
                                        outputError.setText(getString(R.string.userHasNoAccess));
                                    }
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
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