package com.myapp.learnenglish.fragment.home.model.multichoice;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class ExerciseML implements Serializable {
    @Exclude
    private String key;
    private int score;
    private ArrayList<QuestionML> questions;

    public ExerciseML(String key, int score, ArrayList<QuestionML> questions) {

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

    public ArrayList<QuestionML> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuestionML> questions) {
        this.questions = questions;
    }
}
