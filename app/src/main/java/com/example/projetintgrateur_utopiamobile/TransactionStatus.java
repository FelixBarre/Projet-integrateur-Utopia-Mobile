/****************************************
 Fichier : TransactionStatus
 @author : Joel Tidjane
 Date : 2024-05-26
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Création de la class TransactionStatus
 * Affiche un message de confirmation de la transaction
 */
public class TransactionStatus extends AppCompatActivity implements View.OnClickListener{

    /**
     * Déclaration de l'objet button pour retourner à l'activité main
     */
    private Button btnRetour;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     *     Création de l'activité TransactionStatus
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_status);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /**
         * Initialisation des objets
         */

        TextView titleTransaction = (TextView) findViewById(R.id.titleTransaction);
        ImageView imgSucces = (ImageView) findViewById(R.id.imgSucces);
        TextView titleStatusTransaction = (TextView) findViewById(R.id.titleStatusTransaction);
        btnRetour = (Button)findViewById(R.id.valideTransaction);

        btnRetour.setOnClickListener(this);

    }

    /**
     *
     * @param v The view that was clicked.
     *
     */
    @Override
    public void onClick(View v) {
        /**
         * Retour à l'activité principale accueil
         */
        if (v.getId()==R.id.valideTransaction){
            Intent intent = new Intent(TransactionStatus.this, accueil.class);
            startActivity(intent);
            finish();
        }
    }
}