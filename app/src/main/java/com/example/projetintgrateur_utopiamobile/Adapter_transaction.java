/****************************************
 Fichier : Adapter_transaction
 @author : Joel Tidjane
 Date : 2024-05-23
 ****************************************/

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

/**
 * Adapter pour afficher la liste des transaction d'un compte dans une recycleview
 */

public class Adapter_transaction extends RecyclerView.Adapter<Adapter_transaction.MyViewHolder>{
    private ArrayList<TransactionClass> transactions;
    private Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.show_transactions_row,parent, false);
        return new MyViewHolder(view);
    }

    public Adapter_transaction(Context context, ArrayList<TransactionClass> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.montant.setText(String.valueOf(transactions.get(position).getMontant()));
        holder.compteReceveur.setText("Vers : " + transactions.get(position).getCompteReceveur());
        holder.typeTransaction.setText(transactions.get(position).getTypeTransaction());
        holder.etatTransaction.setText(transactions.get(position).getEtatTransaction());
        holder.dateTransaction.setText(transactions.get(position).getDateTransaction());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView montant;
        TextView compteReceveur;
        TextView typeTransaction;
        TextView etatTransaction;
        TextView dateTransaction;
        LinearLayout layoutShowTransaction;

        public MyViewHolder(View itemView) {
            super(itemView);
            montant = (TextView) itemView.findViewById(R.id.montantTransaction);
            compteReceveur = (TextView) itemView.findViewById(R.id.destinataireTransaction);
            typeTransaction = (TextView) itemView.findViewById(R.id.typeTransaction);
            etatTransaction = (TextView) itemView.findViewById(R.id.etatTransaction);
            dateTransaction = (TextView) itemView.findViewById(R.id.dateTransaction);
            layoutShowTransaction = (LinearLayout) itemView.findViewById(R.id.transaction_row);
        }
    }

}
