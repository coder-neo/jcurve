package gui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import states.AbstractGameState;
import entitties.Entity;

public abstract class BasicElement {

	protected BasicElement parent;
	protected ArrayList<BasicElement> children = new ArrayList<BasicElement>();
	protected ArrayList<ElementActionListener> listeners = new ArrayList<ElementActionListener>();
	protected Entity associatedEntity;

	protected float x, y;
	protected int width, height;
	protected Color color = Color.white;
	protected Image image;
	protected SpriteSheet spriteSheet;

	protected int currentSprite = 0;

	protected boolean visible = true;

	public BasicElement(float x, float y, int width, int height) {
		AbstractGameState.getElements().add(this);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void render(Graphics g) {
		if (!visible) {
			return;
		}

		if (image != null) {
			image.draw(x, y, color);
		}

		if (spriteSheet != null) {
			spriteSheet.getSprite(currentSprite, 0).draw(x, y, color);
		}
	}

	public void update(int delta) {

	}

	public void addListener(ElementActionListener listener) {
		listeners.add(listener);
	}

	public BasicElement getParent() {
		return parent;
	}

	public void setParent(BasicElement parent) {
		this.parent = parent;
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

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public SpriteSheet getSpriteSheet() {
		return spriteSheet;
	}

	public void setSpriteSheet(SpriteSheet spriteSheet) {
		this.spriteSheet = spriteSheet;
	}

	public ArrayList<BasicElement> getChildren() {
		return children;
	}

	public ArrayList<ElementActionListener> getListeners() {
		return listeners;
	}

	public Entity getAssociatedEntity() {
		return associatedEntity;
	}

	public void setAssociatedEntity(Entity associatedEntity) {
		this.associatedEntity = associatedEntity;
	}

}
