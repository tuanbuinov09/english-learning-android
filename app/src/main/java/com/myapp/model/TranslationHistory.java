package com.myapp.model;

import java.time.LocalDate;

public class TranslationHistory {
    private int id;
    private String originalText;
    private String translatedText;
    private LocalDate date;

    public TranslationHistory(String text, String translatedText, LocalDate date) {
        this.originalText = text;
        this.translatedText = translatedText;
        this.date = date;
    }

    public TranslationHistory(int id, String text, String translatedText, LocalDate date) {
        this.id = id;
        this.originalText = text;
        this.translatedText = translatedText;
        this.date = date;
    }

    public TranslationHistory() {
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
