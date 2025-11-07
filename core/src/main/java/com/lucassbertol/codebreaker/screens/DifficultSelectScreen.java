package com.lucassbertol.codebreaker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lucassbertol.codebreaker.MainGame;
import com.lucassbertol.codebreaker.data.Question;
import com.lucassbertol.codebreaker.data.QuestionsParsing;

import java.util.ArrayList;

public class DifficultSelectScreen implements Screen {

    private final Stage stage;
    private final Skin skin;
    private final Texture backgroundTexture;
    private final MainGame game;
    private Label messageLabel;
    private boolean counting = false;
    private float countdownTimer = 5f;
    private String selectedDifficulty = "";
    private Question selectedQuestion = null;

    public DifficultSelectScreen(MainGame game) {
        this.game = game;

        // Stage com FitViewport (1920x1080): mantém proporções e escala consistente
        stage = new Stage(new FitViewport(1920, 1080));

        // skin (botões, labels)
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        // background que preenche a tela
        backgroundTexture = new Texture(Gdx.files.internal("images/selectFase.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setScaling(Scaling.stretch);
        backgroundImage.setFillParent(true); // ocupa toda a área do stage
        stage.addActor(backgroundImage);

        // Table para organizar título e botões; Table facilita centralização e responsividade
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        // Título
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font"); // usa a fonte do uiskin default
        Label title = new Label("ESCOLHA A DIFICULDADE:", labelStyle);
        title.setFontScale(3.0f); // escala do título

        // Botões
        TextButton easyButton = new TextButton("FACIL", skin);
        TextButton hardButton = new TextButton("DIFICIL", skin);

        easyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!counting) {
                    // Carrega a primeira questão
                    QuestionsParsing parser = new QuestionsParsing();
                    selectedQuestion = parser.getRandomQuestionExcluding("easy", new ArrayList<>());
                    selectedDifficulty = "easy";

                    // Inicia a contagem regressiva
                    counting = true;
                    countdownTimer = 5f;
                    messageLabel.setText("Iniciando em 5 segundos, prepare-se!");
                    messageLabel.setColor(Color.GREEN);

                    // Desabilita os botões durante a contagem
                    easyButton.setDisabled(true);
                    hardButton.setDisabled(true);
                }
            }
        });
        easyButton.getLabel().setFontScale(1.5f);

        hardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!counting) {
                    // Carrega a primeira questão
                    QuestionsParsing parser = new QuestionsParsing();
                    selectedQuestion = parser.getRandomQuestionExcluding("hard", new ArrayList<>());
                    selectedDifficulty = "hard";

                    // Inicia a contagem regressiva
                    counting = true;
                    countdownTimer = 5f;
                    messageLabel.setText("Iniciando em 5 segundos, prepare-se!");
                    messageLabel.setColor(Color.GREEN);

                    // Desabilita os botões durante a contagem
                    easyButton.setDisabled(true);
                    hardButton.setDisabled(true);
                }
            }
        });
        hardButton.getLabel().setFontScale(1.5f);

        // Label de mensagem (contagem regressiva)
        messageLabel = new Label("", skin);
        messageLabel.setFontScale(2.5f);
        messageLabel.setColor(Color.GREEN);

        // Monta o layout na table:
        table.add(title).colspan(2).padBottom(60f);
        table.row();
        table.add(easyButton).width(300f).height(100f).padRight(30f);
        table.add(hardButton).width(300f).height(100f).padLeft(30f);
        table.row();
        table.add(messageLabel).colspan(2).padTop(40f);

        // Input para a stage
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Limpa tela
        Gdx.gl.glClearColor(0f, 0f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Se estiver contando, atualiza o contador
        if (counting) {
            countdownTimer -= delta;

            // Quando o tempo acabar, vai para a próxima tela
            if (countdownTimer <= 0) {
                game.setScreen(new QuestionScreen(game, selectedQuestion, selectedDifficulty, 0, new ArrayList<>()));
            }
        }

        // Atualiza e desenha background, table, botões, título
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Atualiza a viewport do stage para o novo tamanho e recenter no meio
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
