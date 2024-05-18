package com.example.projetintgrateur_utopiamobile;

public class DemandePret {

    private int id_demande;
    private String date_demande;
    private String date_traitement;
    private String raison;
    private double montant;
    private int id_etat_demande;
    private int id_demandeur;

    public DemandePret() {
    }

    public DemandePret(int id_demande, String date_demande, String date_traitement, String raison, double montant, int id_etat_demande, int id_demandeur) {
        this.id_demande = id_demande;
        this.date_demande = date_demande;
        this.date_traitement = date_traitement;
        this.raison = raison;
        this.montant = montant;
        this.id_etat_demande = id_etat_demande;
        this.id_demandeur = id_demandeur;
    }

    public int getId_demande() {
        return id_demande;
    }

    public void setId_demande(int id_demande) {
        this.id_demande = id_demande;
    }

    public String getDate_demande() {
        return date_demande;
    }

    public void setDate_demande(String date_demande) {
        this.date_demande = date_demande;
    }

    public String getDate_traitement() {
        return date_traitement;
    }

    public void setDate_traitement(String date_traitement) {
        this.date_traitement = date_traitement;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public int getId_etat_demande() {
        return id_etat_demande;
    }

    public void setId_etat_demande(int id_etat_demande) {
        this.id_etat_demande = id_etat_demande;
    }

    public int getId_demandeur() {
        return id_demandeur;
    }

    public void setId_demandeur(int id_demandeur) {
        this.id_demandeur = id_demandeur;
    }
}
