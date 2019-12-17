package com.example.moody.persistence;

import android.database.sqlite.SQLiteDatabase;

public class Database {
    private static final Database ourInstance = new Database();

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    private SQLiteDatabase database;
    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
    }
}
