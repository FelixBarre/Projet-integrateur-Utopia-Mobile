/****************************************
 Fichier : RequestCodes.java
 @author Félix Barré
 Fonctionnalité : Classe pour contenir tous les RequestCodes qu'on utilise à travers l'application
 Date : 13 mai 2024
 Vérification :

 =========================================================
 Historique de modifications :

 =========================================================
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

/**
 * Classe contenant les RequestCodes pour s'assurer que les requêtes reçues correspondent
 * avec ce qu'on a envoyé et pour gérer si il y a plusieurs requêtes dans une activité
 */
public class RequestCodes {
    public static final int CONVERSATIONS_REQUEST_CODE = 101;
    public static final int MAIN_ACTIVITY_REQUEST_CODE = 102;
    public static final int CAMERA_PERM_CODE = 103;
    public static final int CAMERA_REQUEST_CODE = 104;
    public static final int CHANGEMENT_MDP_REQUEST_CODE = 105;
    public static final int VILLES_REQUEST_CODE = 106;
    public static final int MODIFIER_PROFILE_REQUEST_CODE = 107;
    public static final int DESACTIVATION_COMPTE_REQUEST_CODE = 108;
    public static final int DESACTIVATION_PROFILE_REQUEST_CODE = 109;
    public static final int NOTIFICATION_PERM_CODE = 110;
}
