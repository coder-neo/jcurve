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

import utils.ResourceManager;

public class GameState extends JCurveState {
	
	private CurveServer curveServer;

	public GameState(int id) {
		super(id);
		curveServer = new CurveServer();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		ResourceManager.getFont("chatFont").drawString(300, 300, "Hallo, dies ist eine Chatnachricht! XD");
		ResourceManager.getFont("chatFont").drawString(300, 324, "Hallo, dies ist eine zweite möp möp Chatnachricht! XD");
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			getClient().sendTCP(NetworkConstants.GAME_START);
			getClient().sendTCP(new PlayerOptions("adam", "ff0000"));
		}
		if (curveServer != null) {
			// Serverberechnungen
			while (curveServer.getPlayerCons().values().iterator().hasNext()) {
				Player p = curveServer.getPlayerCons().values().iterator().next();

			}
		}

	}

}
