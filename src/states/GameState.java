package states;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import main.NetworkConstants;
import main.Player;
import main.PlayerOptions;
import main.PlayerPoint;
import main.client.CurveClient;
import main.server.CurveServer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import utils.ResourceManager;

public class GameState extends JCurveState {

	private CurveServer curveServer;
	private int playerDelta = 30;
	private int playerCurDelta = 0;

	private long loopDuration;

	public GameState(int id) {
		super(id);
		curveServer = new CurveServer();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		ResourceManager.addImage("laser", "data/images/laser.png");
		getClient().sendUDP(new PlayerOptions("adam", "ff0000"));
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);
		if (curveServer != null) {
			Iterator<Player> players = curveServer.getPlayerCons().values().iterator();
			while (players.hasNext()) {
				Player p = players.next();
				Color color = new Color(p.getProperties().getColorCode());
				Image tmpImg;
				for (int i = 0; i < p.getProperties().getPoints().size() - 1; i++) {
					tmpImg = ResourceManager.getImage(p.getProperties().getImageKey()).copy();
					tmpImg.setRotation((float) Math.toDegrees(p.getProperties().getPoints().get(i).getAngle()));
					g.drawImage(tmpImg, p.getProperties().getPoints().get(i).x, p.getProperties().getPoints().get(i).y, color);
				}
			}
		} else {
			HashMap<Integer, Vector<PlayerPoint>> coordinates = CurveClient.getInstance().getCoordinates();
			for (int i = 0; i < coordinates.size(); i++) {
				
			}
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			getClient().sendUDP(NetworkConstants.GAME_START);
		}
		if (container.getInput().isKeyDown(Input.KEY_LEFT) || container.getInput().isKeyDown(Input.KEY_A)) {
			getClient().sendUDP(NetworkConstants.PLAYER_MOVE_LEFT);
		} else if (container.getInput().isKeyDown(Input.KEY_RIGHT) || container.getInput().isKeyDown(Input.KEY_D)) {
			getClient().sendUDP(NetworkConstants.PLAYER_MOVE_RIGHT);
		} else {
			getClient().sendUDP(NetworkConstants.PLAYER_MOVE_STRAIGHT);
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
					p.move();
				}
				curveServer.sendAllPlayerCoordinates();
			}
			loopDuration = System.currentTimeMillis() - loopDuration;
		}
	}

}
