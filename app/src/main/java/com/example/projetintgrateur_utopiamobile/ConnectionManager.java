/****************************************
 Fichier : ConnectionManager.java
 @author : Max Belval-Michaud
 Fonctionnalité : N/A
 Date de création: 2024-05-22
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Classe pour la gestion de la connexion à internet. Vérifie si la connexion est activée
 */
public class ConnectionManager {
    private ConnectivityManager connectivityManager;

    /**
     *
     * @param context Le contexte / l'activté dans lequel on initialise le ConnectionManager
     * Constructeur du ConnectionManager
     */
    public ConnectionManager(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
    }

    /**
     *
     * @return Vrai ou faux selon l'état de la connexion
     * Vérifie si la connexion de l'utilisateur est ouverte
     */
    public boolean isConnected() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
