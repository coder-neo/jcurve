package states;

import java.util.Iterator;
import java.util.Vector;

import main.NetworkConstants;
import main.Player;
import main.PlayerOptions;
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

public class GameState extends JCurveState {

	private CurveServer curveServer;
	private int playerDelta = 30;
	private int playerCurDelta = 0;
	
	private boolean playerBoost = false;

	private long loopDuration;

	public GameState(int id) {
		super(id);
		curveServer = new CurveServer();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		ResourceManager.addImage("laser", "data/images/laser.png");
		ResourceManager.addImage("bullet", "data/images/shot2.png");
		ResourceManager.addImage("puShot", "data/images/puShot.png");
		ResourceManager.addImage("puBoost", "data/images/puBoost.png");
		ResourceManager.addImage("shotThumb", "data/images/shotThumb.png");
		super.init(container, game);
		getClient().sendUDP(new PlayerOptions("adam", "ff0000"));
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);
		if (curveServer != null) {
			Iterator<Player> players = curveServer.getPlayerCons().values().iterator();
			while (players.hasNext()) {
				Player p = players.next();
				p.render(g);
			}
			for (int i = 0; i < Powerup.getPowerups().size(); i++){
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
		// ------------------------- Client Eingaben ----------------------------
		if (container.getInput().isKeyDown(Input.KEY_LEFT) || container.getInput().isKeyDown(Input.KEY_A)) {
			getClient().sendUDP(NetworkConstants.PLAYER_MOVE_LEFT);
		} else if (container.getInput().isKeyDown(Input.KEY_RIGHT) || container.getInput().isKeyDown(Input.KEY_D)) {
			getClient().sendUDP(NetworkConstants.PLAYER_MOVE_RIGHT);
		} else {
			getClient().sendUDP(NetworkConstants.PLAYER_MOVE_STRAIGHT);
		}
		if (container.getInput().isKeyDown(Input.KEY_LSHIFT)) {
			getClient().sendUDP(NetworkConstants.PLAYER_BOOST_ENABLE);
			playerBoost = true;
		} else {
			if (playerBoost) {
				getClient().sendUDP(NetworkConstants.PLAYER_BOOST_DISABLE);
				playerBoost = false;
			}
		}
		if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			getClient().sendUDP(NetworkConstants.PLAYER_SHOOT);
		}

		// ----------------------- Serverberechnungen ---------------------------
		playerCurDelta += delta;
		if (playerCurDelta > playerDelta - loopDuration) {
			loopDuration = System.currentTimeMillis();
			playerCurDelta = 0;
			if (curveServer != null) {
				Iterator<Player> players = curveServer.getPlayerCons().values().iterator();
				while (players.hasNext()) {
					Player p = players.next();
					p.update(delta);
					if (!p.move()){
						p.die();
					}
				}
				curveServer.sendAllPlayerCoordinates();
			}
			loopDuration = System.currentTimeMillis() - loopDuration;
		}
		Powerup.powerupSpawner();
		for (int i = 0; i < Powerup.getPowerups().size(); i++){
			Powerup.getPowerups().get(i).update(delta);
		}
	}

}