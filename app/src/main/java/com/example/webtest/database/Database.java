package com.example.webtest.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.core.database.sqlite.SQLiteDatabaseKt;

import java.util.ArrayList;
import java.util.List;

public class Database {
    public static final String DATABASE_NAME = "translated_data";
    public static SQLiteDatabase db;
    public static SQLiteDatabase databaseInit(Context context){
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS lang_pairs (id INTEGER PRIMARY KEY AUTOINCREMENT" +
                ",original TEXT, translate TEXT)");
        return db;
    }
    public static List<LangPair> getAllLangPairs(){
        if(db == null) return null;
        Cursor query = db.rawQuery("SELECT * FROM lang_pairs", null);
        List<LangPair> langPairs = new ArrayList<>();
        int idIndex = query.getColumnIndex("id");
        while (query.moveToNext()){
            int id = Integer.valueOf(query.getString(0));
            String original = query.getString(1);
            String translate = query.getString(2);
            LangPair langPair = new LangPair(original, translate);
            langPair.setId(id);
            langPairs.add(langPair);
        }
        query.close();
        return langPairs;
    }
    public static void insertLangPair(LangPair langPair){
        if(db == null) return;
        String query = "INSERT INTO lang_pairs (original, translate) VALUES (?, ?)";
        db.execSQL(query, new String[] {langPair.original, langPair.translate});
    }
    public static void removeLangPair(LangPair langPair){
        if(db == null) return;
        String query = "DELETE FROM lang_pairs WHERE id = ?";
        db.execSQL(query, new String[] {String.valueOf(langPair.id)});
    }
}
