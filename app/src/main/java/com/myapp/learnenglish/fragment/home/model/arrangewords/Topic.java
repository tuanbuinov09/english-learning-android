package com.myapp.learnenglish.fragment.home.model.arrangewords;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class Topic {
    @Exclude
    private String key;
    private String title;
    private ArrayList<Exercise> exercises;

    public Topic(String key, String title, ArrayList<Exercise> exercises) {
        this.key = key;
        this.title = title;
        this.exercises = exercises;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
