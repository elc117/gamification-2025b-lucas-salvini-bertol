package com.lucassbertol.codebreaker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class DifficultSelectScreen implements Screen {

    private final Stage stage;
    private final Skin skin;
    private final Texture backgroundTexture;
    private final MainGame game;

    public DifficultSelectScreen(MainGame game) {
        this.game = game;

        // Stage com FitViewport (1280x720): mantém proporções e escala consistente
        stage = new Stage(new FitViewport(1280, 720));

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
        title.setFontScale(3f); // escala do título

        // Botões
        TextButton easyButton = new TextButton("FACIL", skin);
        TextButton hardButton = new TextButton("DIFICIL", skin);

        easyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TextQuestionScreen(game, "easy"));
            }
        });
        hardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TextQuestionScreen(game, "hard"));
            }
        });

        // Monta o layout na table:
        table.add(title).colspan(2).padBottom(90f);
        table.row();
        table.add(easyButton).width(220f).height(90f).padRight(40f);
        table.add(hardButton).width(220f).height(90f).padLeft(40f);

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
