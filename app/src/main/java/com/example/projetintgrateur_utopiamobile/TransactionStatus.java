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

public class TransactionStatus extends AppCompatActivity implements View.OnClickListener{

    private Button btnRetour;

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

        TextView titleTransaction = (TextView) findViewById(R.id.titleTransaction);
        ImageView imgSucces = (ImageView) findViewById(R.id.imgSucces);
        TextView titleStatusTransaction = (TextView) findViewById(R.id.titleStatusTransaction);
        btnRetour = (Button)findViewById(R.id.valideTransaction);

        btnRetour.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.valideTransaction){
            Intent intent = new Intent(TransactionStatus.this, accueil.class);
            startActivity(intent);
            finish();
        }
    }
}