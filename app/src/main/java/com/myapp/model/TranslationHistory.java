package com.myapp.model;

public class TranslationHistory {
    private String text;
    private String translatedText;

    public TranslationHistory(String text, String translatedText) {
        this.text = text;
        this.translatedText = translatedText;
    }

    public TranslationHistory() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }
}
