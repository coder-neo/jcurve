package gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

import utils.ResourceManager;

/**
 * Ein Button, der eine gewisse Aktion ausführen kann.
 * 
 * @author Benjamin
 */
public class GUIButton extends BasicGUIElement {

	private String text = null;

	private Graphics texture = null;
	private GUIContext context = null;
	private MouseOverArea area = null;

	private Image button = null;

	/**
	 * Erstellt einen neuen Button an der bestimmten Position.
	 * 
	 * @param text
	 *            - der Text des Button
	 * @param context
	 *            - der GUIContext (das GameContainer-Objekt der init, render
	 *            oder update-Methoden)
	 * @param x
	 *            - die xPos
	 * @param y
	 *            - die yPos
	 */
	public GUIButton(String text, GUIContext context, float x, float y) {
		super(x, y, ResourceManager.getFont("standard").getWidth(text), ResourceManager.getFont("standard").getHeight(text));

		this.text = text;
		this.context = context;

		try {
			button = new Image(getWidth(), getHeight());
			texture = button.getGraphics();
			texture.setFont(ResourceManager.getFont("standard"));
		} catch (SlickException e) {
			e.printStackTrace();
		}

		Rectangle shape = new Rectangle(x, y, getWidth(), getHeight());
		area = new MouseOverArea(context, button, shape);
		area.setMouseOverColor(Color.orange);
	}

	/**
	 * Fügt dem Button einen Listener hinzu, der auf Aktionen reagiert.
	 * 
	 * @param listener
	 *            - der ComponentListener
	 */
	public void addListener(ComponentListener listener) {
		area.addListener(listener);
	}

	@Override
	public void render(Graphics g) {
		super.render(g);

		texture.drawString(text, 0, 0);
		texture.flush();

		area.render(context, g);
	}

}
