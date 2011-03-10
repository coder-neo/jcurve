package states;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import main.NetworkConstants;
import main.Player;
import main.PlayerOptions;
import main.PlayerPoint;
import main.PlayerProperties;
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
		Image tmpImg;
		if (curveServer != null) {
			Iterator<Player> players = curveServer.getPlayerCons().values().iterator();
			while (players.hasNext()) {
				Player p = players.next();
				Color color = new Color(p.getProperties().getColorCode());
				for (int i = 0; i < p.getProperties().getPoints().size() - 1; i++) {
					tmpImg = ResourceManager.getImage(p.getProperties().getImageKey()).copy();
					tmpImg.setRotation((float) Math.toDegrees(p.getProperties().getPoints().get(i).getAngle()));
					g.drawImage(tmpImg, p.getProperties().getPoints().get(i).x, p.getProperties().getPoints().get(i).y, color);
				}
			}
		} else {
			HashMap<Integer, Vector<PlayerPoint>> coordinates = CurveClient.getInstance().getCoordinates();
			Iterator<Integer> conIDs = coordinates.keySet().iterator();
			while(conIDs.hasNext()){
				int conID = conIDs.next();
//				tmpImg = ResourceManager.getImage(p.getProperties().getImageKey()).copy();
//				tmpImg.setRotation((float) Math.toDegrees(p.getProperties().getPoints().get(i).getAngle()));
//				g.drawImage(tmpImg, p.getProperties().getPoints().get(i).x, p.getProperties().getPoints().get(i).y, color);
				for (int i = 0; i < coordinates.get(conID).size(); i++){
					PlayerPoint pp = coordinates.get(conID).get(i);
					g.fillOval((float)pp.getX(), (float)pp.getY(), 5, 5);
			Vector<PlayerProperties> playerProperties = CurveClient.getInstance().getPlayerProperties();
			for (int i = 0; i < playerProperties.size(); i++){
				PlayerProperties pp = playerProperties.get(i);
				Color color = new Color(pp.getColorCode());
				for (int j = 0; j < pp.getPoints().size(); j++){
					tmpImg = ResourceManager.getImage(pp.getImageKey()).copy();
					tmpImg.setRotation((float) Math.toDegrees(pp.getPoints().get(j).getAngle()));
					g.drawImage(tmpImg, pp.getPoints().get(j).x, pp.getPoints().get(j).y, color);
				}
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
