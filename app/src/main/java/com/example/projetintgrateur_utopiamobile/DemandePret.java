/****************************************
 Fichier : DemandePret
 @author : Mathis Leduc
 Date : 2024-05-23
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

public class DemandePret {

    private int id_demande;
    private String date_demande;
    private String date_traitement;
    private String raison;
    private double montant;
    private int id_etat_demande;
    private int id_demandeur;

    /**
     *
     * Constructeur sans paramètre de l'objet DemandePret
     */
    public DemandePret() {
    }

    /**
     *
     * @param id_demande id de la bd de la demande
     * @param date_demande date de la création de la demande
     * @param date_traitement date de la traitement de la demande
     * @param raison raison de la demande
     * @param montant montant demandé du prêt
     * @param id_etat_demande id de l'état de traitement de la demande
     * @param id_demandeur id du demandeur
     *
     * Constructeur de l'objet DemandePret
     */
    public DemandePret(int id_demande, String date_demande, String date_traitement, String raison, double montant, int id_etat_demande, int id_demandeur) {
        this.id_demande = id_demande;
        this.date_demande = date_demande;
        this.date_traitement = date_traitement;
        this.raison = raison;
        this.montant = montant;
        this.id_etat_demande = id_etat_demande;
        this.id_demandeur = id_demandeur;
    }

    /**
     *
     * @return retourne l'id de la bd de la demande
     */
    public int getId_demande() {
        return id_demande;
    }

    public void setId_demande(int id_demande) {
        this.id_demande = id_demande;
    }

    /**
     *
     * @return retourne la date de la création de la demande
     */
    public String getDate_demande() {
        return date_demande;
    }

    public void setDate_demande(String date_demande) {
        this.date_demande = date_demande;
    }

    /**
     *
     * @return retourne la date de la traitement de la demande
     */
    public String getDate_traitement() {
        return date_traitement;
    }

    public void setDate_traitement(String date_traitement) {
        this.date_traitement = date_traitement;
    }

    /**
     *
     * @return retourne la raison de la demande
     */
    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    /**
     *
     * @return retourne le montant demandé du prêt
     */
    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    /**
     *
     * @return retourne l'id de l'état de traitement de la demande
     */
    public int getId_etat_demande() {
        return id_etat_demande;
    }

    public void setId_etat_demande(int id_etat_demande) {
        this.id_etat_demande = id_etat_demande;
    }

    /**
     *
     * @return retourne l'id du demandeur
     */
    public int getId_demandeur() {
        return id_demandeur;
    }

    public void setId_demandeur(int id_demandeur) {
        this.id_demandeur = id_demandeur;
    }
}
