package entitties;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SpriteSheet;

import states.AbstractGameState;
import utils.ResourceManager;

public class Player extends Entity {

	private Animation running;
	private Image idle, block;

	public Player(float x, float y) {
		super(x, y);
		initGraphics();
	}

	private void initGraphics() {
		spriteSheet = new SpriteSheet(ResourceManager.getImage("linkRunning03"), 45, 41);
		running = new Animation(spriteSheet, 100);
		running.setAutoUpdate(false);

		idle = ResourceManager.getImage("linkStanding03");
		block = ResourceManager.getImage("linkBlocking");
	}

	@Override
	public void onCollision(Entity otherEntity) {
	}

	@Override
	public void update(int delta) {
		super.update(delta);

		if (!checkCollision()) {
			state = State.FALLING;
			y += speed;
		}

		if (AbstractGameState.getInput().isKeyDown(Input.KEY_RIGHT) || AbstractGameState.getInput().isKeyDown(Input.KEY_D)) {
			state = State.MOVING;
			direction = Direction.RIGHT;
			running.update(delta);
			x += speed;
		} else if (AbstractGameState.getInput().isKeyDown(Input.KEY_LEFT) || AbstractGameState.getInput().isKeyDown(Input.KEY_A)) {
			state = State.MOVING;
			direction = Direction.LEFT;
			running.update(delta);
			x -= speed;
		} else if (AbstractGameState.getInput().isKeyDown(Input.KEY_UP) || AbstractGameState.getInput().isKeyDown(Input.KEY_W)) {
			state = State.JUMPING;
			y -= speed / 2;
		} else if (AbstractGameState.getInput().isKeyPressed(Input.KEY_SPACE)) {
			state = State.ATTACKING;
		} else if (AbstractGameState.getInput().isKeyDown(Input.KEY_LSHIFT)) {
			state = State.BLOCKING;
		} else {
			state = State.IDLE;
		}

		if (checkCollision()) {
			x = oldX;
			y = oldY;
		} else {
			oldX = x;
			oldY = y;
		}
	}

	@Override
	public void render(Graphics g) {
		Image curImage = null;

		g.draw(body);

		if (state == State.MOVING) {
			if (direction == Direction.RIGHT) {
				running.draw(x, y);
			} else {
				running.getCurrentFrame().getFlippedCopy(true, false).draw(x, y);
			}
		} else if (state == State.IDLE) {
			curImage = idle;
		} else if (state == State.BLOCKING) {
			curImage = block;
		}

		if (curImage != null) {
			if (direction == Direction.RIGHT) {
				curImage.draw(x, y);
			} else {
				curImage.getFlippedCopy(true, false).draw(x, y);
			}
		}
	}

}
