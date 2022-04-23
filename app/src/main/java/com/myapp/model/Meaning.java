package com.myapp.model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Meaning implements Serializable {
    private int id;
    private int enWordId;
    private int partOfSpeechId;
    private String meaning;
    private ArrayList<ExampleDetail> listExampleDetails;

    private String partOfSpeechName;

    public String getPartOfSpeechName() {
        return partOfSpeechName;
    }

    public void setPartOfSpeechName(String partOfSpeechName) {
        this.partOfSpeechName = partOfSpeechName;
    }

    public Meaning() {
    }

    public Meaning(int id, int enWordId, int partOfSpeechId, String meaning, ArrayList<ExampleDetail> listExampleDetails) {
        this.id = id;
        this.enWordId = enWordId;
        this.partOfSpeechId = partOfSpeechId;
        this.meaning = meaning;
        this.listExampleDetails = listExampleDetails;
    }

    public Meaning(int id, int enWordId, String partOfSpeechName, String meaning, ArrayList<ExampleDetail> listExampleDetails) {
        this.id = id;
        this.enWordId = enWordId;
        this.partOfSpeechName = partOfSpeechName;
        this.meaning = meaning;
        this.listExampleDetails = listExampleDetails;
    }

    public ArrayList<ExampleDetail> getListExampleDetails() {
        return listExampleDetails;
    }

    public void setListExampleDetails(ArrayList<ExampleDetail> listExampleDetails) {
        this.listExampleDetails = listExampleDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEnWordId() {
        return enWordId;
    }

    public void setEnWordId(int enWordId) {
        this.enWordId = enWordId;
    }

    public int getPartOfSpeechId() {
        return partOfSpeechId;
    }

    public void setPartOfSpeechId(int partOfSpeechId) {
        this.partOfSpeechId = partOfSpeechId;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
