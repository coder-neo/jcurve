package states;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import entitties.Entity;

public abstract class AbstractGameState extends BasicGameState {

	private int stateID = -1;

	private static GameContainer container;
	private static Input input;
	private static StateBasedGame game;

	protected static ArrayList<Entity> entities = new ArrayList<Entity>();

	public AbstractGameState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public int getID() {
		return stateID;
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		AbstractGameState.container = container;
		AbstractGameState.input = container.getInput();
		AbstractGameState.game = game;
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

}
