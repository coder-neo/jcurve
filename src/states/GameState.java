package states;

import main.NetworkConstants;
import main.Player;
import main.PlayerOptions;
import main.server.CurveServer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GameState extends JCurveState {
	private CurveServer curveServer;
	
	public GameState(int id) {
		super(id);
		curveServer = new CurveServer();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			getClient().sendTCP(NetworkConstants.GAME_START);
			getClient().sendTCP(new PlayerOptions("adam", "ff0000"));
		}
		if (curveServer != null) {
			// Serverberechnungen
			while (curveServer.getPlayerCons().values().iterator().hasNext()){
				Player p = curveServer.getPlayerCons().values().iterator().next();
				
			}
		}

	}

}
