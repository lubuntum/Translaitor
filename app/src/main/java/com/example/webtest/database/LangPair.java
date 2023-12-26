package com.example.webtest.database;

public class LangPair {
    public int id;
    public String original;
    public String translate;

    public LangPair(String original, String translate){
        this.original = original;
        this.translate = translate;
    }
    public void setId(int id){
        this.id = id;
    }
}
