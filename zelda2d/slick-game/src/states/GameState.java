package states;

import main.GameConstants;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import entitties.Block;
import entitties.Player;

public class GameState extends AbstractGameState {

	private TiledMap map = null;

	public GameState(int stateID) {
		super(stateID);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		new Player(50, 300);
		map = new TiledMap("data/maps/test.tmx");
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				if (map.getTileId(x, y, 0) > 0) {
					new Block(GameConstants.TILE_SIZE * x, GameConstants.TILE_SIZE * y + (GameConstants.TILE_SIZE / 2), GameConstants.TILE_SIZE, GameConstants.TILE_SIZE / 2);
				}
			}
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.translate(0, -100);

		map.render(0, 0);

		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(g);
		}

		g.translate(0, 100);

		g.drawString("Entities: " + entities.size(), 10, 30);
		g.drawString("State: " + entities.get(0).getState(), 10, 50);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update(delta);
		}
	}

}
