package com.lucassbertol.codebreaker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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
import com.lucassbertol.codebreaker.config.Constants;

public class UserInputScreen implements Screen {

    private final Stage stage;
    private final Skin skin;
    private final Texture backgroundTexture;
    private final MainGame game;

    public UserInputScreen(MainGame game) {
        this.game = game;

        // Stage com FitViewport: mantém proporções e escala consistente
        stage = new Stage(new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT));

        // skin (botões, labels)
        skin = new Skin(Gdx.files.internal(Constants.SKIN_PATH));

        // background que preenche a tela
        backgroundTexture = new Texture(Gdx.files.internal(Constants.BG_SELECT));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setScaling(Scaling.stretch);
        backgroundImage.setFillParent(true); // ocupa toda a área do stage
        stage.addActor(backgroundImage);

        // Table para organizar título e botões
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        // Título
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        Label title = new Label(Constants.MSG_ENTER_USERNAME, labelStyle);
        title.setFontScale(Constants.TITLE_FONT_SCALE);

        // Input de texto
        TextField nameInput = new TextField("", skin);
        nameInput.getStyle().font.getData().setScale(Constants.INPUT_FONT_SCALE);
        nameInput.setMessageText("");

        // Botões
        TextButton nextButton = new TextButton(Constants.BTN_CONTINUE, skin);

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String playerName = nameInput.getText();
                if (!playerName.isEmpty()) { // Verifica se o nome não está vazio
                    game.setScreen(new DifficultSelectScreen(game));
                }
            }
        });
        nextButton.getLabel().setFontScale(Constants.BUTTON_FONT_SCALE);

        // Monta o layout na table:
        table.add(title).colspan(2).padBottom(40f);
        table.row();
        table.add(nameInput).width(Constants.INPUT_WIDTH).height(Constants.INPUT_HEIGHT).colspan(2).padBottom(40f);
        table.row();
        table.add(nextButton).width(Constants.BUTTON_WIDTH).height(Constants.BUTTON_HEIGHT).colspan(2);

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
