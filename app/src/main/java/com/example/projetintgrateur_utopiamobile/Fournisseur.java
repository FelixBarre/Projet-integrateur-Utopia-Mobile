/****************************************
 Fichier : Fournisseur
 @author : Joel Tidjane
 Date : 2024-05-24
 ****************************************/
package com.example.projetintgrateur_utopiamobile;


/**
 * Création de la classe fournisseur
 */
public class Fournisseur {
    /**
     * Déclaration des propriétés de la classe fournisseur
     */
    private int id;
    private String nom;
    private String description;

    /**
     * Constructeur sans parametre sans parametre
     */
    public Fournisseur() {

    }

    /**
     * Constructeur avec parametres sans parametre
     * @param id
     * @param nom
     * @param description
     */
    public Fournisseur(int id, String nom, String description ){
        this.id = id;
        this.nom = nom;
        this.description = description;
    }

    /**
     *
     * @param id set l'id du fournisseur
     */
    public void setId(int id){this.id = id;}

    /**
     *
     * @return l'id du fournisseur
     */
    public int getId(){ return id;}

    /**
     *
     * @param nom set le nom du fournisseur
     */
    public void setNom(String nom){this.nom = nom;}

    /**
     *
     * @return le nom du fournisseur
     */
    public String getNom(){ return nom;}

    /**
     *
     * @param description set la description du fournisseur
     */
    public void setDescription(String description){this.description = description;}

    /**
     *
     * @return la description du fournisseur
     */
    public String getDescription(){ return description;}

    /**
     *
     * @return le nom du fournisseur
     */
    @Override
    public String toString() {
        return nom;
    }

}
