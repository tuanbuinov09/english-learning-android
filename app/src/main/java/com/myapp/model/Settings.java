package com.myapp.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Settings implements Serializable {
    private Locale voiceLanguage;
    private float voiceSpeed;

    private boolean isRemindWords, isDarkTheme;
    private int numberOfRemindADay;
    private LocalTime startTime, endTime;
    private List<Integer> remindDay;

    public Settings() {
        voiceLanguage = Locale.US;
        voiceSpeed = VoiceSpeed.NORMAL;
        isRemindWords = true;
        isDarkTheme = false;
        numberOfRemindADay = 1;
        startTime = LocalTime.of(9, 00);
        endTime = LocalTime.of(17, 00);
        remindDay = new ArrayList<>();
    }

    public boolean isDarkTheme() {
        return isDarkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        isDarkTheme = darkTheme;
    }

    public Locale getVoiceLanguage() {
        return voiceLanguage;
    }

    public void setVoiceLanguage(Locale voiceLanguage) {
        this.voiceLanguage = voiceLanguage;
    }

    public float getVoiceSpeed() {
        return voiceSpeed;
    }

    public void setVoiceSpeed(float voiceSpeed) {
        this.voiceSpeed = voiceSpeed;
    }

    public boolean isRemindWords() {
        return isRemindWords;
    }

    public void setRemindWords(boolean remindWords) {
        isRemindWords = remindWords;
    }

    public int getNumberOfRemindADay() {
        return numberOfRemindADay;
    }

    public void setNumberOfRemindADay(int numberOfRemindADay) {
        this.numberOfRemindADay = numberOfRemindADay;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public List<Integer> getRemindDay() {
        return remindDay;
    }

    public void setRemindDay(List<Integer> remindDay) {
        this.remindDay = remindDay;
    }
}