package com.lucassbertol.codebreaker.utils;

import java.util.ArrayList;
import java.util.List;

public class GameStateManager {
    private int questionsAnswered;
    private final List<Integer> usedQuestionIds;
    private final int totalQuestions;

    public GameStateManager() {
        this(5); // Por padrão, 5 questões
    }

    public GameStateManager(int totalQuestions) {
        this.questionsAnswered = 0;
        this.usedQuestionIds = new ArrayList<>();
        this.totalQuestions = totalQuestions;
    }

    public GameStateManager(int questionsAnswered, List<Integer> usedQuestionIds, int totalQuestions) {
        this.questionsAnswered = questionsAnswered;
        this.usedQuestionIds = usedQuestionIds != null ? usedQuestionIds : new ArrayList<>();
        this.totalQuestions = totalQuestions;
    }

    public void markQuestionAsUsed(int questionId) {
        if (!usedQuestionIds.contains(questionId)) {
            usedQuestionIds.add(questionId);
        }
    }

    // incrementa o contador
    public void incrementAnsweredQuestions() {
        questionsAnswered++;
    }

    // verifica se o jogo acabou
    public boolean isGameCompleted() {
        return questionsAnswered >= totalQuestions;
    }

    // retorna numero de questões respondidas
    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    // retorna lista de questões usadas
    public List<Integer> getUsedQuestionIds() {
        return usedQuestionIds;
    }

    // retorna total de questões
    public int getTotalQuestions() {
        return totalQuestions;
    }

    // retorna texto de progresso
    public String getProgressText() {
        return "Questao " + (questionsAnswered + 1) + " de " + totalQuestions;
    }
}

