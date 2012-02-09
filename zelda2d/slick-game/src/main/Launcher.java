package main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import states.GameState;

public class Launcher extends StateBasedGame {

	public Launcher(String name) {
		super(name);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new GameState(GameConstants.STATE_GAME));
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Launcher(GameConstants.NAME), GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT, false);
		app.setTargetFrameRate(60);
		app.setShowFPS(false);
		app.start();
	}
}
