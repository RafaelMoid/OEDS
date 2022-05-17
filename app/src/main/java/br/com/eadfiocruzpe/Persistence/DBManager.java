package br.com.eadfiocruzpe.Persistence;

public class DBManager {

    private static DB sDb;

    public static void setDBManager(DB db) {
        sDb = db;
    }

    public static DB getDB() {
        return sDb;
    }

}