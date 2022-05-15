package com.myapp.learnenglish.fragment.home.model.arrangewords;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class Exercise implements Serializable {
    private static final long serialVersionUID = 1L;

    @Exclude
    private String key;
    private int score;
    private ArrayList<Question> questions;

    public Exercise(String key, int score, ArrayList<Question> questions) {
        this.key = key;
        this.score = score;
        this.questions = questions;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
