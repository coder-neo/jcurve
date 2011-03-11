package main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import states.GameState;
import states.OptionsState;

public class JCurve extends StateBasedGame {

	public static AppGameContainer app = null;

	public JCurve(String name) {
		super(name);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new OptionsState(GameConstants.STATE_OPTIONS));
		addState(new GameState(GameConstants.STATE_GAME));
//		addState(new MainMenuState(GameConstants.STATE_MAIN_MENU));
//		addState(new LobbyState(GameConstants.STATE_LOBBY));

		enterState(GameConstants.STATE_GAME);
	}

	public static void main(String[] args) throws SlickException {
		app = new AppGameContainer(new JCurve(GameConstants.APP_NAME), GameConstants.APP_WIDHT, GameConstants.APP_HEIGHT, GameConstants.APP_IS_FULLSCREEN);
		app.setVSync(true);
		app.setShowFPS(false);
		app.setTargetFrameRate(60);
		app.start();
	}
}
