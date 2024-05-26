/****************************************
 Fichier : LoadingTransaction
 @author : Joel Tidjane
 Date : 2024-05-26
 ****************************************/

package com.example.projetintgrateur_utopiamobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Activité de chargement lorsque une transaction est effectuée
 */
public class LoadingTransaction extends AppCompatActivity {

    /**
     * Déclaration des propriétés de la class
     */
    private ProgressBar progressBar;
    private int progressStatus = 0;

    private Handler handler = new Handler();

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *Création de l'actvité Loadingtransaction
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /**
         * initialisation de la barre de progession
         */
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarTransaction);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus<50){
                    progressStatus++;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);

                        }
                    });
                    try {
                        Thread.sleep(100);

                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }

                /**
                 * Renvoie vers l'activité de succès de transaction après chargement
                 */
                Intent intent = new Intent(LoadingTransaction.this, TransactionStatus.class);



                startActivity(intent);
                finish();
            }
        }).start();

    }
}