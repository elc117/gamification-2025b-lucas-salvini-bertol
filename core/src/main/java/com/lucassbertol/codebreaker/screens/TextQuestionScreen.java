package com.lucassbertol.codebreaker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lucassbertol.codebreaker.MainGame;
import com.lucassbertol.codebreaker.data.Question;
import com.lucassbertol.codebreaker.data.QuestionsParsing;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;


public class TextQuestionScreen implements Screen {

    private final SpriteBatch batch;
    private final Texture background;
    private final BitmapFont font;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final MainGame game;
    private QuestionsParsing parser;
    private Question currentQuestion;
    private final Stage stage;
    private final Skin skin;
    private String difficulty;
    private int questionsAnswered;
    private List<Integer> usedQuestionIds;

    // Construtor para primeira vez (chamado do menu de dificuldade)
    public TextQuestionScreen(MainGame game, String difficulty) {
        this(game, difficulty, 0, new ArrayList<>());
    }

    // Construtor completo (chamado após acertar uma questão)
    public TextQuestionScreen(MainGame game, String difficulty, int questionsAnswered, List<Integer> usedQuestionIds) {
        this.game = game;
        this.difficulty = difficulty;
        this.questionsAnswered = questionsAnswered;
        this.usedQuestionIds = usedQuestionIds;

        camera = new OrthographicCamera();
        // Usa FitViewport com resolução virtual 1280x720 para manter proporções
        viewport = new FitViewport(1280, 720, camera);
        camera.position.set(1280 / 2f, 720 / 2f, 0);

        batch = new SpriteBatch();

        // Carrega a imagem de fundo da pasta assets
        background = new Texture("images/question.png");

        // Usa a fonte padrão do libGDX.
        font = new BitmapFont();

        // pega questão aleatória que ainda não foi usada
        parser = new QuestionsParsing();
        currentQuestion = parser.getRandomQuestionExcluding(difficulty, usedQuestionIds);

        // Stage com FitViewport para manter proporções consistentes
        stage = new Stage(new FitViewport(1280, 720));
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        // Cria o botão de iniciar
        TextButton btnIniciar = new TextButton("INICIAR FASE", skin);
        btnIniciar.setSize(250, 120);
        btnIniciar.setPosition(
            (1280 - btnIniciar.getWidth()) / 2,
            200
        );

        // Adiciona o listener para o clique
        btnIniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new QuestionScreen(game, currentQuestion, difficulty, questionsAnswered, usedQuestionIds));
            }
        });

        // Adiciona o botão ao stage
        stage.addActor(btnIniciar);

        // Define o stage como o processador de entrada
        Gdx.input.setInputProcessor(stage);

    }
    @Override
    public void show() {
        // Este método é chamado quando a tela se torna a tela ativa.
    }

    @Override
    public void render(float delta) {
        // Limpa a tela
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Atualiza a câmera
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Inicia o desenho
        batch.begin();

        // Desenha a imagem de fundo para preencher a viewport virtual (1280x720)
        batch.draw(background, 0, 0, 1280, 720);

        // Desenha o enunciado formatado com quebra de linhas
        String enunciado = currentQuestion.getEnunciado();
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, enunciado, Color.WHITE, 900, Align.left, true);

        // Calcula a posição X para centralizar o texto
        float x = (1280 - layout.width) / 2;
        float y = 720 - 200;

        font.draw(batch, layout, x, y);
        font.getData().setScale(2.5f);

        batch.end();

        // Renderiza o stage (botões)
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Atualiza os viewports quando a janela é redimensionada
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() {
        // Chamado quando a tela não é mais a ativa.
    }

    @Override
    public void dispose() {
        // Libera os recursos para evitar vazamento de memória
        background.dispose();
        font.dispose();
        batch.dispose();
        stage.dispose();
        skin.dispose();
    }
}

