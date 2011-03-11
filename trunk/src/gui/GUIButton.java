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
 * Ein Button, der eine gewisse Aktion ausführen kann. Der Button kann entweder
 * beschriftet sein, wie etwa ein Menüpunkt, oder einfach eine mit Farbe
 * gefüllte Fläche sein, zB. wenn der Spieler seine Farbe wählt.
 * 
 * @author Benjamin
 */
public class GUIButton extends BasicGUIElement {

	private String text = null;

	private Graphics texture = null;
	private GUIContext context = null;   
	private MouseOverArea area = null;

	private Color normalColor = Color.white, mouseOverColor = Color.orange, disabledColor = Color.gray;
	private Color fillColor;

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
		this(text, context, x, y, "standard");
	}

	/**
	 * @see GUIButton#GUIButton(String, GUIContext, float, float)
	 */
	public GUIButton(String text, GUIContext context, float x, float y, String fontName) {
		super(x, y, ResourceManager.getFont(fontName).getWidth(text), ResourceManager.getFont(fontName).getHeight(text));

		this.text = text;
		this.context = context;

		Image button = null;
		try {
			button = new Image(getWidth(), getHeight());
			texture = button.getGraphics();
			texture.setFont(ResourceManager.getFont(fontName));
		} catch (SlickException e) {
			e.printStackTrace();
		}

		Rectangle shape = new Rectangle(x, y, getWidth(), getHeight());

		area = new MouseOverArea(context, button, shape);
		setNormalColor(normalColor);
		setMouseOverColor(mouseOverColor);
	}

	public GUIButton(GUIContext context, Color color, float x, float y, int width, int height) {
		super(x, y, width, height);

		this.text = "";
		this.context = context;
		this.fillColor = color;

		Image button = null;
		try {
			button = new Image(getWidth(), getHeight());
			texture = button.getGraphics();
			texture.setColor(fillColor);
		} catch (SlickException e) {
			e.printStackTrace();
		}

		Rectangle shape = new Rectangle(x, y, getWidth(), getHeight());

		area = new MouseOverArea(context, button, shape);
		setNormalColor(normalColor);
		setMouseOverColor(mouseOverColor);
	}

	@Override
	public void render(Graphics g) {
		super.render(g);

		if (text != "")
			texture.drawString(text, 0, 0);
		else
			texture.fillRect(0, 0, getWidth(), getHeight());

		texture.flush();

		area.render(context, g);
	}

	/**
	 * Legt die Textfarbe des Buttons fest.
	 * 
	 * @param c
	 *            - die Farbe
	 */
	public void setNormalColor(Color c) {
		normalColor = c;
		area.setNormalColor(normalColor);
	}

	/**
	 * Legt die MouseOver-Farbe des Buttons fest.
	 * 
	 * @param c
	 *            - die Farbe
	 */
	public void setMouseOverColor(Color c) {
		mouseOverColor = c;
		area.setMouseOverColor(mouseOverColor);
	}

	/**
	 * Legt die Farbe für den Button im deaktivierten Zustand fest.
	 * 
	 * @param c
	 *            - die Farbe
	 */
	public void setDisabledColor(Color c) {
		disabledColor = c;
	}

	/**
	 * Liefert die MouseOverArea dieses Button.
	 * 
	 * @return MouseOverArea
	 */
	public MouseOverArea getMouseOverArea() {
		return area;
	}

	/**
	 * Gibt an, ob dieser Button aktiviert ist, d.h. auf Eingaben reagiert.
	 * 
	 * @return boolean
	 */
	public boolean isEnabled() {
		return area.isAcceptingInput();
	}

	/**
	 * Setzt diesen Button aktiv, bzw. inaktiv.
	 * 
	 * @param enable
	 *            - true für aktiv
	 */
	public void setEnabled(boolean enable) {
		if (enable) {
			area.setNormalColor(normalColor);
			area.setMouseOverColor(mouseOverColor);
			area.setAcceptingInput(true);
		} else {
			area.setNormalColor(disabledColor);
			area.setMouseOverColor(disabledColor);
			area.setAcceptingInput(false);
		}
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
	
	public Color getFillColor() {
		return fillColor;
	}

}
