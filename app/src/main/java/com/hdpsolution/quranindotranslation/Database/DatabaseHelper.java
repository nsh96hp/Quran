package com.hdpsolution.quranindotranslation.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hdpsolution.quranindotranslation.Entity.FavoriteVerse;
import com.hdpsolution.quranindotranslation.Entity.MyLanguage;
import com.hdpsolution.quranindotranslation.QuranRules;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;


    public DatabaseHelper(Context context) {
        super(context, QuranRules.DATABASE_MYDB, null, QuranRules.VERSION_DB);
    }

    public static final String CREATE_TABLE_LANGUAGE = "CREATE TABLE " + QuranRules.TBL_LANGUAGE + " (\n" +
            QuranRules.FLAG + "      INTEGER (1)\n" +
            ");\n";

    public static final String CREATE_TABLE_FAVORITE = "CREATE TABLE " + QuranRules.TBL_FAVORITE + " (\n" +
            QuranRules.F_SURAID + " INTEGER (10),\n" +
            QuranRules.F_VERSEID + "    INTEGER   (10)\n" +
            ");\n";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LANGUAGE);
        db.execSQL(CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void AddFavorite(FavoriteVerse favoriteVerse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QuranRules.F_SURAID, favoriteVerse.getSuraid());
        values.put(QuranRules.F_VERSEID, favoriteVerse.getVerseid());
        db.insert(QuranRules.TBL_FAVORITE, null, values);
        db.close();
    }

    public boolean DeleteFavorite(FavoriteVerse favoriteVerse) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + QuranRules.TBL_FAVORITE + " WHERE " + QuranRules.F_SURAID + "="
                + favoriteVerse.getSuraid() + " AND " + QuranRules.F_VERSEID + "=" + favoriteVerse.getVerseid() + ";";
        db.execSQL(sql);
        db.close();
        return true;
    }

    public ArrayList<FavoriteVerse> getFavorite() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + QuranRules.TBL_FAVORITE + ";";
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<FavoriteVerse> lst = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                FavoriteVerse favoriteVerse = new FavoriteVerse(cursor.getInt(0), cursor.getInt(1));
                lst.add(favoriteVerse);
            } while (cursor.moveToNext());
        }
        db.close();
        return lst;
    }

    public ArrayList<FavoriteVerse> getFavoriteBySURAID(int suraid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + QuranRules.TBL_FAVORITE + " WHERE " + QuranRules.F_SURAID + "=" + suraid + ";";
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<FavoriteVerse> lst = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                FavoriteVerse favoriteVerse = new FavoriteVerse(cursor.getInt(0), cursor.getInt(1));
                lst.add(favoriteVerse);
            } while (cursor.moveToNext());
        }
        db.close();
        return lst;
    }


    public void ChooseLanguage(int language) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QuranRules.FLAG, language);
        db.insert(QuranRules.TBL_LANGUAGE, null, values);
        db.close();
    }

    public boolean EditLanguage(int language) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + QuranRules.TBL_LANGUAGE + " SET " + QuranRules.FLAG + "=" + language + ";";
        db.execSQL(sql);
        db.close();
        return true;
    }

    public ArrayList<MyLanguage> getLanguage() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + QuranRules.TBL_LANGUAGE + ";";
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<MyLanguage> lst = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                MyLanguage language = new MyLanguage(cursor.getInt(0));
                lst.add(language);
            } while (cursor.moveToNext());
        }
        db.close();
        return lst;
    }


}
