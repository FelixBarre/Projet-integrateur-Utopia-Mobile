/****************************************
 Fichier : Facture
 @author : Joel Tidjane
 Date : 2024-05-24
 ****************************************/

package com.example.projetintgrateur_utopiamobile;

/**
 * Création de la classe Facture
 */
public class Facture {

    /**
     * initalisation des propriétés de la classe Facture
     */
    private int id;
    private String nom;
    private String description;
    private double montant_defini;
    private int jour_du_mois;
    private int id_fournisseur;

    /**
     * Constructeur sans parametre de la classe Facture
     */

    public Facture(){

    }



    /**
     * Constructeur avec parametres de la classe Facture
     * @param id
     * @param nom
     * @param description
     * @param montant_defini
     * @param jour_du_mois
     * @param id_fournisseur
     */
    public Facture(int id, String nom, String description, double montant_defini, int jour_du_mois, int id_fournisseur ){
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.montant_defini = montant_defini;
        this.jour_du_mois = jour_du_mois;
        this.id_fournisseur = id_fournisseur;
    }

    /**
     * Setters et getters de la classe Facture
     *
     */

    /**
     *
     * @param id set l'id de la facture
     */
    public void setId(int id){this.id = id;}

    /**
     *
     * @return l'id de la facture
     */
    public int getId(){ return id;}

    /**
     *
     * @param nom set de nom de la facture
     */
    public void setNom(String nom){this.nom = nom;}

    /**
     *
     * @return le nom de la facture
     */
    public String getNom(){ return nom;}

    /**
     *
     * @param description set la description de la facture
     */

    public void setDescription(String description){this.description = description;}

    /**
     *
     * @return la description de la facture
     */
    public String getDescription(){ return description;}

    /**
     *
     * @param montant_defini set le montant de la facture
     */

    public void setMontant_defini(double montant_defini){ this.montant_defini = montant_defini;}

    /**
     *
     * @return le montant_defini de la facture
     */
    public double getMontant_defini(){return montant_defini;}

    /**
     *
     * @param jour_du_mois set le jour de paiement de la facture
     */
    public void setJour_du_mois(int jour_du_mois){this.jour_du_mois = jour_du_mois;}

    /**
     *
     * @return le jour de paiement de la facture
     */
    public int getJour_du_mois(){return jour_du_mois;}

    /**
     *
     * @param id_fournisseur set l'id du fournisseur de la facture
     */
    public void setId_fournisseur(int id_fournisseur){this.id_fournisseur = id_fournisseur;}

    /**
     *
     * @return l'id du fournisseur de la facture
     */
    public int getId_fournisseur(){return id_fournisseur;}

    /**
     *
     * @return le montant défini de la facture
     */
    @Override
    public String toString() {
        return String.valueOf(montant_defini);
    }



}
