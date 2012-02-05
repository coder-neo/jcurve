package states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import entitties.Player;

public class GameState extends AbstractGameState {

	private Player player = null;

	private TiledMap map = null;

	public GameState(int stateID) {
		super(stateID);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		player = new Player(50, 300);
		map = new TiledMap("data/maps/test.tmx");
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		map.render(0, -100);

		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update(delta);
		}
	}

}
