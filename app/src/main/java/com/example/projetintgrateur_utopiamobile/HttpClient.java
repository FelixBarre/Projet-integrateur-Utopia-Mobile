package com.example.projetintgrateur_utopiamobile;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    private static HttpClient httpClient;
    private final String apiUrl = "https://odd-friends-stick.loca.lt/api/";
    private static String tokenApi = "";
    private HttpURLConnection connection;
    public enum Methods {
        GET ("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");

        private final String method;

        private Methods(String m) {
            method = m;
        }

        @Override
        public String toString() {
            return this.method;
        }
    }
    public enum Routes {
        TOKEN ("token"),
        CONVERSATIONS("conversations"),
        USER("user");

        private final String route;

        private Routes(String r) {
            route = r;
        }

        @Override
        public String toString() {
            return this.route;
        }
    }

    public static HttpClient instanceOfClient() {
        if (httpClient == null) {
            httpClient = new HttpClient();
        }

        return httpClient;
    }

    private void openConnection(Routes route, Methods method) {
        try {
            URL url = new URL(apiUrl + route.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method.toString());
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            if (route != Routes.TOKEN) {
                connection.setRequestProperty("Authorization", "Bearer " + tokenApi);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void closeConnection() {
        connection.disconnect();
    }
    private void ajouterBodyJSON(String bodyJSON) {
        try {
            OutputStream os = connection.getOutputStream();
            byte[] input = bodyJSON.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int getResponseCode() throws IOException {
        return connection.getResponseCode();
    }
    private String getResponse() throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = input.readLine()) != null) {
            response.append(inputLine);
        }
        input.close();

        return response.toString();
    }

    private boolean validateToken() {
        if (tokenApi.isEmpty()) {
            this.openConnection(Routes.TOKEN, Methods.POST);

            // TODO À changer pour les infos du User
            this.ajouterBodyJSON("{ \"email\": \"test3@user.com\", \"password\": \"test3@user.com\", \"token_name\": \"tokenAPI\" }");

            try {
                JSONObject tokenResponseJSON = new JSONObject(getResponse());
                tokenApi = tokenResponseJSON.getString("SUCCÈS");
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            finally {
                this.closeConnection();
            }

            return !tokenApi.isEmpty();
        }

        return true;
    }
    public String get(Routes route) throws IOException {
        if (!this.validateToken()) {
            return "";
        }

        this.openConnection(route, Methods.GET);

        String response = this.getResponse();

        this.closeConnection();

        return response;
    }
    public String post(Routes route, String body) throws IOException {
        if (route != Routes.TOKEN) {
            if (!this.validateToken()) {
                return "";
            }
        }

        this.openConnection(route, Methods.POST);
        this.ajouterBodyJSON(body);

        String response = this.getResponse();

        this.closeConnection();

        return response;
    }
}
