package com.example.projetintgrateur_utopiamobile;

public class Facture {
    private int id;
    private String nom;
    private String description;
    private double montant_defini;
    private int jour_du_mois;
    private int id_fournisseur;

    public Facture(){

    }

    public Facture(int id, String nom, String description, double montant_defini, int jour_du_mois, int id_fournisseur ){
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.montant_defini = montant_defini;
        this.jour_du_mois = jour_du_mois;
        this.id_fournisseur = id_fournisseur;
    }

    public void setId(int id){this.id = id;}
    public int getId(){ return id;}

    public void setNom(String nom){this.nom = nom;}
    public String getNom(){ return nom;}

    public void setDescription(String description){this.description = description;}
    public String getDescription(){ return description;}

    public void setMontant_defini(double montant_defini){ this.montant_defini = montant_defini;}
    public double getMontant_defini(){return montant_defini;}

    public void setJour_du_mois(int jour_du_mois){this.jour_du_mois = jour_du_mois;}
    public int getJour_du_mois(){return jour_du_mois;}

    public void setId_fournisseur(int id_fournisseur){this.id_fournisseur = id_fournisseur;}
    public int getId_fournisseur(){return id_fournisseur;}

    @Override
    public String toString() {
        return String.valueOf(montant_defini);
    }



}
