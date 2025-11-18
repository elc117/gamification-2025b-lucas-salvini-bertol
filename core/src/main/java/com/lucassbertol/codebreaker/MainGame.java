package com.lucassbertol.codebreaker;

import com.badlogic.gdx.Game;
import com.lucassbertol.codebreaker.screens.MenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MainGame extends Game {
    public String playerName;

	@Override
	public void create() {
		// Define a tela inicial do jogo como a nossa tela de menu.
		// Passamos 'this' para que a MenuScreen possa se comunicar de volta com a classe Game
		// e pedir para trocar de tela.
		setScreen(new MenuScreen(this));
	}
        // setter chamado em UserInputScreen
        public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

        // getter usado depois (ex.: QuestionScreen)
        public String getPlayerName() {
        return playerName;
    }
}
