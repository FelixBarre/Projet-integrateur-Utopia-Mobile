/****************************************
 Fichier : SQLiteManager.java
 @author Félix Barré, Mathis Leduc
 Fonctionnalité : Gestionnaire pour la base de données locale
 Date : 13 mai 2024
 Vérification :

 =========================================================
 Historique de modifications :

 =========================================================
 ****************************************/
package com.example.projetintgrateur_utopiamobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour faire la relation avec la BD locale
 */
public class SQLiteManager extends SQLiteOpenHelper {
    private static SQLiteManager sqLiteManager;
    private static int numberOfVilles;
    private static final String DATABASE_NAME = "BanqueUtopia";
    private static final int DATABASE_VERSION = 1;
    private static final String COMPTE_BANCAIRES_TABLE_NAME = "compte_bancaires";
    private static final String CONVERSATIONS_TABLE_NAME = "conversations";
    private static final String CREDITS_TABLE_NAME = "credits";
    private static final String DEMANDES_TABLE_NAME = "demandes";
    private static final String ETAT_DEMANDES_TABLE_NAME = "etat_demandes";
    private static final String ETAT_TRANSACTIONS_TABLE_NAME = "etat_transactions";
    private static final String FACTURES_TABLE_NAME = "factures";
    private static final String FOURNISSEURS_TABLE_NAME = "fournisseurs";
    private static final String MESSAGES_TABLE_NAME = "messages";
    private static final String MESSAGES_LOCAUX_TABLE_NAME = "messages_locaux";
    private static final String PRETS_TABLE_NAME = "prets";
    private static final String TRANSACTIONS_TABLE_NAME = "transactions";
    private static final String TYPE_DEMANDES_TABLE_NAME = "type_demandes";
    private static final String TYPE_TRANSACTIONS_TABLE_NAME = "type_transactions";
    private static final String USERS_TABLE_NAME = "users";
    private static final String VILLES_TABLE_NAME = "villes";
    private static final String ID_FIELD = "id";
    private static final String ID_USER_FIELD = "id_user";
    private static final String ID_COMPTE_FIELD = "id_compte";
    private static final String ID_DEMANDEUR_FIELD = "id_demandeur";
    private static final String ID_FOURNISSEUR_FIELD = "id_fournisseur";
    private static final String ID_ETAT_DEMANDE_FIELD = "id_etat_demande";
    private static final String ID_TYPE_DEMANDE_FIELD = "id_type_demande";
    private static final String ID_ETAT_TRANSACTION_FIELD = "id_etat_transaction";
    private static final String ID_TYPE_TRANSACTION_FIELD = "id_type_transaction";
    private static final String ID_CONVERSATION_FIELD = "id_conversation";
    private static final String ID_ENVOYEUR_FIELD = "id_envoyeur";
    private static final String ID_RECEVEUR_FIELD = "id_receveur";
    private static final String ID_COMPTE_ENVOYEUR_FIELD = "id_compte_envoyeur";
    private static final String ID_COMPTE_RECEVEUR_FIELD = "id_compte_receveur";
    private static final String ID_VILLE_FIELD = "id_ville";
    private static final String NOM_FIELD = "nom";
    private static final String PRENOM_FIELD = "prenom";
    private static final String TELEPHONE_FIELD = "telephone";
    private static final String NO_CIVIQUE_FIELD = "no_civique";
    private static final String NO_PORTE_FIELD = "no_porte";
    private static final String RUE_FIELD = "rue";
    private static final String CODE_POSTAL_FIELD = "code_postal";
    private static final String EMAIL_FIELD = "email";
    private static final String EMAIL_VERIFIED_AT_FIELD = "email_verified_at";
    private static final String PASSWORD_FIELD = "password";
    private static final String REMEMBER_TOKEN_FIELD = "remember_token";
    private static final String SOLDE_FIELD = "solde";
    private static final String TAUX_INTERET_FIELD = "taux_interet";
    private static final String EST_VALIDE_FIELD = "est_valide";
    private static final String FERME_FIELD = "ferme";
    private static final String LIMITE_FIELD = "limite";
    private static final String DATE_DEMANDE_FIELD = "date_demande";
    private static final String DATE_TRAITEMENT_FIELD = "date_traitement";
    private static final String RAISON_FIELD = "raison";
    private static final String MONTANT_FIELD = "montant";
    private static final String LABEL_FIELD = "label";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String MONTANT_DEFINI_FIELD = "montant_defini";
    private static final String JOUR_DU_MOIS_FIELD = "jour_du_mois";
    private static final String CREATED_AT_FIELD = "created_at";
    private static final String UPDATED_AT_FIELD = "updated_at";
    private static final String TEXTE_FIELD = "texte";
    private static final String IMAGE_FIELD = "image";
    private static final String CHEMIN_DU_FICHIER_FIELD = "chemin_du_fichier";
    private static final String DATE_HEURE_SUPPRIME_FIELD = "date_heure_supprime";
    private static final String DATE_DEBUT_FIELD = "date_debut";
    private static final String DATE_ECHEANCE_FIELD = "date_echeance";

    /**
     *
     * @param context Le contexte d'où le manager est initialisé
     *
     * Constructeur pour l'objet SQLiteManager
     */
    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     *
     * @param context Le contexte d'où le manager est initialisé
     * @return L'instance unique statique de SQLiteManager
     *
     * Permet d'obtenir l'insance unique de SQLiteManager
     */
    public static SQLiteManager instanceOfDatabase(Context context) {
        if (sqLiteManager == null) {
            sqLiteManager = new SQLiteManager(context);
        }

        return sqLiteManager;
    }

    /**
     *
     * @param db La base de données.
     *
     * Création de la base de données (tables)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(COMPTE_BANCAIRES_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY, ")
                .append(EST_VALIDE_FIELD)
                .append(" TINYINT, ")
                .append(ID_USER_FIELD)
                .append(" INTEGER, ")
                .append(NOM_FIELD)
                .append(" TEXT, ")
                .append(SOLDE_FIELD)
                .append(" DECIMAL, ")
                .append(TAUX_INTERET_FIELD)
                .append(" DECIMAL)");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(CONVERSATIONS_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY, ")
                .append(FERME_FIELD)
                .append(" TINYINT)");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(CREDITS_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY, ")
                .append(ID_COMPTE_FIELD)
                .append(" INTEGER, ")
                .append(NOM_FIELD)
                .append(" TEXT, ")
                .append(LIMITE_FIELD)
                .append(" DECIMAL, ")
                .append(EST_VALIDE_FIELD)
                .append(" TINYINT)");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(DEMANDES_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY, ")
                .append(ID_DEMANDEUR_FIELD)
                .append(" INTEGER, ")
                .append(ID_ETAT_DEMANDE_FIELD)
                .append(" INTEGER, ")
                .append(ID_TYPE_DEMANDE_FIELD)
                .append(" INTEGER, ")
                .append(DATE_DEMANDE_FIELD)
                .append(" DATE, ")
                .append(DATE_TRAITEMENT_FIELD)
                .append(" DATE, ")
                .append(RAISON_FIELD)
                .append(" TEXT, ")
                .append(MONTANT_FIELD)
                .append(" DECIMAL)");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(ETAT_DEMANDES_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY, ")
                .append(LABEL_FIELD)
                .append(" TEXT)");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(ETAT_TRANSACTIONS_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY, ")
                .append(LABEL_FIELD)
                .append(" TEXT)");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(FACTURES_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY, ")
                .append(ID_FOURNISSEUR_FIELD)
                .append(" INTEGER, ")
                .append(NOM_FIELD)
                .append(" TEXT, ")
                .append(DESCRIPTION_FIELD)
                .append(" TEXT, ")
                .append(MONTANT_DEFINI_FIELD)
                .append(" DECIMAL, ")
                .append(JOUR_DU_MOIS_FIELD)
                .append(" INTEGER)");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(FOURNISSEURS_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY, ")
                .append(NOM_FIELD)
                .append(" TEXT, ")
                .append(DESCRIPTION_FIELD)
                .append(" TEXT)");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(MESSAGES_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY, ")
                .append(ID_CONVERSATION_FIELD)
                .append(" INTEGER, ")
                .append(ID_ENVOYEUR_FIELD)
                .append(" INTEGER, ")
                .append(ID_RECEVEUR_FIELD)
                .append(" INTEGER, ")
                .append(CREATED_AT_FIELD)
                .append(" DATETIME, ")
                .append(UPDATED_AT_FIELD)
                .append(" DATETIME, ")
                .append(TEXTE_FIELD)
                .append(" TEXT, ")
                .append(CHEMIN_DU_FICHIER_FIELD)
                .append(" TEXT, ")
                .append(DATE_HEURE_SUPPRIME_FIELD)
                .append(" DATETIME)");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(MESSAGES_LOCAUX_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_CONVERSATION_FIELD)
                .append(" INTEGER, ")
                .append(IMAGE_FIELD)
                .append(" BLOB, ")
                .append(TEXTE_FIELD)
                .append(" TEXT)");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(PRETS_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY, ")
                .append(ID_COMPTE_FIELD)
                .append(" INTEGER, ")
                .append(DATE_DEBUT_FIELD)
                .append(" DATETIME, ")
                .append(DATE_ECHEANCE_FIELD)
                .append(" DATETIME, ")
                .append(EST_VALIDE_FIELD)
                .append(" TINYINT, ")
                .append(MONTANT_FIELD)
                .append(" DECIMAL, ")
                .append(NOM_FIELD)
                .append(" TEXT)");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TRANSACTIONS_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY, ")
                .append(ID_TYPE_TRANSACTION_FIELD)
                .append(" INTEGER, ")
                .append(ID_ETAT_TRANSACTION_FIELD)
                .append(" INTEGER, ")
                .append(ID_COMPTE_ENVOYEUR_FIELD)
                .append(" INTEGER, ")
                .append(ID_COMPTE_RECEVEUR_FIELD)
                .append(" INTEGER, ")
                .append(CREATED_AT_FIELD)
                .append(" DATETIME, ")
                .append(UPDATED_AT_FIELD)
                .append(" DATETIME, ")
                .append(MONTANT_FIELD)
                .append(" DECIMAL)");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TYPE_DEMANDES_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY, ")
                .append(LABEL_FIELD)
                .append(" TEXT)");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TYPE_TRANSACTIONS_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY, ")
                .append(LABEL_FIELD)
                .append(" TEXT)");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(USERS_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY, ")
                .append(ID_VILLE_FIELD)
                .append(" INTEGER, ")
                .append(NOM_FIELD)
                .append(" TEXT, ")
                .append(PRENOM_FIELD)
                .append(" TEXT, ")
                .append(TELEPHONE_FIELD)
                .append(" TEXT, ")
                .append(NO_CIVIQUE_FIELD)
                .append(" INTEGER, ")
                .append(NO_PORTE_FIELD)
                .append(" INTEGER, ")
                .append(RUE_FIELD)
                .append(" TEXT, ")
                .append(CODE_POSTAL_FIELD)
                .append(" TEXT, ")
                .append(EMAIL_FIELD)
                .append(" TEXT, ")
                .append(EMAIL_VERIFIED_AT_FIELD)
                .append(" DATETIME, ")
                .append(PASSWORD_FIELD)
                .append(" TEXT, ")
                .append(REMEMBER_TOKEN_FIELD)
                .append(" TEXT, ")
                .append(CREATED_AT_FIELD)
                .append(" DATETIME, ")
                .append(UPDATED_AT_FIELD)
                .append(" DATETIME, ")
                .append(EST_VALIDE_FIELD)
                .append(" TINYINT)");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(VILLES_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY, ")
                .append(NOM_FIELD)
                .append(" TEXT)");

        db.execSQL(sql.toString());
    }

    /**
     *
     * @param db La base de données.
     * @param oldVersion L'ancienne version de la base de données.
     * @param newVersion La nouvelle version de la base de données.
     *
     * Permet de faire les ajustements lorsqu'on met à jour la base de données
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     *
     * @param user l'utilisateur a entrer dans la BD
     * @return Vrai ou faux selon si l'User est précisé (non null) et si l'insertion s'est faite
     *
     * Fonction qui ajoute l'utilisateur spécifié dans la BD locale
     */
    public boolean loadUserIntoDatabase(User user) {
        if (user != null) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();

            contentValues.put("id", user.getId());
            contentValues.put("nom", user.getNom());
            contentValues.put("prenom", user.getPrenom());
            contentValues.put("telephone", user.getTelephone());
            contentValues.put("no_civique", user.getNoCivique());
            contentValues.put("no_porte", user.getNoPorte());
            contentValues.put("rue", user.getRue());
            contentValues.put("id_ville", user.getIdVille());
            contentValues.put("code_postal", user.getCodePostal());
            contentValues.put("email", user.getEmail());

            sqLiteDatabase.insert(USERS_TABLE_NAME, null, contentValues);

            return true;
        }
        return false;
    }

    /**
     *
     * @param userUpdated L'utilisateur mis à jour
     *
     * Fonction qui modifie l'utilisateur en BD locale
     */
    public void updateUserDB(User userUpdated) {
        if (userUpdated != null) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();

            contentValues.put("nom", userUpdated.getNom());
            contentValues.put("prenom", userUpdated.getPrenom());
            contentValues.put("telephone", userUpdated.getTelephone());
            contentValues.put("no_civique", userUpdated.getNoCivique());
            contentValues.put("no_porte", userUpdated.getNoPorte());
            contentValues.put("rue", userUpdated.getRue());
            contentValues.put("id_ville", userUpdated.getIdVille());
            contentValues.put("code_postal", userUpdated.getCodePostal());
            contentValues.put("email", userUpdated.getEmail());

            sqLiteDatabase.update(USERS_TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(UserManager.getAuthUser().getId())});
        }
    }

    /**
     *
     * @param compte Compte bancaire à ajouter dans la BD
     * @return Vrai ou faux selon si le compte bancaire est précisé (non null) et si l'insertion s'est faite
     *
     * Fonction qui ajoute le compte bancaire spécifié dans la BD locale
     */
    public boolean addComptetoDB(CompteBancaire compte) {
        if (compte != null) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();

            contentValues.put("id", compte.getId_compte());
            contentValues.put("nom", compte.getNom());
            contentValues.put("solde", compte.getSolde());
            contentValues.put("taux_interet", compte.getTaux_interet());
            contentValues.put("id_user", compte.getId_user());

            sqLiteDatabase.insert(COMPTE_BANCAIRES_TABLE_NAME, null, contentValues);

            return true;
        }
        return false;
    }

    /**
     *
     * @param id_compte id du compte à modifier
     * @param nom nouveau nom
     *
     * Fonction pour changer le nom d'un compte dans la BD locale
     */
    public void updateNomCompteDB(int id_compte, String nom) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();

            contentValues.put("nom", nom);

            sqLiteDatabase.update(COMPTE_BANCAIRES_TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(id_compte)});
    }

    /**
     *
     * @param villesId Liste des IDs des villes
     * @param villesNom Liste de nom des villes
     *
     * Fonction qui permet de loader les villes dans la BD locale
     */
    public void loadVillesInDB(ArrayList<String> villesId, ArrayList<String> villesNom) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        StringBuilder sql = new StringBuilder()
                .append("DELETE FROM ")
                .append(VILLES_TABLE_NAME)
                .append(";");

        sqLiteDatabase.execSQL(sql.toString());

        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < villesId.size(); i++) {
            contentValues.put("id", villesId.get(i));
            contentValues.put("nom", villesNom.get(i));
            sqLiteDatabase.insert(VILLES_TABLE_NAME, null, contentValues);
        }
    }

    /**
     *
     * @param number le nombre de villes
     *
     * Fonction qui set le nombre de villes dans le compteur de la classe
     */
    public void setNumberOfVilles (int number) {
        numberOfVilles = number;
    }

    /**
     *
     * @param id id de la ville
     * @return le nom de la ville
     *
     * Fonction qui retourne le nom de la ville désirée selon son id
     */
    public String getVilleById(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + NOM_FIELD + " FROM " + VILLES_TABLE_NAME + " WHERE " + ID_FIELD + " = ?", new String[]{Integer.toString(id)});
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        cursor.close();
        return null;
    }

    /**
     *
     * @return les noms de villes
     *
     * Fonction qui retourne dans un ArrayList les noms des villes dans la BD locale
     */
    public ArrayList<String> getNomsVilles () {
        ArrayList<String> villes = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + NOM_FIELD + " FROM " + VILLES_TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                villes.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return villes;
    }

    /**
     *
     * @return Vrai ou faux selon si les villes sont loader ou non
     *
     * Fonction qui vérifie que le nombre de villes en BD local est le même que le compteur pour déterminer si les villes sont chargées
     */
    public boolean isVilleLoaded() {
        int count = 0;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + ID_FIELD + " FROM " + VILLES_TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return count == numberOfVilles;
    }

    /**
     *
     * @param nom le nom de la ville
     * @return L'ID de la ville
     *
     * Fonction qui retourne l'ID de la ville selon son nom
     */
    public String getIdVille(String nom) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + ID_FIELD + " FROM " + VILLES_TABLE_NAME + " WHERE " + NOM_FIELD + " = ?", new String[]{nom});
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        cursor.close();
        return null;
    }

    /**
     *
     * @param messageLocal Le message à ajouter
     * @return L'id du message dans la base de données
     *
     * Insère un message local dans la base de données
     */
    public long addMessageLocalDB(MessageLocal messageLocal) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_CONVERSATION_FIELD, messageLocal.getIdConversation());
        contentValues.put(TEXTE_FIELD, messageLocal.getTexte());

        if (messageLocal.getImage() != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            messageLocal.getImage().compress(Bitmap.CompressFormat.JPEG, 90, stream);
            contentValues.put(IMAGE_FIELD, stream.toByteArray());
        }

        return sqLiteDatabase.insert(MESSAGES_LOCAUX_TABLE_NAME, null, contentValues);
    }

    /**
     *
     * @param id_message_local L'id du message local
     * @return Le nombre de lignes affectées par la suppression
     *
     * Supprime un message local de la base de données
     */
    public int deleteMessageLocal(long id_message_local) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(MESSAGES_LOCAUX_TABLE_NAME, "id = ?", new String[]{String.valueOf(id_message_local)});
    }

    /**
     * Rempli l'arraylist static des messages locaux à partir de la base de données
     */
    public void populateMessagesLocauxArrayList() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        MessageLocal.messagesLocauxArrayList.clear();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + MESSAGES_LOCAUX_TABLE_NAME, null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    int id = result.getInt(0);

                    int id_conversation = result.getInt(1);

                    byte[] blob = result.getBlob(2);
                    Bitmap image = null;

                    if (blob != null) {
                        image = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                    }

                    String texte = result.getString(3);

                    MessageLocal.messagesLocauxArrayList.add(new MessageLocal(id, id_conversation, image, texte));
                }
            }
        }
    }

    /**
     *
     * @param nomCompte nom du compte bancaire
     * @return l'ID du compte bancaire
     *
     * Fonction qui retourne l'ID du compte bancaire selon son nom
     */
    public String getIdCompteBancaire (String nomCompte) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + ID_FIELD + " FROM " + COMPTE_BANCAIRES_TABLE_NAME + " WHERE " + NOM_FIELD + " = ?", new String[]{nomCompte});
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        cursor.close();
        return null;
    }
}
