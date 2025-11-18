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

        stage = new Stage(new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT));
        skin = new Skin(Gdx.files.internal(Constants.SKIN_PATH));

        backgroundTexture = new Texture(Gdx.files.internal(Constants.BG_INPUT));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setScaling(Scaling.stretch);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label titleLabel = new Label(Constants.MSG_ENTER_USERNAME, skin);
        titleLabel.setFontScale(Constants.TITLE_FONT_SCALE);
        table.add(titleLabel).colspan(2).padBottom(60f);
        table.row();

        TextField nameInput = new TextField("", skin);
        nameInput.getStyle().font.getData().setScale(Constants.INPUT_FONT_SCALE);
        nameInput.setMessageText("");

        TextButton nextButton = new TextButton(Constants.BTN_CONTINUE, skin);

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String playerName = nameInput.getText();
                if (!playerName.isEmpty()) {
                    // AQUI: salva o nome no MainGame
                    game.setPlayerName(playerName);

                    // Depois de salvar, vai para a próxima tela
                    game.setScreen(new DifficultSelectScreen(game));
                }
            }
        });
        nextButton.getLabel().setFontScale(Constants.BUTTON_FONT_SCALE);

        table.row();
        table.add(nameInput).width(Constants.INPUT_WIDTH).height(Constants.INPUT_HEIGHT).colspan(2).padBottom(40f);
        table.row();
        table.add(nextButton).width(Constants.BUTTON_WIDTH).height(Constants.BUTTON_HEIGHT).colspan(2);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() { }

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
