package com.myapp.learnenglish.fragment.achievement.model;

public class UserAchievement {
    private int learned;
    private int score;

    public UserAchievement(int learned, int score) {
        this.learned = learned;
        this.score = score;
    }

    public int getLearned() {
        return learned;
    }

    public void setLearned(int learned) {
        this.learned = learned;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
