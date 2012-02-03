package states;

import gui.GUIButton;
import gui.GUIChat;
import gui.GUIPlayerList;
import gui.GUITextField;

import java.util.ArrayList;

import main.JCurve;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import server.CurveServer;
import shared.ChatMessage;
import shared.ConnectedPlayer;
import shared.GameCommand;
import shared.GameConstants;
import shared.NetworkConstants;
import utils.ResourceManager;
import client.CurveClient;

/**
 * In dieser State halten die Spieler sich auf, wenn sie bereits auf einem Server sind, aber noch kein Spiel gestartet haben. Hier sieht man alle Mitspieler auf einen Blick und hat einen Chat zur Kommunikation zur Verfügung. Wenn alle Spieler bereit sind, startet der Server das Spiel.
 * 
 * @author Benjamin
 */
public class LobbyState extends JCurveState {

	private StateBasedGame game = null;

	public static final String MSG_PLAYER_CONNECTED = "%s joined.";
	public static final String MSG_PLAYER_DISCONNECTED = "%s left.";
	public static final String MSG_WAIT = "Waiting for players";
	public static final String MSG_WAIT_DOTS = "...";

	public static final int TEXTFIELD_HEIGHT = 24;
	public static final int MIN_PLAYERS_TO_PLAY = 2;

	private ArrayList<ConnectedPlayer> connectedPlayers = new ArrayList<ConnectedPlayer>();
	private ArrayList<ChatMessage> chatMessages = new ArrayList<ChatMessage>();

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

		this.game = game;

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

		buttonPlay = new GUIButton("Start game", container, GameConstants.APP_WIDTH / 2 - 100, GameConstants.APP_HEIGHT - 100);
		buttonPlay.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent source) {
				JCurve.server.getKryonetServer().sendToAllTCP(new GameCommand(NetworkConstants.GAME_START));
				startGame();
			}
		});

		buttonCancel = new GUIButton("Cancel", container, GameConstants.APP_WIDTH / 2 + 100, GameConstants.APP_HEIGHT - 100);
		buttonCancel.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent source) {
				game.enterState(GameConstants.STATE_MAIN_MENU);
				if (JCurve.server != null) {
					JCurve.server.shutdown();
					JCurve.server = null;
				} else {
					CurveClient.getInstance().getClient().stop();
				}
			}
		});

		addGUIElements(chatGUI, playerList, buttonPlay, buttonCancel);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);

		if (JCurve.createServer && JCurve.server == null) {
			JCurve.app.setTitle(GameConstants.APP_NAME + " [SERVER]");
			JCurve.server = new CurveServer();

			// add self to server
			CurveClient.getInstance().connect(JCurve.server.getIP());
		} else {
			JCurve.app.setTitle(GameConstants.APP_NAME + " [CLIENT]");
		}
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);

		JCurve.createServer = false;
		JCurve.app.setTitle(GameConstants.APP_NAME);

		chatGUI.clear();
		playerList.clear();
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		super.update(container, game, delta);

		if (JCurve.server != null) {
			connectedPlayers = JCurve.server.getConnectedPlayers();
			chatMessages = JCurve.server.getChatMessages();
		} else {
			connectedPlayers = CurveClient.getInstance().getConnectedPlayers();
			chatMessages = CurveClient.getInstance().getChatMessages();
		}

		playerList.updateList(connectedPlayers);
		chatGUI.updateChatMessages(chatMessages);

		if (connectedPlayers.size() < MIN_PLAYERS_TO_PLAY) {
			buttonPlay.setEnabled(false);
			dotDelta += delta;
			if (dotDelta >= maxDotDelta) {
				dotDelta = 0;
				curDotPos++;
				if (curDotPos > MSG_WAIT_DOTS.length()) {
					curDotPos = 1;
				}
			}
		} else {
			if (!buttonPlay.isEnabled()) {
				buttonPlay.setEnabled(true);
			}
		}

		if (container.getInput().isKeyPressed(Input.KEY_ENTER) && !chatField.hasFocus()) {
			chatField.setFocus(true);
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);

		if (connectedPlayers.size() < MIN_PLAYERS_TO_PLAY) {
			int strWidth = ResourceManager.getFont("standard").getWidth(MSG_WAIT) / 2;
			ResourceManager.getFont("standard").drawString(GameConstants.APP_WIDTH / 2 - strWidth, GameConstants.APP_HEIGHT / 2, MSG_WAIT + " " + MSG_WAIT_DOTS.substring(0, curDotPos));
		}

		chatField.render(container, g);
	}

	/**
	 * Trägt eine neue Chatnachricht eines Spielers ein.
	 * 
	 * @param e
	 *            - das TextField, bzw. Eingabefeld in der Lobby
	 */
	private void enterChatMessage(AbstractComponent e) {
		GUITextField field = (GUITextField) e;
		CurveClient.getInstance().sendChatMessage(field.getText());

		field.clear();
	}

	/**
	 * Wechselt die State und startet somit das Spiel.
	 */
	public void startGame() {
		game.enterState(GameConstants.STATE_GAME);
	}

}
