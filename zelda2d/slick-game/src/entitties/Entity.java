package entitties;

import main.GameConstants;

import org.newdawn.fizzy.Body;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
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
	
	protected int health = 3;

	protected boolean solid = true;

	protected Animation animation;
	protected SpriteSheet spriteSheet;
	protected Image image;

	protected Direction direction = Direction.RIGHT;
	protected State state = State.IDLE;

	protected Body<Object> body;

	public Entity(float x, float y) {
		AbstractGameState.getEntities().add(this);
		this.x = x;
		this.y = y;
	}

	public Entity(float x, float y, int width, int height) {
		AbstractGameState.getEntities().add(this);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void update(int delta) {
		updateBounds();
		if (body == null) {
			initBody();
			AbstractGameState.getWorld().add(body);
		}
	}

	public abstract void initBody();

	public void render(Graphics g) {
		if (body == null) {
			return;
		}

		if (GameConstants.DEBUG) {
			try {
				AbstractGameState.getRenderer().drawBody(g, body);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

	// protected boolean checkCollision() {
	// for (int i = 0; i < AbstractGameState.getEntities().size(); i++) {
	// Entity entity = AbstractGameState.getEntities().get(i);
	// if (entity == this || !entity.isSolid() || !isSolid()) {
	// continue;
	// }
	// if (entity.getBody().intersects(getBody())) {
	// onCollision(entity);
	// entity.onCollision(this);
	// return true;
	// }
	// }
	//
	// return false;
	// }

	protected void updateBounds() {
		if (spriteSheet != null) {
			width = spriteSheet.getSprite(0, 0).getWidth();
			height = spriteSheet.getSprite(0, 0).getHeight();
		}

		if (body != null) {
			x = body.getX() - width / 2;
			y = body.getY() - height / 2;
		}
	}

	public abstract void onCollision(Entity otherEntity);

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

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public Body<Object> getBody() {
		return body;
	}

	public void setBody(Body<Object> body) {
		this.body = body;
	}

}
