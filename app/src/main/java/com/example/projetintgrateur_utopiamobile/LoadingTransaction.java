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

public class LoadingTransaction extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;

    private Handler handler = new Handler();

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
                        Thread.sleep(200);

                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }

                Intent intent = new Intent(LoadingTransaction.this, InscriptionSucces.class);



                startActivity(intent);
                finish();
            }
        }).start();

    }
}