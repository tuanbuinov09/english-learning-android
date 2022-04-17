package com.myapp.learnenglish.fragment.home.model;

import java.util.ArrayList;

public class Unit {
    private int id;
    private int num;
    private String title;
    private ArrayList<Exercise> exercises;

    public Unit(int id, int num, String title, ArrayList<Exercise> exercises) {
        this.id = id;
        this.num = num;
        this.title = title;
        this.exercises = exercises;
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
