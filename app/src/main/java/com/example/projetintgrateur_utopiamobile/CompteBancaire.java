package com.example.projetintgrateur_utopiamobile;

public class CompteBancaire {
    private int id_compte;
    private String nom;
    private double solde;
    private double taux_interet;
    private int id_user;
    private boolean est_valide;

    public CompteBancaire() {
    }

    public CompteBancaire(int id_compte, String nom, double solde, double taux_interet, int id_user, boolean est_valide) {
        this.id_compte = id_compte;
        this.nom = nom;
        this.solde = solde;
        this.taux_interet = taux_interet;
        this.id_user = id_user;
        this.est_valide = est_valide;
    }

    public int getId_compte() {
        return id_compte;
    }

    public void setId_compte(int id_compte) {
        this.id_compte = id_compte;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public double getTaux_interet() {
        return taux_interet;
    }

    public void setTaux_interet(double taux_interet) {
        this.taux_interet = taux_interet;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public boolean isEst_valide() {
        return est_valide;
    }

    public void setEst_valide(boolean est_valide) {
        this.est_valide = est_valide;
    }
}
