package states;

import gui.GUIButton;
import gui.GUITextField;

import java.util.Vector;

import main.GameConstants;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import utils.ResourceManager;

/**
 * 
 * Hier kann der Spieler Optionen einstellen, die in einer XML-Datein lokal
 * gespeichert werden
 * 
 * @author Benedikt
 * 
 */
public class OptionsState extends JCurveState {

	GUITextField nameText = null;
	Vector<Color> farben = null;
	Vector<GUIButton> farbButtons = null;
	Color spielerfarbe = null;

	public OptionsState(int id) {
		super(id);

	}

	@Override
	public void init(GameContainer container, final StateBasedGame game) throws SlickException {
		super.init(container, game);

		farbButtons = new Vector<GUIButton>();

		farben = new Vector<Color>();
		farben.add(Color.red);
		farben.add(Color.green);
		farben.add(Color.blue);
		farben.add(Color.yellow);

		int y = 400;
		// initallisiert die Rechtecke mit den Farben
		for (int i = 0; i < farben.size(); i++) {
			farbButtons.add(new GUIButton(container, farben.get(i), y, 150, 50, 50));
			y += 100;

			// Sollte ein Farbe angeklickt werden so wird in der Klassenvariable
			// spielerfarbe gespeichert
			farbButtons.get(i).addListener(new ComponentListener() {
				@Override
				public void componentActivated(AbstractComponent source) {
					for (int i = 0; i < farbButtons.size(); i++) {
						if (source.equals(farbButtons.get(i).getMouseOverArea())) {
							spielerfarbe = farbButtons.get(i).getFillColor();
						}
					}
				}
			});
			addGUIElements(farbButtons.get(i));
		}

		nameText = new GUITextField(container, ResourceManager.getFont("standard"), 400, 100, 200, 30);
		nameText.setBackgroundColor(Color.green);

		GUIButton speichern = new GUIButton("speichern", container, 100, 350);
		speichern.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent source) {
				// TODO Optionen auslesen und in XML speichern

			}
		});

		GUIButton zurueck = new GUIButton("zurück", container, 100, 450);
		zurueck.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent source) {
				game.enterState(GameConstants.STATE_MAIN_MENU);
			}
		});

		addGUIElements(zurueck, speichern);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);

		ResourceManager.getFont("standard").drawString(300, 100, "Name:");
		ResourceManager.getFont("standard").drawString(300, 150, "Farbe:");

		nameText.render(container, g);
	}
}