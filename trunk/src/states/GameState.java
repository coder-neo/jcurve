package states;

import java.util.Iterator;

import main.NetworkConstants;
import main.Player;
import main.PlayerOptions;
import main.server.CurveServer;

import org.lwjgl.Sys;
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
	
	private long loopDuration;

	public GameState(int id) {
		super(id);
		curveServer = new CurveServer();
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		getClient().sendUDP(new PlayerOptions("adam", "ff0000"));
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);
		ResourceManager.getFont("chatFont").drawString(300, 300, "Hallo, dies ist eine Chatnachricht! XD");
		ResourceManager.getFont("chatFont").drawString(300, 324, "Hallo, dies ist eine zweite möp möp Chatnachricht! XD");
		Iterator<Player> players = curveServer.getPlayerCons().values().iterator();
		g.setLineWidth(3);
		while (players.hasNext()){
			Player p = players.next();
			g.setColor(p.getColor());
			for (int i = 0; i < p.getPoints().size() - 1; i++){
				g.drawLine(p.getPoints().get(i).x, p.getPoints().get(i).y, p.getPoints().get(i+1).x, p.getPoints().get(i+1).y);
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
		
		// -----------------------   Serverberechnungen  ---------------------------
		playerCurDelta += delta;
		if (playerCurDelta > playerDelta - loopDuration) {
			loopDuration = System.currentTimeMillis();
			playerCurDelta = 0;
			if (curveServer != null) {
				Iterator<Player> players = curveServer.getPlayerCons().values().iterator();
				while (players.hasNext()){
					Player p = players.next();
					p.move();
				}
				curveServer.sendAllPlayerCoordinates();
			}
			loopDuration = System.currentTimeMillis() - loopDuration;
		}
	}

}
