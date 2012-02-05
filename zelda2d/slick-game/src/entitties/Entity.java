package entitties;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

import states.AbstractGameState;

public abstract class Entity {

	public static enum Direction {
		LEFT, RIGHT;
	}

	public static enum State {
		IDLE, MOVING, BLOCKING, JUMPING, FALLING, ATTACKING
	}

	protected float speed = 2.0f;

	protected float x, y, scale = 1.0f;
	protected int width, height;
	protected Color color;

	protected Animation animation;
	protected SpriteSheet spriteSheet;

	protected Direction direction = Direction.RIGHT;
	protected State state = State.IDLE;

	public Entity(float x, float y) {
		AbstractGameState.getEntities().add(this);
		this.x = x;
		this.y = y;
	}

	public abstract void update(int delta);

	public abstract void render(Graphics g);

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

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
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

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public SpriteSheet getSpriteSheet() {
		return spriteSheet;
	}

	public void setSpriteSheet(SpriteSheet spriteSheet) {
		this.spriteSheet = spriteSheet;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

}
