package com.lucassbertol.codebreaker.data;

import java.util.List;

public class Question {
    private int id;
    private String title;
    private String text;
    private String question;
    private List<List<String>> answer;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getQuestion() {
        return question;
    }

    public List<List<String>> getAnswer() {
        return answer;
    }

    public String getEnunciado() {
        return text;
    }

    public String getQuestaoTexto() {
        return question;
    }
}
