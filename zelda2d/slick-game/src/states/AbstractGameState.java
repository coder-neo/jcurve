package states;

import java.util.ArrayList;

import main.GameConstants;

import org.newdawn.fizzy.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import physics.Renderer;
import entitties.Entity;
import gui.BasicElement;

public abstract class AbstractGameState extends BasicGameState {

	private int stateID = -1;

	private static GameContainer container;
	private static Input input;
	private static StateBasedGame game;

	protected static World world;
	protected static Renderer renderer;

	protected static ArrayList<Entity> entities = new ArrayList<Entity>();
	protected static ArrayList<BasicElement> elements = new ArrayList<BasicElement>();

	public AbstractGameState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public int getID() {
		return stateID;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		renderer = new Renderer();
		world = new World(10f);
		world.setIterations(30);
		world.setBounds(container.getWidth(), container.getHeight());
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		AbstractGameState.container = container;
		AbstractGameState.input = container.getInput();
		AbstractGameState.game = game;
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		for (int z = 0; z < GameConstants.GAME_SPEED; z++) {
			world.update(1.0f * delta / 30);

			if (container.getInput().isKeyPressed(Input.KEY_F1)) {
				GameConstants.DEBUG = !GameConstants.DEBUG;
			}

			for (int i = 0; i < entities.size(); i++) {
				entities.get(i).update(delta);
			}

			for (int i = 0; i < elements.size(); i++) {
				elements.get(i).update(delta);
			}
		}
	}

	protected void renderGUI(Graphics g) {
		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).render(g);
		}
	}

	public static GameContainer getContainer() {
		return container;
	}

	public static Input getInput() {
		return input;
	}

	public static StateBasedGame getGame() {
		return game;
	}

	public static ArrayList<Entity> getEntities() {
		return entities;
	}

	public static World getWorld() {
		return world;
	}

	public static void setWorld(World world) {
		AbstractGameState.world = world;
	}

	public static Renderer getRenderer() {
		return renderer;
	}

	public static void setRenderer(Renderer renderer) {
		AbstractGameState.renderer = renderer;
	}

	public static ArrayList<BasicElement> getElements() {
		return elements;
	}

}
