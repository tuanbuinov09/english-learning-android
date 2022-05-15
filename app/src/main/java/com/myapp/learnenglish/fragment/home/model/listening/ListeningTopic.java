package com.myapp.learnenglish.fragment.home.model.listening;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class ListeningTopic implements Serializable {
    @Exclude
    private String key;
    private String title;
    private ArrayList<ListeningExercise> exercises;

    public ListeningTopic(String key, String title, ArrayList<ListeningExercise> exercises) {
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

    public ArrayList<ListeningExercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<ListeningExercise> exercises) {
        this.exercises = exercises;
    }
}
