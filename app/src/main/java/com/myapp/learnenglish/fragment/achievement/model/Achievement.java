package com.myapp.learnenglish.fragment.achievement.model;

import java.io.Serializable;

public class Achievement implements Serializable {
    private String name;
    private String description;
    private int requiredQuantity;
    private String path;

    public Achievement(String name, String description, int requiredQuantity, String path) {
        this.name = name;
        this.description = description;
        this.requiredQuantity = requiredQuantity;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(int requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
