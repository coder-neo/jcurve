package states;

import gui.GUIChat;
import gui.GUIPlayerList;
import gui.GUITextField;

import java.util.Vector;

import main.GameConstants;
import main.Player;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import utils.ResourceManager;

/**
 * In dieser State halten die Spieler sich auf, wenn sie bereits auf einem
 * Server sind, aber noch kein Spiel gestartet haben. Hier sieht man alle
 * Mitspieler auf einen Blick und hat einen Chat zur Kommunikation zur
 * Verfügung. Wenn alle Spieler bereit sind, startet der Server das Spiel.
 * 
 * @author Benjamin
 */
public class LobbyState extends JCurveState {

	private Vector<Player> players = new Vector<Player>();

	private GUIChat chatGUI = null;
	private GUIPlayerList playerList = null;
	private GUITextField chatField = null;

	private int dotDelta = 0;
	private int maxDotDelta = 500;
	private int curDotPos = 1;

	public static final int TEXTFIELD_HEIGHT = 24;

	public LobbyState(int id) {
		super(id);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		chatGUI = new GUIChat(0, GameConstants.APP_HEIGHT - 300, 300, 300);
		chatGUI.setBorder(Color.red);

		playerList = new GUIPlayerList(0, 0, 300, GameConstants.APP_HEIGHT - 300);
		playerList.setBorder(Color.red);

		chatField = new GUITextField(container, ResourceManager.getFont("chatFont"), 0, GameConstants.APP_HEIGHT - TEXTFIELD_HEIGHT, 298, TEXTFIELD_HEIGHT - 2);
		chatField.setBorderColor(Color.red);
		chatField.setBackgroundColor(Color.white);
		chatField.setTextColor(Color.black);
		chatField.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent e) {
				GUITextField field = (GUITextField) e;
				String playerName = "Test";
				chatGUI.addMessage(playerName, field.getText());
				field.setText("");
			}
		});

		addGUIElements(chatGUI, playerList);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		super.update(container, game, delta);

		playerList.setPlayers(players);

		dotDelta += delta;
		if (dotDelta >= maxDotDelta) {
			dotDelta = 0;
			curDotPos++;
			if (curDotPos > 3)
				curDotPos = 1;
		}

		if (container.getInput().isKeyDown(Input.KEY_ENTER) && !chatField.hasFocus()) {
			chatField.setFocus(true);
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);

		if (players.size() == 0) {
			String waitMsg = "WARTE AUF SPIELER";
			String dots = "...";
			int strWidth = ResourceManager.getFont("chatFont").getWidth(waitMsg) / 2;
			ResourceManager.getFont("chatFont").drawString(GameConstants.APP_WIDHT / 2 - strWidth, GameConstants.APP_HEIGHT / 2, waitMsg + " " + dots.substring(0, curDotPos));
		}

		chatField.render(container, g);
	}
}
