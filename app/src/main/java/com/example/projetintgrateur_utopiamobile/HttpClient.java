/****************************************
 Fichier : HttpClient.java
 @author Félix Barré
 Fonctionnalité : Classe qui gère les requêtes Http vers notre api web
 Date : 13 mai 2024
 Vérification :

 =========================================================
 Historique de modifications :

 =========================================================
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.graphics.Bitmap;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/**
 * Classe pour la gestion d'envoi de requêtes HTTP vers notre API
 */
public class HttpClient {
    private static HttpClient httpClient;
    public static final String urlSite = "http://10.0.2.2:8000";
    private final String apiUrl = urlSite + "/api/";
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

    /**
     *
     * @return L'instance unique statique de HttpClient
     *
     * Permet d'obtenir l'insance unique de httpClient
     */
    public static HttpClient instanceOfClient() {
        if (httpClient == null) {
            httpClient = new HttpClient();
        }

        return httpClient;
    }

    /**
     *
     * @param token Le token obtenu
     *
     * Set le token obtenu à la connexion
     */
    public void setTokenApi(String token) {
        tokenApi = token;
    }

    /**
     *
     * @param route La route à ouvrir
     * @param method La méthode à utilser
     * @throws IOException Exception lors de l'ouverture de la connexion
     *
     * Ouvre la connexion Http à l'api
     */
    private void openConnection(String route, Methods method) throws IOException {
        URL url = new URL(apiUrl + route);
        connection = (HttpURLConnection) url.openConnection();
        connection.setUseCaches(false);
        connection.setRequestMethod(method.toString());
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setRequestProperty("Accept", "application/json; charset=utf-8");

        if (!method.equals(Methods.GET)) {
            connection.setDoOutput(true);
        }

        connection.setDoInput(true);

        if (!route.equals(ROUTETOKEN)) {
            connection.setRequestProperty("Authorization", "Bearer " + tokenApi);
        }
    }

    /**
     * Ferme la connexion Http
     */
    private void closeConnection() {
        connection.disconnect();
        connection = null;
    }

    /**
     *
     * @param bodyJSON String qui contient les paramètres (body) en JSON
     * @throws IOException Exception lors de l'ajout du body au stream
     *
     * Ajoute les paramètres JSON (body) à la requête
     */
    private void ajouterBodyJSON(String bodyJSON) throws IOException {
        OutputStream os = connection.getOutputStream();
        byte[] input = bodyJSON.getBytes("utf-8");
        os.write(input, 0, input.length);
    }

    /**
     *
     * @param body String qui contient les paramètres (body) en JSON
     * @param image L'image bitmap capturée avec la caméra
     * @throws Exception
     *
     * Ajoute les paramètres JSON (body) à la requête en plus de l'image capturée avec la caméra
     */
    private void ajouterBodyWithImage(String body, Bitmap image) throws Exception {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";

        DataOutputStream request = new DataOutputStream(connection.getOutputStream());

        JSONObject bodyJSON = new JSONObject(body);

        Iterator<String> keys = bodyJSON.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            if (bodyJSON.has(key)) {
                String value = bodyJSON.getString(key);

                request.writeBytes(twoHyphens + boundary + lineEnd);
                request.writeBytes("Content-Disposition: form-data; name=\"" + key + "\""+lineEnd);
                request.writeBytes(lineEnd);
                request.writeBytes(value + lineEnd);
            }
        }

        String fileName = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA_FRENCH).format(new Date());

        request.writeBytes(twoHyphens + boundary + lineEnd);
        request.writeBytes("Content-Disposition: form-data; name=\"pieceJointe\";filename=\"" + fileName + ".jpeg\"" + lineEnd);
        request.writeBytes("Content-Type: image/jpeg" + lineEnd);
        request.writeBytes(lineEnd);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        request.write(stream.toByteArray());

        request.writeBytes(lineEnd);
        request.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

        request.flush();
        request.close();
    }

    /**
     *
     * @return La réponse de la requête
     * @throws IOException Exception levée lors de la lecture de la réponse
     *
     * Permet d'obtenir la réponse de la requête Http
     */
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

    /**
     *
     * @return Vrai si on a le token, sinon faux
     *
     * Valide si le token d'authentification est présent
     */
    private boolean validateToken() {
        if (tokenApi.isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     *
     * @param route La route de la requête get
     * @return La réponse de la requête get
     * @throws IOException Exception lors de la requête get
     *
     * Exécute une requête get
     */
    public String get(String route) throws IOException {
        if (!this.validateToken()) {
            return "";
        }

        this.openConnection(route, Methods.GET);

        String response = this.getResponse();

        this.closeConnection();

        return response;
    }

    /**
     *
     * @param route La route de la requête get
     * @param body Le body de la requête get
     * @return La réponse de la requête get
     * @throws IOException Exception lors de la requête get
     *
     * Exécute une requête get avec body
     */
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

    /**
     *
     * @param route La route de la requête post
     * @param body Le body de la requête post
     * @return La réponse de la requête post
     * @throws IOException Exception lors de la requête post
     *
     * Exécute une requête post
     */
    public String post(String route, String body) throws IOException {
        if (!route.equals(ROUTETOKEN)) {
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

    /**
     *
     * @param body Le body de la requête post
     * @param image L'image Bitmap à inclure avec le message
     * @return La réponse de la requête post
     * @throws Exception Exception lors de la requête post
     *
     * Exécute une requête post qui envoie un message avec image
     */
    public String postMessageWithImage(String body, Bitmap image) throws Exception {
        if (!this.validateToken()) {
            return "";
        }

        this.openConnection("messages", Methods.POST);

        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");

        this.ajouterBodyWithImage(body, image);

        String response = this.getResponse();

        this.closeConnection();

        return response;
    }

    /**
     *
     * @param route La route de la requête put
     * @param body Le body de la requête put
     * @return La réponse de la requête put
     * @throws IOException Exception lors de la requête put
     *
     * Exécute une requête put
     */
    public String put(String route, String body) throws IOException {
        if (!this.validateToken()) {
            return "";
        }

        this.openConnection(route, Methods.PUT);
        this.ajouterBodyJSON(body);

        String response = this.getResponse();

        this.closeConnection();

        return response;
    }

    /**
     *
     * @param route La route de la requête delete
     * @return La réponse de la requête delete
     * @throws IOException Exception lors de la requête delete
     *
     * Exécute une requête delete
     */
    public String delete(String route) throws IOException {
        if (!this.validateToken()) {
            return "";
        }

        this.openConnection(route, Methods.DELETE);

        String response = this.getResponse();

        this.closeConnection();

        return response;
    }
}