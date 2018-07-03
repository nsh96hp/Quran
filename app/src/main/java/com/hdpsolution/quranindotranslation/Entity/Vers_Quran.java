package com.hdpsolution.quranindotranslation.Entity;

public class Vers_Quran {
    private int suraid;
    private int versid;
    private String text;

    public Vers_Quran(int suraid, int versid, String text) {
        this.suraid = suraid;
        this.versid = versid;
        this.text = text;
    }

    public Vers_Quran() {
    }

    public int getSuraid() {
        return suraid;
    }

    public void setSuraid(int suraid) {
        this.suraid = suraid;
    }

    public int getVersid() {
        return versid;
    }

    public void setVersid(int versid) {
        this.versid = versid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
