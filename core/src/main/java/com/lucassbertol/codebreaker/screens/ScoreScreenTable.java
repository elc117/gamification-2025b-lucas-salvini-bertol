package com.lucassbertol.codebreaker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.Json;
import com.lucassbertol.codebreaker.MainGame;
import com.lucassbertol.codebreaker.config.Constants;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import org.checkerframework.checker.units.qual.C;


import java.util.HashMap;
import java.util.Map;

import static com.lucassbertol.codebreaker.config.Constants.*;

public class ScoreScreenTable implements Screen {
    private final MainGame game;
    private Stage stage;
    private Table scoreTable;
    private Skin skin;
    private Texture background;

    public ScoreScreenTable(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal(Constants.SKIN_PATH));
        background = new Texture(Gdx.files.internal(Constants.BG_SCORE));

        // Tabela principal
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top();

        // Botão voltar no topo esquerdo
        TextButton backButton = new TextButton("Voltar", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        Table topTable = new Table();
        topTable.add(backButton).left().pad(20);
        mainTable.add(topTable).expandX().fillX().top().left().row();

        // Tabela de scores
        scoreTable = new Table();
        scoreTable.pad(0);

        // Cabeçalho
        Label headerPos = new Label("Posicao", skin);
        headerPos.setColor(Color.GREEN);
        headerPos.setFontScale(Constants.TITLE_FONT_SCALE);
        Label headerName = new Label("Nome", skin);
        headerName.setColor(Color.GREEN);
        headerName.setFontScale(Constants.TITLE_FONT_SCALE);
        Label headerPoints = new Label("Pontos", skin);
        headerPoints.setColor(Color.GREEN);
        headerPoints.setFontScale(Constants.TITLE_FONT_SCALE);

        scoreTable.add(headerPos).pad(30);
        scoreTable.add(headerName).pad(30);
        scoreTable.add(headerPoints).pad(30);
        scoreTable.row();

        // ScrollPane para a tabela
        ScrollPane scrollPane = new ScrollPane(scoreTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        Drawable scrollPaneBackground = scrollPane.getStyle().background;
        scrollPane.getStyle().background = null;
        mainTable.add(scrollPane).width(1280).height(600).expand().pad(0).row();

        stage.addActor(mainTable);

        fetchScores();
    }

    private void fetchScores() {
        String url = SHEETS_API_URL + SPREADSHEET_ID + "/values/" + RANGE + "?key=" + API_KEY;

        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
        request.setUrl(url);

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String jsonResponse = httpResponse.getResultAsString();
                parseAndDisplayScores(jsonResponse);
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("ScoreScreen", "Erro ao buscar dados: " + t.getMessage());
                displayErrorMessage();
            }

            @Override
            public void cancelled() {
                Gdx.app.log("ScoreScreen", "Requisição cancelada");
            }
        });
    }

    private void parseAndDisplayScores(String jsonResponse) {
        Gdx.app.postRunnable(() -> {
            try {
                Json json = new Json();
                @SuppressWarnings("unchecked")
                Map<String, Object> data = json.fromJson(HashMap.class, jsonResponse);

                if (data.containsKey("values")) {
                    @SuppressWarnings("unchecked")
                    com.badlogic.gdx.utils.Array<com.badlogic.gdx.utils.Array<String>> values = (com.badlogic.gdx.utils.Array<com.badlogic.gdx.utils.Array<String>>) data.get("values");

                    int position = 1;
                    for (com.badlogic.gdx.utils.Array<String> row : values) {
                        if (row.size >= 2) {
                            Label posLabel = new Label(String.valueOf(position), skin);
                            posLabel.setFontScale(Constants.INPUT_FONT_SCALE);

                            Label nameLabel = new Label(row.get(0), skin);
                            nameLabel.setFontScale(Constants.INPUT_FONT_SCALE);

                            Label pointsLabel = new Label(row.get(1), skin);
                            pointsLabel.setFontScale(Constants.INPUT_FONT_SCALE);

                            scoreTable.add(posLabel).pad(8);
                            scoreTable.add(nameLabel).pad(8);
                            scoreTable.add(pointsLabel).pad(8);
                            scoreTable.row();
                            position++;
                        }
                    }
                }
            } catch (Exception e) {
                Gdx.app.error("ScoreScreen", "Erro ao processar JSON: " + e.getMessage());
                displayErrorMessage();
            }
        });
    }



    private void displayErrorMessage() {
        Gdx.app.postRunnable(() -> {
            Label errorLabel = new Label("Erro ao carregar ranking", skin);
            errorLabel.setFontScale(Constants.FEEDBACK_FONT_SCALE);
            scoreTable.add(errorLabel).colspan(3).pad(20);
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        background.dispose();
    }
}
