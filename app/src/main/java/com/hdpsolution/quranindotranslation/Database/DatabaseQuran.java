package com.hdpsolution.quranindotranslation.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.hdpsolution.quranindotranslation.Entity.Sura_Quran;
import com.hdpsolution.quranindotranslation.Entity.Vers_Quran;
import com.hdpsolution.quranindotranslation.QuranRules;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQuran extends SQLiteAssetHelper {

    public DatabaseQuran(Context context) {
        super(context, QuranRules.DATABASE_QURAN, null, QuranRules.VERSION_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }

    public ArrayList<Sura_Quran> getSuraQuran() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {QuranRules.SURAID, QuranRules.NAME,QuranRules.NAME_ENG,QuranRules.NAME_FRA,QuranRules.NAME_SPA,
        QuranRules.NAME_ITA,QuranRules.NAME_GER,QuranRules.NAME_TUR,QuranRules.NAME_IND,QuranRules.NAME_RUS,QuranRules.NAME_SOM,QuranRules.PLACE};

        qb.setTables(QuranRules.TBL_SURA);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        ArrayList<Sura_Quran> lst= new ArrayList<>();
        c.moveToFirst();

        while (!c.isAfterLast()){
            Sura_Quran sura= new Sura_Quran(c.getInt(0),c.getString(1),
                    c.getString(2),c.getString(3),
                    c.getString(4),c.getString(5),c.getString(6),
                    c.getString(7),c.getString(8),c.getString(9),
                    c.getString(10),c.getInt(11));
            lst.add(sura);
            c.moveToNext();
        }
        return lst;
    }

    public ArrayList<Vers_Quran> getVerse(int suraid) {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {QuranRules.SURAID,QuranRules.VERSID,QuranRules.VERS_TEXT};

        qb.setTables(QuranRules.TBL_VERS);
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

    public ArrayList<Integer> getSizePart() {
        ArrayList<Integer> result =new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {QuranRules.SURAID,QuranRules.VERSID,QuranRules.VERS_TEXT};

        qb.setTables(QuranRules.TBL_VERS);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        ArrayList<Vers_Quran> lst= new ArrayList<>();
        c.moveToFirst();

        while (!c.isAfterLast()){
            Vers_Quran ver= new Vers_Quran(c.getInt(0),c.getInt(1),c.getString(2)) ;
            lst.add(ver);
            c.moveToNext();
        }

        for (int i=1;i<=114;i++){
            for (int j=0;j<lst.size();j++){
                result.add(0);
                if(lst.get(j).getSuraid()==i){
                    result.set(i-1,result.get(i-1)+1);
                }
            }
        }
        return result;
    }


}
