package com.hdpsolution.quranindotranslation.Entity;

import android.support.annotation.NonNull;

public class FavoriteVerse implements Comparable<FavoriteVerse>{

    private int suraid;
    private int verseid;

    public FavoriteVerse() {
    }

    public FavoriteVerse( int suraid, int verseid) {
        this.suraid = suraid;
        this.verseid = verseid;
    }


    public int getSuraid() {
        return suraid;
    }

    public void setSuraid(int suraid) {
        this.suraid = suraid;
    }

    public int getVerseid() {
        return verseid;
    }

    public void setVerseid(int verseid) {
        this.verseid = verseid;
    }

    @Override
    public int compareTo(@NonNull FavoriteVerse o) {
        return this.getSuraid()-o.getSuraid();
    }
}
