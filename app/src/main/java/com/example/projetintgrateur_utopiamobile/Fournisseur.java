package com.example.projetintgrateur_utopiamobile;

public class Fournisseur {
    private int id;
    private String nom;
    private String description;

    public Fournisseur() {

    }

    public Fournisseur(int id, String nom, String description ){
        this.id = id;
        this.nom = nom;
        this.description = description;
    }

    public void setId(int id){this.id = id;}
    public int getId(){ return id;}

    public void setNom(String id){this.nom = nom;}
    public String getNom(){ return nom;}

    public void setDescription(String description{this.description = description;}
    public String getDescription(){ return description;}

}
