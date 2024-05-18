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
    public static void setAuthUser(User user) {
        authUser = user;
    }
    public User getAuthUser() {
        return authUser;
    }
}
