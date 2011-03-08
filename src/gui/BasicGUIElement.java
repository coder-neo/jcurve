package gui;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.ControlledInputReciever;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

/**
 * Ein abstraktes Element der GUI des Spiels.
 * 
 * @author Benjamin
 */
public abstract class BasicGUIElement {

	private BasicGUIElement parent = null;

	private Vector<BasicGUIElement> children = new Vector<BasicGUIElement>();
	private Vector<ControlledInputReciever> listener = new Vector<ControlledInputReciever>();

	private float x, y;
	private int width, height;
	private Color backgroundColor, fontColor, borderColor;
	private Image image;
	private Rectangle border;

	public BasicGUIElement(float x, float y) {
		this(x, y, 0, 0);
	}

	public BasicGUIElement(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void render(Graphics g) {
		if (backgroundColor != null)
			g.setBackground(backgroundColor);

		if (fontColor != null)
			g.setColor(fontColor);

		if (image != null)
			image.draw(x, y);

		if (border != null) {
			g.setColor(borderColor);
			if (backgroundColor != null)
				g.fill(border);
			else
				g.draw(border);
		}

		g.setColor(Color.white);
		g.setBackground(Color.black);
	}

	public void update(int delta) {
	}

	public void setParent(BasicGUIElement parent) {
		this.parent = parent;
	}

	public BasicGUIElement getParent() {
		return parent;
	}

	public Vector<BasicGUIElement> getChildren() {
		return children;
	}

	public void addChild(BasicGUIElement element) {
		if (!children.contains(element)) {
			children.add(element);
			element.setParent(this);
		}
	}

	public void addListener(ControlledInputReciever listener) {
		if (!this.listener.contains(listener))
			this.listener.add(listener);
	}

	public Vector<ControlledInputReciever> getListener() {
		return listener;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Rectangle getBorder() {
		return border;
	}

	public void setBorder(Color borderColor) {
		this.borderColor = borderColor;
		this.border = new Rectangle(x, y, width, height);
	}
}
