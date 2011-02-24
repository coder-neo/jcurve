package main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class JCurve extends StateBasedGame {

	public JCurve(String name) {
		super(name);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {

	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new JCurve(GameConstants.APP_NAME), GameConstants.APP_WIDHT, GameConstants.APP_HEIGHT, GameConstants.APP_IS_FULLSCREEN);
		app.setVSync(true);
		app.setTargetFrameRate(60);
		app.start();
	}

}
