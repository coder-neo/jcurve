package states;

import main.GameConstants;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import entitties.Block;
import entitties.Player;
import gui.LifeDisplay;
import gui.TextDisplay;

public class GameState extends AbstractGameState {

	public GameState(int stateID) {
		super(stateID);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		player = new Player(100, 400);
		map = new TiledMap("data/maps/test.tmx");
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				int blockedLayer = map.getLayerIndex(GameConstants.LAYER_FOREGROUND);
				if (map.getTileId(x, y, blockedLayer) > 0) {
					new Block(GameConstants.TILE_SIZE * x, GameConstants.TILE_SIZE * y, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
				}
			}
		}

		new LifeDisplay(30, 60, 100, 10).setAssociatedEntity(player);
		new TextDisplay("Hello world. How are thy highness?", 30, 100, 200, 100);
	}

}
