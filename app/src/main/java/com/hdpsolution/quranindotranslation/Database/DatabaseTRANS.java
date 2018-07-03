package com.hdpsolution.quranindotranslation.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.hdpsolution.quranindotranslation.Entity.Vers_Quran;
import com.hdpsolution.quranindotranslation.QuranRules;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class DatabaseTRANS extends SQLiteAssetHelper {

    public DatabaseTRANS(Context context, String DatabaseTRANS) {
        super(context, DatabaseTRANS, null, QuranRules.VERSION_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }

    public ArrayList<Vers_Quran> getVerseTrans(int suraid) {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {QuranRules.SURA,QuranRules.VERS,QuranRules.TRANS};

        qb.setTables(QuranRules.TBL_TRANS);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        ArrayList<Vers_Quran> lst= new ArrayList<>();
        c.moveToFirst();

        while (!c.isAfterLast()){
            Vers_Quran ver= new Vers_Quran(c.getInt(0),c.getInt(1),c.getString(2)) ;
            lst.add(ver);
            c.moveToNext();
        }
        ArrayList<Vers_Quran> result=new ArrayList<>();
        for (int i=0;i<lst.size();i++){
            if(lst.get(i).getSuraid()==suraid){
                result.add(lst.get(i));
            }
        }
        return result;
    }

    public ArrayList<Vers_Quran> getAllVerseTrans() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {QuranRules.SURA,QuranRules.VERS,QuranRules.TRANS};

        qb.setTables(QuranRules.TBL_TRANS);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        ArrayList<Vers_Quran> lst= new ArrayList<>();
        c.moveToFirst();
        while (!c.isAfterLast()){
            Vers_Quran ver= new Vers_Quran(c.getInt(0),c.getInt(1),c.getString(2)) ;
            lst.add(ver);
            c.moveToNext();
        }
        return lst;
    }


}
