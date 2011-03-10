package states;

import main.GameConstants;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import utils.ResourceManager;

public class MainMenuState extends JCurveState {

	public MainMenuState(int id) {
		super(id);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);

		ResourceManager.getFont("header").drawString(100, 100, GameConstants.APP_NAME, Color.red);

		ResourceManager.getFont("standard").drawString(100, 250, "Neues Spiel starten");
		ResourceManager.getFont("standard").drawString(100, 300, "Spiel beitreten");
		ResourceManager.getFont("standard").drawString(100, 350, "Optionen");
		ResourceManager.getFont("standard").drawString(100, 450, "Beenden");

		ResourceManager.getFont("small").drawString(100, 450, GameConstants.APP_VERSION);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		super.update(container, game, delta);

	}

}
