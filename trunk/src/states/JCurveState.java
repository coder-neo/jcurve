package states;

import gui.BasicGUIElement;

import java.util.Vector;

import main.client.CurveClient;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import utils.ResourceManager;

import com.esotericsoftware.kryonet.Client;

/**
 * Die abstrakte Oberklasse unserer einzelnen States. Bietet
 * Basisfunktionalitäten, die häufig gebraucht werden.
 * 
 * @author Benjamin
 */
public abstract class JCurveState extends BasicGameState {

	private int id;

	private Vector<BasicGUIElement> elements = new Vector<BasicGUIElement>();

	public JCurveState(int id) {
		this.id = id;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		ResourceManager.addFont("chatFont", "data/fonts/tempesta.ttf", 12, false, false);
		ResourceManager.addFont("header", "data/fonts/cool.ttf", 60, false, false);
		ResourceManager.addFont("standard", "data/fonts/cool.ttf", 30, false, false);
		ResourceManager.addFont("small", "data/fonts/cool.ttf", 12, false, false);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).render(g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).update(delta);
		}
	}

	/**
	 * Liefert das Client-Objekt des Spielers.
	 * 
	 * @return Client
	 */
	public Client getClient() {
		return CurveClient.getInstance().getClient();
	}

	/**
	 * Fügt n GUI-Objekte in diese State hinzu.
	 * 
	 * @param e
	 *            - eine Liste von GUI-Elementen
	 */
	public void addGUIElements(BasicGUIElement... e) {
		for (int i = 0; i < e.length; i++)
			elements.add(e[i]);
	}

}
