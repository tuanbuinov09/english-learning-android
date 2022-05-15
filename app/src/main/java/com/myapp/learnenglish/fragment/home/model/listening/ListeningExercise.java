package com.myapp.learnenglish.fragment.home.model.listening;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class ListeningExercise implements Serializable {
    @Exclude
    private String key;
    private int score;
    private ArrayList<ListeningQuestion> questions;

    public ListeningExercise(String key, int score, ArrayList<ListeningQuestion> questions) {
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<ListeningQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<ListeningQuestion> questions) {
        this.questions = questions;
    }
}
