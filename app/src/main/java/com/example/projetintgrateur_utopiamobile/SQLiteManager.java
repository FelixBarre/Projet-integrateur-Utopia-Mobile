package com.example.projetintgrateur_utopiamobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteManager extends SQLiteOpenHelper {
    private static SQLiteManager sqLiteManager;
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
    private static final String ID_ETAT_DEMANDE_FIELD = "id_etat_demande";
    private static final String ID_TYPE_DEMANDE_FIELD = "id_type_demande";
    private static final String NOM_FIELD = "nom";
    private static final String SOLDE_FIELD = "solde";
    private static final String TAUX_INTERET_FIELD = "taux_interet";
    private static final String EST_VALIDE_FIELD = "est_valide";
    private static final String FERME_FIELD = "ferme";
    private static final String LIMITE_FIELD = "limite";
    private static final String DATE_DEMANDE_FIELD = "date_demande";
    private static final String DATE_TRAITEMENT_FIELD = "date_traitement";
    private static final String RAISON_FIELD = "raison";
    private static final String MONTANT_FIELD = "montant";

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context) {
        if (sqLiteManager == null) {
            sqLiteManager = new SQLiteManager(context);
        }

        return sqLiteManager;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(COMPTE_BANCAIRES_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
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
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(FERME_FIELD)
                .append(" TINYINT)");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(CREDITS_TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
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
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
