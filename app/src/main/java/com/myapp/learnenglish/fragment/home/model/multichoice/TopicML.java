package com.myapp.learnenglish.fragment.home.model.multichoice;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class TopicML implements Serializable {
    @Exclude
    private String key;
    private ArrayList<ExerciseML> exercises;

    public TopicML(String key, ArrayList<ExerciseML> exercises) {
        this.key = key;
        this.exercises = exercises ;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<ExerciseML> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<ExerciseML> exercises) {
        this.exercises = exercises;
    }
}
