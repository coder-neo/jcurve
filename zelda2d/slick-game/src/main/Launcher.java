package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Bootstrap;

import states.GameState;

public class Launcher extends StateBasedGame {

	public Launcher(String name) {
		super(name);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		container.setTargetFrameRate(60);
		container.setVSync(true);

		addState(new GameState(GameConstants.STATE_GAME));
	}

	public static void main(String[] args) {
		Bootstrap.runAsApplication(new Launcher(GameConstants.NAME), 800, 600, false);
	}

}
