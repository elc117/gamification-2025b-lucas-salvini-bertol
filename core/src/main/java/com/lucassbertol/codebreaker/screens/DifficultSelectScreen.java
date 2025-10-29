package com.lucassbertol.codebreaker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lucassbertol.codebreaker.MainGame;


public class DifficultSelectScreen implements Screen {

	private final SpriteBatch batch;
	private final Texture background;
	private final BitmapFont font;
	private final GlyphLayout layout;
	private final Stage stage;
	private final Skin skin;
	

	   public DifficultSelectScreen(MainGame game) {
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("selectFase.png"));

        font = new BitmapFont();
        font.getData().setScale(4f);
        layout = new GlyphLayout(font, "Escolha a dificuldade");

        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        float larguraBotao = 200f;
        float alturaBotao = 80f;
        float espacamento = 40f; // espaço entre os botões

        // Calcule a posição inicial para centralizar os dois botões juntos
        float larguraTotal = (2 * larguraBotao) + espacamento;
        float xInicial = (Gdx.graphics.getWidth() - larguraTotal) / 2;
        float y = 200f; // altura dos botões	

        TextButton easyButton = new TextButton("Facil", skin);
        easyButton.setSize(larguraBotao, alturaBotao);
        easyButton.setPosition(xInicial, y);
        easyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // start jogo fácil
            }
        });
        stage.addActor(easyButton);

        TextButton hardButton = new TextButton("Difícil", skin);
        hardButton.setSize(larguraBotao, alturaBotao);
        hardButton.setPosition(xInicial + larguraBotao + espacamento, y);
        hardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // start jogo difícil
            }
        });
        stage.addActor(hardButton);
    }

	@Override
	public void show() { 
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		float x = (Gdx.graphics.getWidth() - layout.width) / 2;
		float y = Gdx.graphics.getHeight() - 50; 
		font.draw(batch, layout, x, y);

		batch.end();

		stage.act(delta);
		stage.draw();

	}

	@Override
	public void resize(int width, int height) { }

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void hide() { }

	@Override
	public void dispose() {
		batch.dispose();
		background.dispose();
		stage.dispose();
		skin.dispose();
	}
}
