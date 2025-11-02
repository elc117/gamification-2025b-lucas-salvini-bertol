package com.lucassbertol.codebreaker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lucassbertol.codebreaker.MainGame;

public class MenuScreen implements Screen {

    private final SpriteBatch batch;
    private final Texture background;
    private final BitmapFont font;
    private final OrthographicCamera camera;
    private final MainGame game;

    // GlyphLayout nos ajuda a medir o tamanho do texto para centralizá-lo
    private final GlyphLayout layout;
    private final String message = "PRESS START";

    public MenuScreen(MainGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        // Ajusta a câmera para o tamanho da tela do dispositivo
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();

        // Carrega a imagem de fundo da pasta assets
        background = new Texture("images/menu.png");

        // Usa a fonte padrão do libGDX.
        font = new BitmapFont();
        font.getData().setScale(3f); // Aumenta o tamanho da fonte

        layout = new GlyphLayout(font, message);
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

        // Desenha a imagem de fundo para preencher a tela
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Calcula a posição para centralizar o texto
        float textX = (Gdx.graphics.getWidth() - layout.width) / 2;
        float textY = (Gdx.graphics.getHeight() + layout.height) / 2;

        // Desenha o texto
        font.draw(batch, message, textX, textY);

        batch.end();

        // Verifica se o usuário tocou na tela
        if (Gdx.input.justTouched()) {
            game.setScreen(new UserInputScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        // Atualiza a câmera quando a janela é redimensionada
        camera.setToOrtho(false, width, height);
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
    }
}
