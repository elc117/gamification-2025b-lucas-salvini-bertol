package com.lucassbertol.codebreaker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lucassbertol.codebreaker.MainGame;
import com.lucassbertol.codebreaker.data.Question;
import com.lucassbertol.codebreaker.utils.AnswerValidator;
import com.lucassbertol.codebreaker.utils.GameStateManager;

import java.util.ArrayList;
import java.util.List;

public class QuestionScreen implements Screen {

    private final Stage stage;
    private final Skin skin;
    private final Texture backgroundTexture;
    private final MainGame game;
    private Question currentQuestion;
    private final String difficulty;
    private final List<TextField> inputFields;
    private Label messageLabel;
    private Label progressLabel;
    private GameStateManager gameState;

    public QuestionScreen(MainGame game, Question question, String difficulty, int questionsAnswered, List<Integer> usedQuestionIds) {
        this.game = game;
        this.currentQuestion = question;
        this.difficulty = difficulty;
        this.inputFields = new ArrayList<>();
        this.gameState = new GameStateManager(questionsAnswered, usedQuestionIds, 5);

        // Adiciona a questão atual à lista de usadas
        gameState.markQuestionAsUsed(currentQuestion.getId());

        // Stage com FitViewport (1920x1080): mantém proporções e escala consistente
        stage = new Stage(new FitViewport(1920, 1080));
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        // Background
        backgroundTexture = new Texture(Gdx.files.internal("images/question.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setScaling(Scaling.stretch);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        // Tabela principal que vai conter tudo
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // Label de progresso no topo (fixo)
        progressLabel = new Label(gameState.getProgressText(), skin);
        progressLabel.setFontScale(1.5f);

        // Tabela de conteúdo (que vai no ScrollPane)
        Table contentTable = new Table();
        contentTable.top();

        // Label do enunciado (contexto da questão)
        Label enunciadoLabel = new Label(currentQuestion.getEnunciado(), skin);
        enunciadoLabel.setFontScale(2.0f);
        enunciadoLabel.setWrap(true);
        enunciadoLabel.setAlignment(com.badlogic.gdx.utils.Align.center);

        // Label da questão
        Label questionLabel = new Label(currentQuestion.getQuestaoTexto(), skin);
        questionLabel.setFontScale(2.0f);
        questionLabel.setWrap(true);

        // Label de mensagem (erro/acerto)
        messageLabel = new Label("", skin);
        messageLabel.setColor(Color.RED);
        messageLabel.setFontScale(1.8f);

        // Tabela para inputs
        Table inputTable = new Table();
        createInputFields(inputTable);

        // Botão verificar
        TextButton verifyButton = new TextButton("VERIFICAR", skin);
        verifyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                checkAnswer();
            }
        });
        verifyButton.getLabel().setFontScale(1.5f);

        // Montando a tabela de conteúdo
        contentTable.row().pad(20);
        contentTable.add(enunciadoLabel).width(1200).top();
        contentTable.row().pad(30);
        contentTable.add(questionLabel).width(1200).top();
        contentTable.row().pad(30);
        contentTable.add(inputTable);
        contentTable.row().pad(20);
        contentTable.add(messageLabel);
        contentTable.row().pad(30);
        contentTable.add(verifyButton).width(300).height(100);
        contentTable.row().padBottom(50); // Espaço no final

        // ScrollPane para permitir rolagem
        ScrollPane scrollPane = new ScrollPane(contentTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false); // Apenas scroll vertical

        // Remove o fundo do ScrollPane (deixa transparente)
        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle(scrollPane.getStyle());
        scrollStyle.background = null; // Remove o fundo cinza
        scrollPane.setStyle(scrollStyle);

        // Montando o layout principal
        mainTable.row().pad(20);
        mainTable.add(progressLabel).top();
        mainTable.row().expandY().fillY();
        mainTable.add(scrollPane).expand().fill().pad(10);

        Gdx.input.setInputProcessor(stage);
    }

    private void createInputFields(Table table) {
        inputFields.clear();
        List<List<String>> answers = currentQuestion.getAnswer();

        for (int i = 0; i < answers.size(); i++) {
            TextField input = new TextField("", skin);
            inputFields.add(input);

            Label label = new Label("Resposta " + (i + 1) + ":", skin);
            label.setFontScale(1.5f);

            table.row().pad(15);
            table.add(label).padRight(20);
            table.add(input).width(500).height(80);
        }
    }

    private void checkAnswer() {
        // Usa o AnswerValidator para validar as respostas
        boolean allCorrect = AnswerValidator.validateAnswers(inputFields, currentQuestion);

        if (allCorrect) {
            // Incrementa o contador de questões respondidas
            gameState.incrementAnsweredQuestions();

            if (gameState.isGameCompleted()) {
                // Completou as 5 questões
                messageLabel.setText("PARABENS! Voce completou todas as questoes!");
                messageLabel.setColor(Color.GREEN);

                // Aguarda um pouco e volta para menu
                Gdx.app.postRunnable(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Gdx.app.log("QuestionScreen", "Error waiting: " + e.getMessage());
                    }
                    game.setScreen(new DifficultSelectScreen(game));
                });
            } else {
                // Vai para a próxima questão
                com.lucassbertol.codebreaker.data.QuestionsParsing parser = new com.lucassbertol.codebreaker.data.QuestionsParsing();
                Question nextQuestion = parser.getRandomQuestionExcluding(difficulty, gameState.getUsedQuestionIds());

                game.setScreen(new QuestionScreen(game, nextQuestion, difficulty,
                    gameState.getQuestionsAnswered(), gameState.getUsedQuestionIds()));
            }
        } else {
            // Resposta incorreta
            messageLabel.setText("ERRADO! Tente novamente.");
            messageLabel.setColor(Color.RED);

            // Usa o AnswerValidator para limpar os campos
            AnswerValidator.clearInputFields(inputFields);
        }
    }


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
    }
}

