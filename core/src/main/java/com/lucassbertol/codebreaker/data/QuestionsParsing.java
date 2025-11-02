package com.lucassbertol.codebreaker.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import java.util.List;
import java.util.Random;

public class QuestionsParsing {
    private final Gson gson;
    private final Random random;

    public QuestionsParsing() {
        this.gson = new Gson();
        this.random = new Random();
    }

    public QuestionsData loadEasyQuestions() {
        FileHandle file = Gdx.files.internal("questions/easy.json");
        String jsonContent = file.readString();
        return gson.fromJson(jsonContent, QuestionsData.class);
    }

    public QuestionsData loadHardQuestions() {
        FileHandle file = Gdx.files.internal("questions/hard.json");
        String jsonContent = file.readString();
        return gson.fromJson(jsonContent, QuestionsData.class);
    }

    public QuestionsData loadQuestionsByDifficulty(String difficulty) {
        String path = "questions/" + difficulty.toLowerCase() + ".json";
        FileHandle file = Gdx.files.internal(path);
        String jsonContent = file.readString();
        return gson.fromJson(jsonContent, QuestionsData.class);
    }

    public Question getRandomQuestion(String difficulty) {
        QuestionsData data = loadQuestionsByDifficulty(difficulty);
        List<Question> questions = data.getQuestoes();
        int randomIndex = random.nextInt(questions.size());
        return questions.get(randomIndex);
    }
}
