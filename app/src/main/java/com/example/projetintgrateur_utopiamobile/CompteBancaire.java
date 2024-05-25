/****************************************
 Fichier : CompteBancaire
 @author : Mathis Leduc
 Date : 2024-05-23
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

public class CompteBancaire {
    private int id_compte;
    private String nom;
    private double solde;
    private double taux_interet;
    private int id_user;
    private boolean est_valide;

    /**
     *
     * Constructeur sans paramètre de l'objet Compte Bancaire
     */
    public CompteBancaire() {
    }

    /**
     *
     * @param id_compte id de la bd du compte
     * @param nom nom du compte
     * @param solde solde du compte
     * @param taux_interet taux d'intérêt du compte
     * @param id_user id du propriétaire du compte
     * @param est_valide boolean qui est true si le compte est valide
     *
     * Constructeur de l'objet Compte Bancaire
     */
    public CompteBancaire(int id_compte, String nom, double solde, double taux_interet, int id_user, boolean est_valide) {
        this.id_compte = id_compte;
        this.nom = nom;
        this.solde = solde;
        this.taux_interet = taux_interet;
        this.id_user = id_user;
        this.est_valide = est_valide;
    }

    /**
     *
     * @return retourne l'id de la bd du compte
     */
    public int getId_compte() {
        return id_compte;
    }

    /**
     *
     * @param id_compte id de la bd du compte
     */
    public void setId_compte(int id_compte) {
        this.id_compte = id_compte;
    }

    /**
     *
     * @return retourne le nom du compte
     */
    public String getNom() {
        return nom;
    }

    /**
     *
     * @param nom nom du compte
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     *
     * @return retourne le solde du compte
     */
    public double getSolde() {
        return solde;
    }

    /**
     *
     * @param solde solde du compte
     */
    public void setSolde(double solde) {
        this.solde = solde;
    }

    /**
     *
     * @return retourne le taux d'intérêt du compte
     */
    public double getTaux_interet() {
        return taux_interet;
    }

    /**
     *
     * @param taux_interet le taux d'intérêt du compte
     */
    public void setTaux_interet(double taux_interet) {
        this.taux_interet = taux_interet;
    }

    /**
     *
     * @return retourne l'id du propriétaire du compte
     */
    public int getId_user() {
        return id_user;
    }

    /**
     *
     * @param id_user l'id du propriétaire du compte
     */
    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    /**
     *
     * @return retourne si le compte est valide
     */
    public boolean isEst_valide() {
        return est_valide;
    }

    /**
     *
     * @param est_valide si le compte est valide ou non
     */
    public void setEst_valide(boolean est_valide) {
        this.est_valide = est_valide;
    }

    /**
     * override de equals qui compare deux comptes par leur id
     *
     * @param o object à comparer
     *  @return retourne si le compte est égal ou non
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompteBancaire compte = (CompteBancaire) o;
        return id_compte == compte.id_compte;
    }
}
