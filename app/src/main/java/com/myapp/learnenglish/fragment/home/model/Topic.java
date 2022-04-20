package com.myapp.learnenglish.fragment.home.model;

import java.util.ArrayList;

public class Topic {
    private int id;
    private String title;
    private ArrayList<Exercise> exercises;

    public Topic(int id, String title, ArrayList<Exercise> exercises) {
        this.id = id;
        this.title = title;
        this.exercises = exercises;
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

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }
}
