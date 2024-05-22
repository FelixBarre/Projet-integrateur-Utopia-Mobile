package com.example.projetintgrateur_utopiamobile;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_compte extends RecyclerView.Adapter<Adapter_compte.MyViewHolder> {
    private ArrayList<CompteBancaire> comptes;
    private Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.compte_row, parent, false);
        return new MyViewHolder(view);
    }

    public Adapter_compte(Context context, ArrayList<CompteBancaire> comptes) {
        this.context = context;
        this.comptes = comptes;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nomCompte.setText(comptes.get(position).getNom());
        String solde = " " + comptes.get(position).getSolde() + "$";
        holder.soldeValeurCompte.setText(solde);
        holder.modifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(context, EditCompteActivity.class);
                    intent.putExtra("id_compte", comptes.get(position).getId_compte());
                    startActivity(context, intent, null);
            }
        });

        holder.layoutCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, show_transactions.class);
                //intent.putExtra("id_compte", comptes.get(position).getId_compte());
                startActivity(context, intent, null);

            }
        });
    }

    @Override
    public int getItemCount() {
        return comptes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nomCompte;
        TextView soldeValeurCompte;
        TextView derActCompte;
        ImageView modifButton;
        LinearLayout layoutCompte;

        public MyViewHolder(View itemView) {
            super(itemView);
            nomCompte = (TextView) itemView.findViewById(R.id.textNomCompte);
            soldeValeurCompte = (TextView) itemView.findViewById(R.id.textSoldeValeurCompte);
            derActCompte = (TextView) itemView.findViewById(R.id.textDerAct);
            modifButton = (ImageView) itemView.findViewById(R.id.editCompte);
            layoutCompte = (LinearLayout) itemView.findViewById(R.id.layout_compte_row);
        }
    }
}
