package com.myapp.learnenglish.fragment.home.model;

import java.util.ArrayList;

public class Exercise {
    private int id;
    private int num;
    private ArrayList<Question> questions;

    public Exercise(int id, int num, ArrayList<Question> questions) {
        this.id = id;
        this.num = num;
        this.questions = questions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
