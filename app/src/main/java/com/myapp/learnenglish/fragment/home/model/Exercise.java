package com.myapp.learnenglish.fragment.home.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Exercise implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private ArrayList<Question> questions;

    public Exercise(int id, String title, ArrayList<Question> questions) {
        this.id = id;
        this.title = title;
        this.questions = questions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
