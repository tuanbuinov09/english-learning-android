package com.myapp.model;

import java.io.Serializable;

public class ExampleDetail implements Serializable {
    private int id;
    private int meaningId;
    private String example;
    private String exampleMeaning;

    public ExampleDetail() {
    }

    public ExampleDetail(int id, int meaningId, String example, String exampleMeaning) {
        this.id = id;
        this.meaningId = meaningId;
        this.example = example;
        this.exampleMeaning = exampleMeaning;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMeaningId() {
        return meaningId;
    }

    public void setMeaningId(int meaningId) {
        this.meaningId = meaningId;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getExampleMeaning() {
        return exampleMeaning;
    }

    public void setExampleMeaning(String exampleMeaning) {
        this.exampleMeaning = exampleMeaning;
    }
}
