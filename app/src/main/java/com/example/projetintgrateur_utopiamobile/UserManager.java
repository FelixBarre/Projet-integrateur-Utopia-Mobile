/****************************************
 Fichier : UserManager.java
 @author : Max Belval-Michaud
 Fonctionnalité : M-CTE-1 -> M-CTE-6 Profil utilisateur
 Date de création: 2024-05-15
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Classe qui gère l'objet utilisateur
 */
public class UserManager {
    private static User authUser;

    /**
     *
     * @param email Le courriel de l'utilisateur
     * @return Un objet utilisateur
     *
     * Fonction qui retourne un objet utilisateur selon son Email avec une requête à l'API
     */
    public User getUser(String email) {
        try {
            HttpClient httpClient = HttpClient.instanceOfClient();
            String responseGET = httpClient.get("profilesApi/" + email);
            JSONObject response = new JSONObject(responseGET);

            if (response.has("data")) {
                JSONArray data = new JSONArray(response.get("data").toString());
                JSONObject dataSpecific = new JSONObject(data.get(0).toString());

                int id = (int) dataSpecific.get("id");
                String nom = dataSpecific.get("nom").toString();
                String prenom = dataSpecific.get("prenom").toString();
                String telephone = dataSpecific.get("telephone").toString();
                String noCivique = dataSpecific.get("no_civique").toString();
                String noPorte = dataSpecific.get("no_porte").toString();
                String rue = dataSpecific.get("rue").toString();
                int idVille = (int) dataSpecific.get("id_ville");
                String codePostal = dataSpecific.get("code_postal").toString();

                User newUser = new User(id, nom, prenom, telephone, noCivique, noPorte, rue, idVille, codePostal, email);

                return newUser;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param user L'utilisateur authentifié
     *
     * Fonction qui initialise l'utilisateur authentifié
     */
    public static void setAuthUser(User user) {
        authUser = user;
    }

    /**
     *
     * @return L'utilisateur authentifié
     *
     * Fonction qui retourne l'utilisateur authentifié
     */
    public static User getAuthUser() {
        return authUser;
    }
}
