package com.myapp.learnenglish.fragment.home.model.multichoice;
import java.io.Serializable;

public class QuestionML implements Serializable {
    private String key;
    private String A;
    private String B;
    private String C;
    private String D;
    private String content;
    private String answer;


    public QuestionML(String key, String a, String b, String c, String d, String content, String answer) {
        this.key = key;
        A = a;
        B = b;
        C = c;
        D = d;
        this.content = content;
        this.answer = answer;
    }

    public String getA() {
        return A;
    }

    public String getB() {
        return B;
    }

    public String getC() {
        return C;
    }

    public String getD() {
        return D;
    }

    public void setA(String a) {
        A = a;
    }

    public void setB(String b) {
        B = b;
    }

    public void setC(String c) {
        C = c;
    }

    public void setD(String d) {
        D = d;
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
