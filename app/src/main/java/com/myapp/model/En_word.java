package com.myapp.model;

public class En_word {
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

    private String pronunciation;

    public String getPronunciation() {
        return this.pronunciation;
    }

    public void setPronunciation(String value) {
        this.pronunciation = value;
    }

    private String meaning;

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public En_word(int id_, String word_, String pronunciation_, String meaning_) {
        this.id = id_;
        this.word = word_;
        this.pronunciation = pronunciation_;
        this.meaning = meaning_;
    }
}
