package states;

import gui.GUIChat;

import main.GameConstants;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class LobbyState extends JCurveState {

	private GUIChat chatGUI = null;

	public LobbyState(int id) {
		super(id);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		chatGUI = new GUIChat(0, GameConstants.APP_HEIGHT - 300, 300, 300);
		// TODO: render GUIs
	}

}
