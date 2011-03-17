package states;

import java.util.Iterator;
import java.util.Vector;

import main.JCurve;
import main.NetworkConstants;
import main.Player;
import main.PlayerProperties;
import main.client.CurveClient;
import main.powerup.Powerup;
import main.server.CurveServer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import utils.ResourceManager;

/**
 * Die Hauptstate des Spiels. Hier läuft das eigentliche Spiel ab. Die Spieler
 * steuern ihre Schlange durch die Spielwelt und müssen versuchen, anderen
 * Spielern sowie der Wand auszuweichen. Ab und an erscheinen Powerups, mit
 * welchen der Spieler entweder einen vorübergehenden Boost bekommt, bzw. drei
 * mal schießen kann.
 * 
 * Da jeder Client auch ein Server sein kann, laufen hier grundsätzlich beide
 * Berechnungen ab. Der "Serverspieler" schickt dann die berechneten Koordinaten
 * an alle Spieler.
 * 
 * @author Adam
 * @author Benedikt
 * @author Benjamin
 */
public class GameState extends JCurveState {

	private CurveServer curveServer;

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

		ResourceManager.addImage("laser", "data/images/laser.png");
		ResourceManager.addImage("bullet", "data/images/shot2.png");
		ResourceManager.addImage("puShot", "data/images/puShot.png");
		ResourceManager.addImage("puBoost", "data/images/puBoost.png");
		ResourceManager.addImage("shotThumb", "data/images/shotThumb.png");
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);

		if (JCurve.server != null)
			curveServer = JCurve.server;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);

		// TODO der client muss die powerups auch rendern! aber nur der server
		// darf sie spawnen, bzw. berechnen

		if (curveServer != null) {
			Iterator<Player> players = curveServer.getPlayerCons().values().iterator();
			while (players.hasNext()) {
				Player p = players.next();
				p.render(g);
			}
			for (int i = 0; i < Powerup.getPowerups().size(); i++) {
				Powerup.getPowerups().get(i).render();
			}
		} else {
			Vector<PlayerProperties> playerProperties = CurveClient.getInstance().getPlayerProperties();
			for (int i = 0; i < playerProperties.size(); i++) {
				PlayerProperties pp = playerProperties.get(i);
				pp.render(g);
			}
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		updateClientLogic(delta);

		if (curveServer != null)
			updateServerLogic(delta);
	}

	/**
	 * Update-Schleife des Clients, in welcher er Tastenbefehle an den Server
	 * schickt.
	 * 
	 * @param delta
	 *            - ms seit letztem Update
	 */
	private void updateClientLogic(int delta) {
		// Bewegungen
		if (container.getInput().isKeyDown(Input.KEY_LEFT) || container.getInput().isKeyDown(Input.KEY_A))
			getClient().sendUDP(NetworkConstants.PLAYER_MOVE_LEFT);
		else if (container.getInput().isKeyDown(Input.KEY_RIGHT) || container.getInput().isKeyDown(Input.KEY_D))
			getClient().sendUDP(NetworkConstants.PLAYER_MOVE_RIGHT);
		else
			getClient().sendUDP(NetworkConstants.PLAYER_MOVE_STRAIGHT);

		// Schießen
		if (container.getInput().isKeyPressed(Input.KEY_SPACE))
			getClient().sendUDP(NetworkConstants.PLAYER_SHOOT);

		// Boost
		if (container.getInput().isKeyDown(Input.KEY_LSHIFT)) {
			getClient().sendUDP(NetworkConstants.PLAYER_BOOST_ENABLE);
			playerBoost = true;
		} else {
			if (playerBoost) {
				getClient().sendUDP(NetworkConstants.PLAYER_BOOST_DISABLE);
				playerBoost = false;
			}
		}
	}

	/**
	 * Update-Schleife des Servers. Er schickt die Koordinaten an alle Spieler
	 * und sorgt dafür, dass Powerups zufällig auf der Karte erscheinen.
	 * 
	 * TODO: das darf nur der server machen und muss es an die clients
	 * schickenmomentan berechnet jeder client das für sich selber inkls. spawnt
	 * eigene poweurps.
	 * 
	 * @param delta
	 *            - ms seit letztem Update
	 */
	private void updateServerLogic(int delta) {
		playerCurDelta += delta;
		if (playerCurDelta > playerDelta - loopDuration) {
			loopDuration = System.currentTimeMillis();
			playerCurDelta = 0;
			Iterator<Player> players = curveServer.getPlayerCons().values().iterator();
			while (players.hasNext()) {
				Player p = players.next();
				p.update(delta);
				if (!p.move()) {
					p.die();
				}
			}
			curveServer.sendAllPlayerCoordinates();
			loopDuration = System.currentTimeMillis() - loopDuration;
		}

		// TODO: server-client richtig umsetzen
		Powerup.powerupSpawner();
		for (int i = 0; i < Powerup.getPowerups().size(); i++) {
			Powerup.getPowerups().get(i).update(delta);
		}
	}

}