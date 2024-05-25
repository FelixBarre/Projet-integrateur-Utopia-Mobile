/****************************************
 Fichier : CompteBancaireManager
 @author : Mathis Leduc
 Fonctionnalité : M-CTE-8 consulter les comptes bancaires
 Date : 2024-05-23
 ****************************************/

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
                    VerifyComptes();
                    HttpClient httpClient = HttpClient.instanceOfClient();
                    //GET de tous les types de comptes
                    String responseGET = httpClient.get("comptesBancaires");
                    JSONObject Json = new JSONObject(responseGET);
                    JSONArray arrayJson = Json.getJSONArray("data");
                    //GET des prêts
                    String responsePret = httpClient.get("prets");
                    JSONObject JsonPret = new JSONObject(responsePret);
                    JSONArray arrayPret = JsonPret.getJSONArray("data");
                    //Get des crédits
                    String responseCredit = httpClient.get("credits");
                    JSONObject JsonCredit = new JSONObject(responseCredit);
                    JSONArray arrayCredit = JsonCredit.getJSONArray("data");

                    //place tous les id des comptes bancaires prêts dans une liste
                    for (int i = 0; i < arrayPret.length(); i++) {
                        JSONObject objPretJson = arrayPret.getJSONObject(i);
                        listPret.add(objPretJson.getInt("id_compte"));
                    }

                    //place tous les id des comptes bancaires crédits dans une liste
                    for (int i = 0; i < arrayCredit.length(); i++) {
                        JSONObject objCreditJson = arrayCredit.getJSONObject(i);
                        listCredit.add(objCreditJson.getInt("id_compte"));
                    }


                    for (int i = 0; i < arrayJson.length(); i++) {
                        isIn = false;
                        estPret = false;
                        estCredit = false;
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
                        //si un compte est déjà dans l'array comptes l'update
                        for (int j = 0; j < comptes.size(); j++) {
                            if (comptes.get(j).equals(compte)) {
                                isIn = true;
                                comptes.set(j, compte);
                            }
                        }
                        //si un compte est déjà dans l'array prêts l'update
                        for (int j = 0; j < prets.size(); j++) {
                            if (prets.get(j).equals(compte)) {
                                isIn = true;
                                prets.set(j, compte);
                            }
                        }
                        //si un compte est déjà dans l'array credits l'update
                        for (int j = 0; j < credits.size(); j++) {
                            if (credits.get(j).equals(compte)) {
                                isIn = true;
                                credits.set(j, compte);
                            }
                        }
                        //si le compte est aucun des array l'ajoute à son array correspondant
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

    public void VerifyComptes() {
        if (!comptes.isEmpty()) {
            for (int i = comptes.size() - 1; i >= 0 ; i--) {
                if (!comptes.get(i).isEst_valide()) {
                    comptes.remove(i);
                }
            }
        }
        if (!prets.isEmpty()) {
            for (int i = prets.size() - 1; i >= 0 ; i--) {
                if (!prets.get(i).isEst_valide()) {
                    prets.remove(i);
                }
            }
        }
        if (!credits.isEmpty()) {
            for (int i = credits.size() - 1; i >= 0 ; i--) {
                if (!credits.get(i).isEst_valide()) {
                    credits.remove(i);
                }
            }
        }
    }
}
