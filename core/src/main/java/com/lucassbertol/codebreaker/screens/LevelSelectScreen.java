package com.lucassbertol.codebreaker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lucassbertol.codebreaker.MainGame;


public class LevelSelectScreen implements Screen {

	private final SpriteBatch batch;
	private final Texture background;

	public LevelSelectScreen(MainGame game) {
		batch = new SpriteBatch();
		background = new Texture(Gdx.files.internal("selectFase.png"));
	}

	@Override
	public void show() { }

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
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
	}
}
