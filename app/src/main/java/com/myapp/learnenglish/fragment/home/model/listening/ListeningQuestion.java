package com.myapp.learnenglish.fragment.home.model.listening;

import java.io.Serializable;

public class ListeningQuestion implements Serializable {
    private String content;

    public ListeningQuestion(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
