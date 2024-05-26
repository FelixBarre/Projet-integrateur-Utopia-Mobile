/****************************************
 Fichier : TransactionClass
 @author : Joel Tidjane
 Date : 2024-05-24
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import java.util.Date;

/**
 * Création de la classe TransactionClass
 */
public class TransactionClass {
    /**
     * Déclaration des propriétés de la classe
     */
    private int id;
    private double montant;
    private String compteEnvoyeur;
    private String compteReceveur;
    private String typeTransaction;
    private String etatTransaction;
    private String dateTransaction;
    private String dateTransactionModifie;

    /**
     * Constructeur sans parametre de TransactionClass
     */
    public TransactionClass() {
    }

    /**
     * Constructeur avec parametre
     * @param id
     * @param montant
     * @param compteEnvoyeur
     * @param compteReceveur
     * @param typeTransaction
     * @param etatTransaction
     * @param dateTransaction
     * @param dateTransactionModifie
     */

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

    /**
     *
     * @param id set l'id de la transaction
     */
    public void setId(int id){this.id = id;}

    /**
     *
     * @return l'id de la transaction
     */
    public int getId(){ return id;}

    /**
     *
     * @param montant set le montant de la transaction
     */
    public void setMontant(double montant){this.montant = montant;}

    /**
     *
     * @return le montant de la transaction
     */
    public double getMontant(){ return montant;}

    /**
     *
     * @param compteEnvoyeur set le compte envoyeur de la transaction
     */
    public void setCompteEnvoyeur(String compteEnvoyeur){this.compteReceveur = compteEnvoyeur;}

    /**
     *
     * @return le compte envoyeur de la transaction
     */
    public String getCompteEnvoyeur(){ return compteEnvoyeur;}

    /**
     *
     * @param compteReceveur set le compte receveur de la transaction
     */
    public void setCompteReceveur(String compteReceveur){this.compteReceveur = compteReceveur;}

    /**
     *
     * @return le compte receveur de la transaction
     */
    public String getCompteReceveur(){ return compteReceveur;}

    /**
     *
     * @param typeTransactionr set le type d ela transaction
     */
    public void setTypeTransaction(String typeTransactionr){this.typeTransaction = typeTransactionr;}

    /**
     *
     * @return le type de la transaction
     */

    public String getTypeTransaction(){ return typeTransaction;}

    /**
     *
     * @param etatTransaction set l'etat de la transaction
     */

    public void setEtatTransaction(String etatTransaction){this.etatTransaction = etatTransaction;}

    /**
     *
     * @return l'état de la transaction
     */
    public String getEtatTransaction(){ return etatTransaction;}

    /**
     *
     * @param dateTransaction set la date de la transaction
     */
    public void setDateTransaction(String dateTransaction){this.dateTransaction = dateTransaction;}

    /**
     *
     * @return la date de la transaction
     */
    public String getDateTransaction(){ return dateTransaction;}

    /**
     *
     * @param dateTransactionModifie set la date de la modification de la transaction
     */
    public void setDateTransactionModifie(String dateTransactionModifie){this.dateTransactionModifie = dateTransactionModifie;}

    /**
     *
     * @return la date de la modification de la transaction
     */
    public String getDateTransactionModifie(){ return dateTransactionModifie;}

}
