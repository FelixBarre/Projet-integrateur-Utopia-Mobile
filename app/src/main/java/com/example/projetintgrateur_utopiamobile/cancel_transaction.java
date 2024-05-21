package com.example.projetintgrateur_utopiamobile;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class cancel_transaction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cancel_transaction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle bundle = getIntent().getExtras();

        String typeTransaction = bundle.getString("typeTransaction");
        String montantTransaction = bundle.getString("montantTransaction");
        String destinataireTransaction = bundle.getString("destinataireTransaction");

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        TextView date = (TextView) findViewById(R.id.dateTransaction);
        TextView descriptionTransaction = (TextView) findViewById(R.id.descriptionTransaction);

        date.setText(currentDate);
        descriptionTransaction.setText(typeTransaction+ " de "+ montantTransaction + " vers "+ destinataireTransaction);
    }
}