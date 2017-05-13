package com.example.pc.telefon_rehberi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PC on 16.04.2016.
 */
public class veritabani extends SQLiteOpenHelper {
    private static final String VERİTABANİ_ADİ="telefonrehberi";
    private static final  int SURUM=1;
    public veritabani(Context c){
    super(c,VERİTABANİ_ADİ,null,SURUM);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE kişiler(ad TEXT,soyad TEXT,tel TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS kişiler");
        onCreate(db);

    }
}
