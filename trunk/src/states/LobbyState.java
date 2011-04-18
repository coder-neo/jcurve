package states;

import gui.GUIButton;
import gui.GUIChat;
import gui.GUIPlayerList;
import gui.GUITextField;

import java.util.Date;

import main.GameConstants;
import main.JCurve;
import main.Player;
import main.PlayerProperties;
import main.client.CurveClient;
import main.server.CurveServer;

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

	public static final String MSG_PLAYER_CONNECTED = "%s hat sich dem Spiel angeschlossen";
	public static final String MSG_PLAYER_DISCONNECTED = "%s hat uns verlassen";
	public static final String MSG_WAIT = "Warte auf Mitspieler";
	public static final String MSG_WAIT_DOTS = "...";

	public static final int TEXTFIELD_HEIGHT = 24;
	public static final int MIN_PLAYERS_TO_PLAY = 2;

	private GUIChat chatGUI = null;
	private GUIPlayerList playerList = null;
	private GUITextField chatField = null;
	private GUIButton buttonPlay = null, buttonCancel = null;

	private int dotDelta = 0;
	private int maxDotDelta = 500;
	private int curDotPos = 1;

	public LobbyState(int id) {
		super(id);
	}

	@Override
	public void init(GameContainer container, final StateBasedGame game) throws SlickException {
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
				enterChatMessage(e);
			}
		});

		buttonPlay = new GUIButton("Spiel starten", container, GameConstants.APP_WIDHT / 2 - 100, GameConstants.APP_HEIGHT - 100);
		buttonPlay.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent source) {
				game.enterState(GameConstants.STATE_GAME);
			}
		});

		buttonCancel = new GUIButton("Abbrechen", container, GameConstants.APP_WIDHT / 2 + 100, GameConstants.APP_HEIGHT - 100);
		buttonCancel.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent source) {
				game.enterState(GameConstants.STATE_MAIN_MENU);
			}
		});

		addGUIElements(chatGUI, playerList, buttonPlay, buttonCancel);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);

		if (JCurve.createServer && JCurve.server == null) {
			JCurve.server = new CurveServer();
			CurveClient.getInstance().connect("localhost");
		}
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);

		JCurve.createServer = false;

		if (JCurve.server != null) {
			chatGUI.clear();
			playerList.clear();
		} else {
			CurveClient.getInstance().getClient().close();
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		super.update(container, game, delta);

		updateLobby();

		playerList.updatePlayerVector(CurveClient.getInstance().getPlayerProperties());

		if (CurveClient.getInstance().getPlayerProperties().size() < MIN_PLAYERS_TO_PLAY) {
			buttonPlay.setEnabled(false);
			dotDelta += delta;
			if (dotDelta >= maxDotDelta) {
				dotDelta = 0;
				curDotPos++;
				if (curDotPos > 3)
					curDotPos = 1;
			}
		} else {
			if (!buttonPlay.isEnabled())
				buttonPlay.setEnabled(true);
		}

		if (container.getInput().isKeyPressed(Input.KEY_ENTER) && !chatField.hasFocus())
			chatField.setFocus(true);

		if (container.getInput().isKeyPressed(Input.KEY_F1)) {
			Player p = new Player();
			p.getProperties().setName(new Date().getTime() + "");
			addPlayer(p.getProperties());
		} else if (container.getInput().isKeyPressed(Input.KEY_F2)) {
			removePlayer(0);
		}
	}

	private void updateLobby() {
		playerList.updatePlayerVector(CurveClient.getInstance().getPlayerProperties());
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);

		if (CurveClient.getInstance().getPlayerProperties().size() < MIN_PLAYERS_TO_PLAY) {
			int strWidth = ResourceManager.getFont("standard").getWidth(MSG_WAIT) / 2;
			ResourceManager.getFont("standard").drawString(GameConstants.APP_WIDHT / 2 - strWidth, GameConstants.APP_HEIGHT / 2, MSG_WAIT + " " + MSG_WAIT_DOTS.substring(0, curDotPos));
		}

		chatField.render(container, g);
	}

	/**
	 * Trägt eine neue Chatnachricht eines Spielers ein.
	 * 
	 * TODO: richtigen namen rausfinden und benutzen und an server schicken
	 * 
	 * @param e
	 *            - das TextField, bzw. Eingabefeld in der Lobby
	 */
	private void enterChatMessage(AbstractComponent e) {
		GUITextField field = (GUITextField) e;
		String playerName = null;

		if (JCurve.userData.getName().trim().equals("")) {
			playerName = "anonym";
		} else {
			playerName = JCurve.userData.getName();
		}

		chatGUI.addMessage(playerName, field.getText());
		field.setText("");
	}

	/**
	 * Entfernt einen Spieler aus der Lobby anhand des Vector-Index.
	 * 
	 * @param index
	 *            - der Index
	 */
	public void removePlayer(int index) {
		removePlayer(CurveClient.getInstance().getPlayerProperties().get(index));
//		if (!CurveClient.getInstance().getPlayerProperties().contains(CurveClient.getInstance().getPlayerProperties().get(index)))
//			return;
//
//		String name = CurveClient.getInstance().getPlayerProperties().get(index).getName();
//		CurveClient.getInstance().getPlayerProperties().remove(index);
//		chatGUI.addSystemMessage(MSG_PLAYER_DISCONNECTED.replace("%s", name));
	}

	/**
	 * Entfernt einen Spieler aus der Lobby anhand des Player-Objektes
	 * 
	 * @param p
	 *            - das Objekt
	 */
	public void removePlayer(PlayerProperties p) {
		if (p == null) {
			return;
		}
		String name = p.getName();
		chatGUI.addSystemMessage(MSG_PLAYER_DISCONNECTED.replace("%s", name));
//		for (int i = 0; i < CurveClient.getInstance().getPlayerProperties().size(); i++) {
//			if (CurveClient.getInstance().getPlayerProperties().get(i).equals(p)) {
//				removePlayer(i);
//				return;
//			}
//		}
	}

	/**
	 * Registriert einen neuen Spieler in dieser Lobby.
	 * 
	 * @param p
	 *            - das Spielerobjekt
	 */
	public void addPlayer(PlayerProperties p) {
		chatGUI.addSystemMessage(MSG_PLAYER_CONNECTED.replace("%s", p.getName()));
	}
}
