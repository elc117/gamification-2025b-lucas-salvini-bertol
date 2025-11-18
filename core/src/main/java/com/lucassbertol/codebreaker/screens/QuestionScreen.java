package com.lucassbertol.codebreaker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lucassbertol.codebreaker.MainGame;
import com.lucassbertol.codebreaker.config.Constants;
import com.lucassbertol.codebreaker.data.Question;
import com.lucassbertol.codebreaker.utils.AnswerValidator;
import com.lucassbertol.codebreaker.managers.GameStateManager;
import com.lucassbertol.codebreaker.managers.TimerManager;
import com.lucassbertol.codebreaker.managers.ScoreManager;
import com.lucassbertol.codebreaker.leaderboard.LeaderboardService;
import com.lucassbertol.codebreaker.leaderboard.LeaderboardService.SubmitCallback;

import java.util.ArrayList;
import java.util.List;

public class QuestionScreen implements Screen {

    private final Stage stage;
    private final Skin skin;
    private final Texture backgroundTexture;
    private final MainGame game;
    private final Question currentQuestion;
    private final String difficulty;
    private final List<TextField> inputFields;
    private final Label messageLabel;
    private final Label progressLabel;
    private final GameStateManager gameState;
    private final TimerManager timerManager;
    private final ScoreManager scoreManager;
    private final Label timerLabel;

    public QuestionScreen(MainGame game, Question question, String difficulty, int questionsAnswered, List<Integer> usedQuestionIds, TimerManager timerManager, ScoreManager scoreManager) {
        this.game = game;
        this.currentQuestion = question;
        this.difficulty = difficulty;
        this.inputFields = new ArrayList<>();
        this.gameState = new GameStateManager(questionsAnswered, usedQuestionIds, Constants.TOTAL_QUESTIONS);
        if (timerManager == null) {
            this.timerManager = new TimerManager(difficulty);
        } else {
            this.timerManager = timerManager;
        }
        if (scoreManager == null) {
            this.scoreManager = new ScoreManager(difficulty);
        } else {
            this.scoreManager = scoreManager;
        }

        // Adiciona a questão atual à lista de usadas
        gameState.markQuestionAsUsed(currentQuestion.getId());

        // Stage com FitViewport
        stage = new Stage(new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT));
        skin = new Skin(Gdx.files.internal(Constants.SKIN_PATH));

        // background que preenche a tela
        backgroundTexture = new Texture(Gdx.files.internal(Constants.BG_QUESTION));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setScaling(Scaling.stretch);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        // Tabela principal que vai conter tudo
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // Timer Label (fora do ScrollPane)
        timerLabel = new Label("0s", skin);
        timerLabel.setFontScale(Constants.TIMER_FONT_SCALE);

        // Tabela de conteúdo (que vai no ScrollPane)
        Table contentTable = new Table();
        contentTable.top();

        // Label de progresso (dentro do ScrollPane)
        progressLabel = new Label(gameState.getProgressText(), skin);
        progressLabel.setFontScale(Constants.PROGRESS_FONT_SCALE);

        // Label do enunciado (contexto da questão)
        Label enunciadoLabel = new Label(currentQuestion.getEnunciado(), skin);
        enunciadoLabel.setFontScale(Constants.QUESTION_TEXT_SCALE);
        enunciadoLabel.setWrap(true);
        enunciadoLabel.setAlignment(com.badlogic.gdx.utils.Align.center);
        enunciadoLabel.setColor(Color.GREEN);

        // Label da questão
        Label questionLabel = new Label(currentQuestion.getQuestaoTexto(), skin);
        questionLabel.setFontScale(Constants.QUESTION_TEXT_SCALE);
        questionLabel.setWrap(true);

        // Label de mensagem (erro/acerto)
        messageLabel = new Label("", skin);
        messageLabel.setColor(Color.RED);
        messageLabel.setFontScale(Constants.FEEDBACK_FONT_SCALE);

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
        verifyButton.getLabel().setFontScale(Constants.BUTTON_FONT_SCALE);

        // Montando a tabela de conteúdo
        contentTable.pad(20);
        contentTable.add(progressLabel).top().padBottom(15);
        contentTable.row();
        contentTable.add(enunciadoLabel).width(1200).top();
        contentTable.row().pad(30);
        contentTable.add(questionLabel).width(1200).top();
        contentTable.row().pad(30);
        contentTable.add(inputTable);
        contentTable.row().pad(20);
        contentTable.add(messageLabel);
        contentTable.row().pad(20);
        contentTable.add(verifyButton).width(300).height(100);
        contentTable.row().padBottom(50);

        // ScrollPane para permitir rolagem
        ScrollPane scrollPane = new ScrollPane(contentTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false); // Apenas scroll vertical

        // Remove o fundo do ScrollPane
        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle(scrollPane.getStyle());
        scrollStyle.background = null;
        scrollPane.setStyle(scrollStyle);

        // Layout principal com timer
        mainTable.add(timerLabel).top().right().padTop(20).padRight(20);
        mainTable.row();
        mainTable.add(scrollPane).width(1770).height(900).pad(10);


        Gdx.input.setInputProcessor(stage);
    }

    private void createInputFields(Table table) {
        inputFields.clear();
        ArrayList<ArrayList<String>> answers = currentQuestion.getAnswer();

        for (int i = 0; i < answers.size(); i++) {
            TextField input = new TextField("", skin);
            input.getStyle().font.getData().setScale(Constants.INPUT_FONT_SCALE);
            inputFields.add(input);

            Label label = new Label("Resposta " + (i + 1) + ":", skin);
            label.setFontScale(Constants.BUTTON_FONT_SCALE);

            table.row().pad(15);
            table.add(label).padRight(20);
            table.add(input).width(500).height(80);
        }
    }

    private void checkAnswer() {
        boolean allCorrect = AnswerValidator.validateAnswers(inputFields, currentQuestion);

        if (allCorrect) {
            scoreManager.addCorrectAnswer();
            gameState.incrementAnsweredQuestions();

            if (gameState.isGameCompleted()) {
                // Parar o timer e o score manager para fixar a pontuação
                timerManager.stop();
                scoreManager.stop();

                messageLabel.setText("Calculando score...");
                messageLabel.setColor(Color.GREEN);

                // Obter nome do jogador e pontuação final
                String nomeJogador = game.getPlayerName();
                int pontuacao = scoreManager.getScore();

                LeaderboardService leaderboardService = new LeaderboardService();
                leaderboardService.submitScore(nomeJogador, pontuacao, new SubmitCallback() {
                    @Override
                    public void onSuccess() {
                        game.setScreen(new ScoreScreen(game, scoreManager));
                    }

                    @Override
                    public void onError(String message) {
                        game.setScreen(new ScoreScreen(game, scoreManager));
                    }
                });

            } else {
                com.lucassbertol.codebreaker.data.QuestionsParsing parser =
                    new com.lucassbertol.codebreaker.data.QuestionsParsing();
                Question nextQuestion = parser.getRandomQuestionExcluding(
                    difficulty, gameState.getUsedQuestionIds()
                );

                game.setScreen(new QuestionScreen(
                    game,
                    nextQuestion,
                    difficulty,
                    gameState.getQuestionsAnswered(),
                    gameState.getUsedQuestionIds(),
                    timerManager,
                    scoreManager
                ));
            }
        } else {
            scoreManager.addWrongAnswer();
            messageLabel.setText("Tente novamente");
            messageLabel.setColor(Color.RED);
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

        timerManager.update(delta);
        scoreManager.update(delta);
        timerLabel.setText("" + timerManager.getFormattedTime());

        if (timerManager.isTimeUp()) {
            game.setScreen(new MenuScreen(game));
            return;
        }

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
