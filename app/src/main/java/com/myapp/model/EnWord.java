package com.myapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EnWord implements Serializable {
    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int value) {
        this.id = value;
    }

    private String word;

    public String getWord() {
        return this.word;
    }

    public void setWord(String value) {
        this.word = value;
    }
    private int views;
    private String pronunciation;

    public String getPronunciation() {
        return this.pronunciation;
    }

    public void setPronunciation(String value) {
        this.pronunciation = value;
    }

    private ArrayList<Meaning> listMeaning;

    public ArrayList<Meaning> getListMeaning() {
        return listMeaning;
    }

    public void setListMeaning(ArrayList<Meaning> listMeaning) {
        this.listMeaning = listMeaning;
    }

    public EnWord(int id_, String word_, String pronunciation_, ArrayList<Meaning> listMeaning_) {
        this.id = id_;
        this.word = word_;
        this.pronunciation = pronunciation_;
        this.listMeaning = listMeaning_;
    }
    public EnWord(int id_, String word_, String pronunciation_) {
        this.id = id_;
        this.word = word_;
        this.pronunciation = pronunciation_;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public EnWord() {
    }

    @Override
    public String toString() {
        return "EnWord{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", views=" + views +
                ", pronunciation='" + pronunciation + '\'' +
                ", listMeaning=" + listMeaning +
                '}';
    }
}
