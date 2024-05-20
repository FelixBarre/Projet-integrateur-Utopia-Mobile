package com.example.projetintgrateur_utopiamobile;

import java.util.Date;

public class TransactionClass {
    private int id;
    private double montant;
    private String compteEnvoyeur;
    private String compteReceveur;
    private String typeTransaction;
    private String etatTransaction;
    private String dateTransaction;
    private String dateTransactionModifie;

    public TransactionClass() {
    }

    public TransactionClass(int id, float montant, String compteEnvoyeur, String compteReceveur, String typeTransaction, String etatTransaction, String dateTransaction, String dateTransactionModifie){
        this.id = id;
        this.montant = montant;
        this.compteEnvoyeur = compteEnvoyeur;
        this.compteReceveur = compteReceveur;
        this.typeTransaction = typeTransaction;
        this.etatTransaction = etatTransaction;
        this.dateTransaction = dateTransaction;
        this.dateTransactionModifie = dateTransactionModifie;
    }

    public void setId(int id){this.id = id;}
    public int getId(){ return id;}

    public void setMontant(double montant){this.montant = montant;}
    public double getMontant(){ return montant;}

    public void setCompteEnvoyeur(String compteEnvoyeur){this.compteReceveur = compteEnvoyeur;}
    public String getCompteEnvoyeur(){ return compteEnvoyeur;}

    public void setCompteReceveur(String compteReceveur){this.compteReceveur = compteReceveur;}
    public String getCompteReceveur(){ return compteReceveur;}

    public void setTypeTransaction(String typeTransactionr){this.typeTransaction = typeTransactionr;}
    public String getTypeTransaction(){ return typeTransaction;}

    public void setEtatTransaction(String etatTransaction){this.etatTransaction = etatTransaction;}
    public String getEtatTransaction(){ return etatTransaction;}

    public void setDateTransaction(String dateTransaction){this.dateTransaction = dateTransaction;}
    public String getDateTransaction(){ return dateTransaction;}

    public void setDateTransactionModifie(String dateTransactionModifie){this.dateTransactionModifie = dateTransactionModifie;}
    public String getDateTransactionModifie(){ return dateTransactionModifie;}

}
