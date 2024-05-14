package com.example.projetintgrateur_utopiamobile;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_compte extends RecyclerView.Adapter<Adapter_compte.MyViewHolder> {
    private String comptes[];
    private Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.compte_row, parent, false);
        return new MyViewHolder(view);
    }

    public Adapter_compte(Context context, String comptes[]) {
        this.context = context;
        this.comptes = comptes;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nomCompte.setText(comptes[position]);
    }

    @Override
    public int getItemCount() {
        return comptes.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nomCompte;
        TextView soldeTextCompte;
        TextView soldeValeurCompte;
        TextView derActCompte;
        LinearLayout layoutCompte;

        public MyViewHolder(View itemView) {
            super(itemView);
            nomCompte = (TextView) itemView.findViewById(R.id.textNomCompte);
            soldeTextCompte = (TextView) itemView.findViewById(R.id.textSoldeCompte);
            soldeValeurCompte = (TextView) itemView.findViewById(R.id.textSoldeValeurCompte);
            derActCompte = (TextView) itemView.findViewById(R.id.textDerAct);
            layoutCompte = (LinearLayout) itemView.findViewById(R.id.layout_compte_row);
        }
    }
}
