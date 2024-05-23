/*
 * Auteur(s):
 */
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

    public static HttpClient instanceOfClient() {
        if (httpClient == null) {
            httpClient = new HttpClient();
        }

        return httpClient;
    }

    public void setTokenApi(String token) {
        tokenApi = token;
    }

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

    private void closeConnection() {
        connection.disconnect();
        connection = null;
    }

    private void ajouterBodyJSON(String bodyJSON) throws IOException {
        OutputStream os = connection.getOutputStream();
        byte[] input = bodyJSON.getBytes("utf-8");
        os.write(input, 0, input.length);
    }

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
            return false;
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