package com.example.projetintgrateur_utopiamobile;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    private static HttpClient httpClient;
    private final String apiUrl = "http://10.0.2.2:8000/api/";
    private static String tokenApi = "";
    private HttpURLConnection connection;
    private final String ROUTETOKEN = "token";
    private enum Methods {
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

    public static HttpClient instanceOfClient() {
        if (httpClient == null) {
            httpClient = new HttpClient();
        }

        return httpClient;
    }

    public void setTokenApi(String token) {
        tokenApi = token;
    }

    private void openConnection(String route, Methods method) {
        try {
            URL url = new URL(apiUrl + route);
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setRequestMethod(method.toString());
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json; charset=utf-8");

            if (method != Methods.GET) {
                connection.setDoOutput(true);
            }

            connection.setDoInput(true);

            if (route != ROUTETOKEN) {
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

    private String getResponse() throws IOException {
        InputStream inputStream;
        if (connection.getResponseCode() >= 400) {
            inputStream = connection.getErrorStream();
        }
        else {
            inputStream = connection.getInputStream();
        }
        BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
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
            this.openConnection(ROUTETOKEN, Methods.POST);

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

    public String get(String route) throws IOException {
        if (!this.validateToken()) {
            return "";
        }

        this.openConnection(route, Methods.GET);

        String response = this.getResponse();

        this.closeConnection();

        return response;
    }

    public String get(String route, String body) throws IOException {
        if (!this.validateToken()) {
            return "";
        }

        this.openConnection(route, Methods.GET);
        this.ajouterBodyJSON(body);

        String response = this.getResponse();

        this.closeConnection();

        return response;
    }


    public String post(String route, String body) throws IOException {
        if (route != ROUTETOKEN) {
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
