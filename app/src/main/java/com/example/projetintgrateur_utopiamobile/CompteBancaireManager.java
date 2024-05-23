/*
 * Auteur(s): Mathis Leduc
 */
package com.example.projetintgrateur_utopiamobile;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class CompteBancaireManager {
    public static ArrayList<CompteBancaire> comptes = new ArrayList<>();
    public static ArrayList<CompteBancaire> prets = new ArrayList<>();
    public static ArrayList<CompteBancaire> credits = new ArrayList<>();
    public ArrayList<Integer> listPret = new ArrayList<>();
    public ArrayList<Integer> listCredit = new ArrayList<>();
    public boolean isIn = false;
    public boolean estPret = false;
    public boolean estCredit = false;


    public void initComptes(Context context) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = HttpClient.instanceOfClient();
                    String responseGET = httpClient.get("comptesBancaires");
                    JSONObject Json = new JSONObject(responseGET);
                    JSONArray arrayJson = Json.getJSONArray("data");
                    String responsePret = httpClient.get("prets");
                    JSONObject JsonPret = new JSONObject(responsePret);
                    JSONArray arrayPret = JsonPret.getJSONArray("data");
                    String responseCredit = httpClient.get("prets");
                    JSONObject JsonCredit = new JSONObject(responseCredit);
                    JSONArray arrayCredit = JsonCredit.getJSONArray("data");

                    for (int i = 0; i < arrayPret.length(); i++) {
                        JSONObject objPretJson = arrayPret.getJSONObject(i);
                        listPret.add(objPretJson.getInt("id_compte"));
                    }

                    for (int i = 0; i < arrayCredit.length(); i++) {
                        JSONObject objPretJson = arrayPret.getJSONObject(i);
                        listCredit.add(objPretJson.getInt("id_compte"));
                    }

                    for (int i = 0; i < arrayJson.length(); i++) {
                        isIn = false;
                        estPret = false;
                        JSONObject objJson = arrayJson.getJSONObject(i);
                        CompteBancaire compte = new CompteBancaire();
                        compte.setId_compte(objJson.getInt("id"));
                        compte.setNom(objJson.getString("nom"));
                        compte.setSolde(objJson.getDouble("solde"));
                        compte.setTaux_interet(objJson.getDouble("taux_interet"));
                        compte.setId_user(objJson.getInt("id_user"));
                        if (objJson.getInt("est_valide") == 1) {
                            compte.setEst_valide(true);
                        } else {
                            compte.setEst_valide(false);
                        }
                        for (int j = 0; j < comptes.size(); j++) {
                            if (comptes.get(j).equals(compte)) {
                                isIn = true;
                                comptes.set(j, compte);
                            }
                        }
                        for (int j = 0; j < prets.size(); j++) {
                            if (prets.get(j).equals(compte)) {
                                isIn = true;
                                prets.set(j, compte);
                            }
                        }
                        for (int j = 0; j < credits.size(); j++) {
                            if (credits.get(j).equals(compte)) {
                                isIn = true;
                                credits.set(j, compte);
                            }
                        }
                        if (isIn == false) {
                            sqLiteManager.addComptetoDB(compte);
                            for (int p = 0; p < listPret.size(); p++) {
                                if (compte.getId_compte() == listPret.get(p)) {
                                    estPret = true;
                                }
                            }
                            for (int p = 0; p < listCredit.size(); p++) {
                                if (compte.getId_compte() == listCredit.get(p)) {
                                    estCredit = true;
                                }
                            }
                            if (estPret == true) {
                                prets.add(compte);
                            } else if (estCredit == true){
                                credits.add(compte);
                            } else {
                                comptes.add(compte);
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
