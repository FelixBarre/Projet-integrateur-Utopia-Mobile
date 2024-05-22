package com.example.projetintgrateur_utopiamobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.SpannableString;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
        holder.RaisonDemandePret.setText(demandes.get(position).getRaison());
        SpannableString spannableString = new SpannableString(demandes.get(position).getDate_demande());
        spannableString.setSpan(new UnderlineSpan(), 0, demandes.get(position).getDate_demande().length(), 0);
        holder.DateDemandePret.setText(spannableString);
        String statut = "En attente";
        if (demandes.get(position).getId_etat_demande() == 1)
            statut = "Approuvée";
        else if (demandes.get(position).getId_etat_demande() == 2)
            statut = "Refusée";
        else if (demandes.get(position).getId_etat_demande() == 4)
            statut = "Annulée";
        holder.StatutDemandePret.setText(statut);
        String montant = "Montant: " + demandes.get(position).getMontant() + "$";
        holder.MontantDemandePret.setText(montant);
        holder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpClient httpClient = HttpClient.instanceOfClient();
                            String responsePOST = httpClient.post("annulation/demande_de_pret", "{ \"id\": \"" + demandes.get(position).getId_demande() + "\"}");
                            JSONObject Json = new JSONObject(responsePOST);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Intent intent = new Intent(context, DemandePretActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                }).start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return demandes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView RaisonDemandePret;
        TextView DateDemandePret;
        TextView StatutDemandePret;
        TextView MontantDemandePret;
        ImageView delButton;
        LinearLayout layoutDemandePret;

        public MyViewHolder(View itemView) {
            super(itemView);
            RaisonDemandePret = (TextView) itemView.findViewById(R.id.textRaison);
            DateDemandePret = (TextView) itemView.findViewById(R.id.textDateDemandePret);
            StatutDemandePret = (TextView) itemView.findViewById(R.id.textStatutValeurDemandePret);
            MontantDemandePret = (TextView) itemView.findViewById(R.id.textMontantDemandePret);
            delButton = (ImageView) itemView.findViewById(R.id.delDemande);
            layoutDemandePret = (LinearLayout) itemView.findViewById(R.id.layout_demandePret_row);
        }
    }
}
