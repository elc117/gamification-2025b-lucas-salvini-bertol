package com.lucassbertol.codebreaker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lucassbertol.codebreaker.MainGame;

public class MenuScreen implements Screen {

    private final SpriteBatch batch;
    private final Texture background;
    private final BitmapFont font;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final MainGame game;
    private final Skin skin;

    // GlyphLayout nos ajuda a medir o tamanho do texto para centralizá-lo
    private final GlyphLayout layout;
    private final String message = "PRESS START";

    public MenuScreen(MainGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        // FitViewport com resolução virtual 1280x720 para manter proporções
        viewport = new FitViewport(1920, 1080, camera);
        camera.position.set(1920 / 2f, 1080 / 2f, 0);

        batch = new SpriteBatch();

        // Carrega a imagem de fundo da pasta assets
        background = new Texture("images/menu.png");

        // Usa a fonte do skin para melhor aparência
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        font = skin.getFont("default-font");
        font.getData().setScale(3.0f); // Aumenta o tamanho da fonte

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

        // Desenha a imagem de fundo para preencher a viewport virtual (1920x1080)
        batch.draw(background, 0, 0, 1920, 1080);

        // Calcula a posição para centralizar o texto
        float textX = (1920 - layout.width) / 2;
        float textY = (1080 + layout.height) / 2;

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
        // Atualiza o viewport quando a janela é redimensionada
        viewport.update(width, height, true);
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
        skin.dispose();
        batch.dispose();
    }
}
