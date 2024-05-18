package com.example.projetintgrateur_utopiamobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_demandePret extends RecyclerView.Adapter<Adapter_demandePret.MyViewHolder>{

    private ArrayList<DemandePret> demandes;
    private Context context;
    
    @NonNull
    @Override
    public Adapter_demandePret.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.demande_pret_row, parent, false);
        return new Adapter_demandePret.MyViewHolder(view);
    }

    public Adapter_demandePret(Context context, ArrayList<DemandePret> demandes) {
        this.context = context;
        this.demandes = demandes;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_demandePret.MyViewHolder holder, int position) {
        holder.DateDemandePret.setText(demandes.get(position).getDate_demande());
        String statut = "En attente";
        if (demandes.get(position).getId_etat_demande() == 1)
            statut = "Approuvée";
        else if (demandes.get(position).getId_etat_demande() == 2)
            statut = "Refusée";
        else if (demandes.get(position).getId_etat_demande() == 4)
            statut = "Annulée";
        holder.StatutDemandePret.setText(statut);
        holder.MontantDemandePret.setText(String.valueOf(demandes.get(position).getMontant()));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView DateDemandePret;
        TextView StatutDemandePret;
        TextView MontantDemandePret;
        LinearLayout layoutDemandePret;

        public MyViewHolder(View itemView) {
            super(itemView);
            DateDemandePret = (TextView) itemView.findViewById(R.id.textDateDemandePret);
            StatutDemandePret = (TextView) itemView.findViewById(R.id.textStatutValeurDemandePret);
            MontantDemandePret = (TextView) itemView.findViewById(R.id.textMontantDemandePret);
            layoutDemandePret = (LinearLayout) itemView.findViewById(R.id.layout_demandePret_row);
        }
    }
}
