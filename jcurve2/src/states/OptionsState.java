package states;

import gui.GUIButton;
import gui.GUITextField;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import main.JCurve;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.muffin.FileMuffin;
import org.newdawn.slick.state.StateBasedGame;

import shared.GameConstants;
import utils.ResourceManager;

/**
 * 
 * Hier kann der Spieler Optionen einstellen, die in einer XML-Datein lokal gespeichert werden
 * 
 * @author Benedikt
 * @author Benjamin
 */
public class OptionsState extends JCurveState {

	private GUITextField playerName = null;
	private Color playerColor = null;

	public OptionsState(int id) {
		super(id);
	}

	@Override
	public void init(GameContainer container, final StateBasedGame game) throws SlickException {
		super.init(container, game);

		Vector<Color> colors = new Vector<Color>();
		colors.add(Color.red);
		colors.add(Color.green);
		colors.add(Color.blue);
		colors.add(Color.yellow);
		colors.add(Color.pink);
		colors.add(Color.orange);
		colors.add(Color.magenta);
		colors.add(Color.gray);
		colors.add(Color.cyan);

		int x = 350;

		Color c = Color.decode(String.valueOf(JCurve.userData.getColorCode()));

		final Vector<GUIButton> buttons = new Vector<GUIButton>();

		for (int i = 0; i < colors.size(); i++) {
			final GUIButton button = new GUIButton(container, colors.get(i), x, 300, 50, 50);
			buttons.add(button);
			if (colors.get(i).equals(c)) {
				button.setActive(true);
			}
			button.setMouseOverColor(colors.get(i).darker(.25f));
			button.addListener(new ComponentListener() {
				@Override
				public void componentActivated(AbstractComponent source) {
					playerColor = button.getFillColor();
					for (int i = 0; i < buttons.size(); i++) {
						buttons.get(i).setActive(false);
					}
					button.setActive(true);
				}
			});

			addGUIElements(button);

			x += 75;
		}

		// TODO in JCurve.userData.getName() ist noch nicht gefüllt, da die
		// satetes vorher schon estellt werden, somit ist es null --> schlecht
		// :-D

		// playerName = new GUITextField(container,
		// ResourceManager.getFont("chatFont"), 350, 250, 300, 25,
		// JCurve.userData.getName() );
		playerName = new GUITextField(container, ResourceManager.getFont("chatFont"), 350, 250, 300, 25, "");
		playerName.setBackgroundColor(Color.white);
		playerName.setTextColor(Color.black);

		// TODO beim Speichern muss auch die Farbe gespeichert werden
		GUIButton buttonSave = new GUIButton("Save", container, 100, 450);
		buttonSave.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent source) {
				saveConfigFile();
				game.enterState(GameConstants.STATE_MAIN_MENU);
			}
		});

		GUIButton buttonCancel = new GUIButton("Cancel", container, 100, 500);
		buttonCancel.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent source) {
				game.enterState(GameConstants.STATE_MAIN_MENU);
			}
		});

		addGUIElements(buttonCancel, buttonSave);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		String name = JCurve.userData.getName();
		playerName.setText(name);

	}

	/**
	 * Speichert die Einstellungen des Spielers in einer Datei auf dem PC.
	 */
	private void saveConfigFile() {
		FileMuffin file = new FileMuffin();
		HashMap<Object, Object> data = new HashMap<Object, Object>();
		data.put("Name", playerName.getText());
		data.put("Color", playerColor);
		try {
			file.saveFile(data, GameConstants.APP_LOCAL_OPTIONS_FILENAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		MainMenuState.readConfigFile();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);

		ResourceManager.getFont("header").drawString(100, 100, "Options", Color.red);

		ResourceManager.getFont("standard").drawString(100, 250, "Name:");
		ResourceManager.getFont("standard").drawString(100, 300, "Color:");

		playerName.render(container, g);
	}
}