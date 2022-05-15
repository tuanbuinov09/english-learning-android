package com.myapp.learnenglish.fragment.home.model.arrangewords;

import java.io.Serializable;

public class Question implements Serializable {
    private String key;
    private String content;
    private String answer;

    public Question(String key, String content, String answer) {
        this.key = key;
        this.content = content;
        this.answer = answer;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
