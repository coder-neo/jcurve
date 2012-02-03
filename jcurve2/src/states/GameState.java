package states;

import java.util.HashMap;
import java.util.Vector;

import main.JCurve;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import shared.ConnectedPlayer;
import shared.GameCommand;
import shared.GameConstants;
import shared.NetworkConstants;
import utils.ResourceManager;
import client.CurveClient;
import client.Player;
import client.PlayerPoint;

/**
 * Die Hauptstate des Spiels. Hier läuft das eigentliche Spiel ab. Die Spieler steuern ihre Schlange durch die Spielwelt und müssen versuchen, anderen Spielern sowie der Wand auszuweichen. Ab und an erscheinen Powerups, mit welchen der Spieler entweder einen vorübergehenden Boost bekommt, bzw. drei mal schießen kann.
 * 
 * Da jeder Client auch ein Server sein kann, laufen hier grundsätzlich beide Berechnungen ab. Der "Serverspieler" schickt dann die berechneten Koordinaten an alle Spieler.
 * 
 * @author Adam
 * @author Benedikt
 * @author Benjamin
 */
public class GameState extends JCurveState {

	private StateBasedGame stateBasedGame = null;

	private int playerDelta = 30;
	private int playerCurDelta = 0;

	private boolean playerBoost = false;

	private long loopDuration;

	public GameState(int id) {
		super(id);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		stateBasedGame = game;

		ResourceManager.addImage("laser", "data/images/laser.png");
		ResourceManager.addImage("bullet", "data/images/shot2.png");
		ResourceManager.addImage("puShot", "data/images/puShot.png");
		ResourceManager.addImage("puBoost", "data/images/puBoost.png");
		ResourceManager.addImage("shotThumb", "data/images/shotThumb.png");
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);

		JCurve.runningGameState = this;

		if (JCurve.server != null) {
			System.out.println("[SERVER] I'm the server!");
			for (int i = 0; i < JCurve.server.getConnectedPlayers().size(); i++) {
				ConnectedPlayer connectedPlayer = JCurve.server.getConnectedPlayers().get(i);
				Player player = new Player(connectedPlayer);
				player.initPlayerPosition();
				System.out.println("[SERVER] Created player: " + connectedPlayer.getProperties().getName());
			}
		} else {
			System.out.println("[CLIENT] I'm just a client!");
			for (int i = 0; i < CurveClient.getInstance().getConnectedPlayers().size(); i++) {
				ConnectedPlayer connectedPlayer = CurveClient.getInstance().getConnectedPlayers().get(i);
				new Player(connectedPlayer);
			}
		}
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);

		resetGame();

		if (JCurve.server != null) {
			System.out.println("[SERVER] I'm shutting down, the game is over.");
			JCurve.server.shutdown();
			JCurve.server = null;
		}

		JCurve.runningGameState = null;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);

		for (int i = 0; i < Player.getPlayers().size(); i++) {
			Player.getPlayers().get(i).render(g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		updateClientLogic(delta);

		if (JCurve.server != null) {
			updateServerLogic(delta);
		}
	}

	/**
	 * Empfängt einen Befehl vom Netzwerk und leitet ihn entsprechend weiter, je nachdem ob die Instanz Server/Client ist.
	 * 
	 * @param cmd
	 *            - der Befehl, der ausgeführt werden soll
	 */
	public void parseCommand(GameCommand cmd) {
		if (JCurve.server != null) {
			serverParseCommand(cmd);
		} else {
			clientParseCommand(cmd);
		}
	}

	/**
	 * Setzt Befehle um, die NUR für den Client bestimmt sind und vom Server ausgehen.
	 * 
	 * @param cmd
	 *            - der Befehl des Servers
	 */
	private void clientParseCommand(GameCommand cmd) {
		System.out.println("[CLIENT] Received cmd: " + cmd.getCommand());

		switch (cmd.getCommand()) {
		case NetworkConstants.GAME_END:
			stopGame();
			break;
		}
	}

	/**
	 * Setzt Befehle um, die NUR für den Server bestimmt sind und von einem Client ausgehen.
	 * 
	 * @param cmd
	 *            - der Befehl von einem Client
	 */
	private void serverParseCommand(GameCommand cmd) {
		Player sendingPlayer = null;
		for (int i = 0; i < Player.getPlayers().size(); i++) {
			Player curPlayer = Player.getPlayers().get(i);
			if (curPlayer.getOwnerConnectedPlayer().getConnectionID() == cmd.getConnectionID()) {
				sendingPlayer = curPlayer;
				break;
			}
		}

		if (sendingPlayer != null) {
			switch (cmd.getCommand()) {
			case NetworkConstants.PLAYER_MOVE_STRAIGHT:
				sendingPlayer.steerStraight();
				break;
			case NetworkConstants.PLAYER_MOVE_LEFT:
				sendingPlayer.steerLeft();
				break;
			case NetworkConstants.PLAYER_MOVE_RIGHT:
				sendingPlayer.steerRight();
				break;
			case NetworkConstants.PLAYER_BOOST_ENABLE:
				sendingPlayer.setBoost(true);
				break;
			case NetworkConstants.PLAYER_BOOST_DISABLE:
				sendingPlayer.setBoost(false);
				break;
			case NetworkConstants.PLAYER_SHOOT:
				sendingPlayer.shoot();
				break;
			}
		}

		sendCoordinates();
	}

	/**
	 * Sendet die Koordinaten der Spieler an alle Spieler.
	 */
	private void sendCoordinates() {
		HashMap<Integer, PlayerPoint> newPoints = new HashMap<Integer, PlayerPoint>();
		for (int i = 0; i < Player.getPlayers().size(); i++) {
			Player curPlayer = Player.getPlayers().get(i);
			newPoints.put(curPlayer.getOwnerConnectedPlayer().getConnectionID(), curPlayer.getProperties().getPoints().lastElement());
		}
		sendUDP(newPoints);
	}

	/**
	 * Setzt die aktuelle Runde zurück.
	 */
	private void resetGame() {
		Player.getPlayers().removeAllElements();
	}

	/**
	 * Beendet das Spiel und wechselt die State.
	 */
	private void stopGame() {
		// TODO: highscore oder so
		stateBasedGame.enterState(GameConstants.STATE_MAIN_MENU, new FadeOutTransition(), new FadeInTransition());
	}

	/**
	 * Updateloop des Clients. Hier werden Befehle an den Server geschickt.
	 * 
	 * @param delta
	 *            - ms seit letztem Update
	 */
	private void updateClientLogic(int delta) {
		GameCommand cmd = new GameCommand();
		cmd.setConnectionID(CurveClient.getInstance().getClient().getID());

		// Bewegungen
		if (container.getInput().isKeyDown(Input.KEY_LEFT) || container.getInput().isKeyDown(Input.KEY_A)) {
			cmd.setCommand(NetworkConstants.PLAYER_MOVE_LEFT);
		} else if (container.getInput().isKeyDown(Input.KEY_RIGHT) || container.getInput().isKeyDown(Input.KEY_D)) {
			cmd.setCommand(NetworkConstants.PLAYER_MOVE_RIGHT);
		} else {
			cmd.setCommand(NetworkConstants.PLAYER_MOVE_STRAIGHT);
		}

		// Schießen
		if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			cmd.setCommand(NetworkConstants.PLAYER_SHOOT);
		}

		// Boost
		if (container.getInput().isKeyDown(Input.KEY_LSHIFT)) {
			cmd.setCommand(NetworkConstants.PLAYER_BOOST_ENABLE);
			playerBoost = true;
		} else {
			if (playerBoost) {
				cmd.setCommand(NetworkConstants.PLAYER_BOOST_DISABLE);
				playerBoost = false;
			}
		}

		getClient().sendTCP(cmd);
	}

	/**
	 * Updateloop des Servers. Hier werden die Spieler/Powerups/etc berechnet.
	 * 
	 * @param delta
	 *            - ms seit letztem Update
	 */
	private void updateServerLogic(int delta) {
		boolean allPlayersDead = true;

		for (int i = 0; i < Player.getPlayers().size(); i++) {
			Player curPlayer = Player.getPlayers().get(i);
			curPlayer.update(delta);
			if (!curPlayer.move()) {
				curPlayer.die();
			}

			if (curPlayer.isAlive()) {
				allPlayersDead = false;
			}
		}

		if (allPlayersDead) {
			sendTCP(new GameCommand(NetworkConstants.GAME_END));
			stopGame();
		}
	}

	/**
	 * Sendet ein Objekt an alle Clients per TCP.
	 * 
	 * @param object
	 */
	private void sendTCP(Object object) {
		JCurve.server.getKryonetServer().sendToAllTCP(object);
	}

	/**
	 * Sendet ein Objekt an alle Clients per UDP.
	 * 
	 * @param object
	 */
	private void sendUDP(Object object) {
		JCurve.server.getKryonetServer().sendToAllUDP(object);
	}

	public void updatePoints(HashMap<Integer, PlayerPoint> newPoints) {
		Vector<Player> players = Player.getPlayers();
		for (int j = 0; j < players.size(); j++) {
			PlayerPoint pp = newPoints.get(players.get(j).getConnection().getID());
			players.get(j).getProperties().getPoints().add(pp);
		}
	}
}