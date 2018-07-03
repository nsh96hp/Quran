package com.hdpsolution.quranindotranslation.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Sura_Quran {
    private int suraid;
    private String name;
    private String name_english;
    private String name_fra;
    private String name_spa;
    private String name_ita;
    private String name_ger;
    private String name_tur;
    private String name_ind;
    private String name_rus;
    private String name_som;
    private int place;

    public Sura_Quran(int suraid, String name, String name_english, String name_fra, String name_spa, String name_ita, String name_ger, String name_tur, String name_ind, String name_rus, String name_som, int place) {
        this.suraid = suraid;
        this.name = name;
        this.name_english = name_english;
        this.name_fra = name_fra;
        this.name_spa = name_spa;
        this.name_ita = name_ita;
        this.name_ger = name_ger;
        this.name_tur = name_tur;
        this.name_ind = name_ind;
        this.name_rus = name_rus;
        this.name_som = name_som;
        this.place = place;
    }

    public Sura_Quran() {
    }

    public int getSuraid() {
        return suraid;
    }

    public void setSuraid(int suraid) {
        this.suraid = suraid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_english() {
        return name_english;
    }

    public void setName_english(String name_english) {
        this.name_english = name_english;
    }

    public String getName_fra() {
        return name_fra;
    }

    public void setName_fra(String name_fra) {
        this.name_fra = name_fra;
    }

    public String getName_spa() {
        return name_spa;
    }

    public void setName_spa(String name_spa) {
        this.name_spa = name_spa;
    }

    public String getName_ita() {
        return name_ita;
    }

    public void setName_ita(String name_ita) {
        this.name_ita = name_ita;
    }

    public String getName_ger() {
        return name_ger;
    }

    public void setName_ger(String name_ger) {
        this.name_ger = name_ger;
    }

    public String getName_tur() {
        return name_tur;
    }

    public void setName_tur(String name_tur) {
        this.name_tur = name_tur;
    }

    public String getName_ind() {
        return name_ind;
    }

    public void setName_ind(String name_ind) {
        this.name_ind = name_ind;
    }

    public String getName_rus() {
        return name_rus;
    }

    public void setName_rus(String name_rus) {
        this.name_rus = name_rus;
    }

    public String getName_som() {
        return name_som;
    }

    public void setName_som(String name_som) {
        this.name_som = name_som;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }


}
