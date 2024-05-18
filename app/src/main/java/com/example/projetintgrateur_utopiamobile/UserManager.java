package com.example.projetintgrateur_utopiamobile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class UserManager {
    private static User authUser;
    public boolean checkUserIsUser(String email) {
        try {
            HttpClient httpClient = HttpClient.instanceOfClient();
            String responseGET = httpClient.get("profilesApi/" + email);

            JSONObject response = new JSONObject(responseGET);
            if (response.toString().contains("Utilisateur")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

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

    public static void setAuthUser(User user) {
        authUser = user;
    }

    public static User getAuthUser() {
        return authUser;
    }
}
