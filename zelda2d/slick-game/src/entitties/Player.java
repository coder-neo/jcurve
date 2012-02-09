package entitties;

import main.GameConstants;

import org.jbox2d.common.Vec2;
import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SpriteSheet;

import states.AbstractGameState;
import utils.ResourceManager;
import utils.StaticUtils;

public class Player extends Entity {

	private Animation running, sleeping;
	private Image idle, block, attack, jump;

	private int attackDelta = 150;
	private int curAttackDelta = 0;

	private int attackDelay = 60;
	private int curAttackDelay = 0;

	private int jumpDelta = 500;
	private int curJumpDelta = 0;

	private int idleTime = 0;
	private int idleAnimationDelay = 10000;

	private boolean shieldSoundPlayed = false;
	private boolean jumpSoundPlayed = false;

	public Player(float x, float y) {
		super(x, y);
		initGraphics();
		health = 12;
	}

	private void initGraphics() {
		spriteSheet = new SpriteSheet(ResourceManager.getImage("linkRunning03"), 45, 41);
		running = new Animation(spriteSheet, 100);
		running.setAutoUpdate(false);

		sleeping = new Animation(new SpriteSheet(ResourceManager.getImage("linkSleeping"), 45, 41), 500);
		sleeping.setLooping(false);

		idle = ResourceManager.getImage("linkStanding03");
		block = ResourceManager.getImage("linkBlocking");
		jump = ResourceManager.getImage("linkJumping");
	}

	@Override
	public void initBody() {
		speed = 12.0f;
		body = new DynamicBody<Object>(new Rectangle(width, height), x, y);
		body.setFixedRotation(true);
		body.setRestitution(0f);
		body.setFriction(0f);
		body.setDensity(1.0f);
	}

	@Override
	public void onCollision(Entity otherEntity) {
	}

	@Override
	public void update(int delta) {
		super.update(delta);

		float xSpeed = 0;
		float ySpeed = 0;

		if (state == State.ATTACKING) {
			curAttackDelta += delta;
			if (curAttackDelta >= attackDelta) {
				curAttackDelta = 0;
				curAttackDelay = attackDelay;
				state = State.IDLE;
			}

			return;
		}

		if (curAttackDelay > 0) {
			curAttackDelay -= delta;
		} else if (curAttackDelay < 0) {
			curAttackDelay = 0;
		}

		state = State.IDLE;

		if (AbstractGameState.getInput().isKeyDown(Input.KEY_LSHIFT)) {
			state = State.BLOCKING;
			if (!shieldSoundPlayed) {
				ResourceManager.getSound("linkShield").play();
				shieldSoundPlayed = true;
			}
		}

		if (AbstractGameState.getInput().isKeyDown(Input.KEY_RIGHT) || AbstractGameState.getInput().isKeyDown(Input.KEY_D)) {
			state = State.MOVING;
			direction = Direction.RIGHT;
			running.update(delta);
			xSpeed = speed;
		}
		if (AbstractGameState.getInput().isKeyDown(Input.KEY_LEFT) || AbstractGameState.getInput().isKeyDown(Input.KEY_A)) {
			state = State.MOVING;
			direction = Direction.LEFT;
			running.update(delta);
			xSpeed = -speed;
		}

		if (AbstractGameState.getInput().isKeyDown(Input.KEY_UP) || AbstractGameState.getInput().isKeyDown(Input.KEY_W)) {
			if (body.getYVelocity() <= 0f) {
				curJumpDelta += delta;
				if (curJumpDelta >= jumpDelta) {
					curJumpDelta = 0;
					state = State.FALLING;
					return;
				}
				state = State.JUMPING;
				ySpeed = -15.0f;
				if (!jumpSoundPlayed) {
					ResourceManager.getSound("linkJump0" + StaticUtils.getRandomIntegerBetween(1, 2)).play();
					jumpSoundPlayed = true;
				}
			}
		}

		if (AbstractGameState.getInput().isKeyPressed(Input.KEY_SPACE) && curAttackDelay == 0) {
			state = State.ATTACKING;
			attack = ResourceManager.getImage("linkAttacking0" + StaticUtils.getRandomIntegerBetween(1, 2));
			ResourceManager.getSound("linkAttack0" + StaticUtils.getRandomIntegerBetween(1, 4)).play();
		}

		if (!body.isTouchingAnything()) {
			state = State.FALLING;
			if (body.getYVelocity() >= 0) {
				ySpeed = 15.0f;
			}
		}

		if (state == State.IDLE) {
			idleTime += delta;
			if (idleTime > idleAnimationDelay) {
				sleeping.update(delta);
			}
			shieldSoundPlayed = false;
			jumpSoundPlayed = false;
			curJumpDelta = 0;
		} else if (state == State.MOVING) {
			idleTime = 0;
			sleeping.restart();
			jumpSoundPlayed = false;
			curJumpDelta = 0;
		} else {
			idleTime = 0;
		}

		body.setVelocity(xSpeed, ySpeed);
	}

	@Override
	public void render(Graphics g) {
		super.render(g);

		Image curImage = null;

		if (state == State.MOVING) {
			if (direction == Direction.RIGHT) {
				running.getCurrentFrame().draw(x, y, scale);
			} else {
				running.getCurrentFrame().getFlippedCopy(true, false).draw(x, y, scale);
			}
		} else if (state == State.IDLE) {
			if (idleTime > idleAnimationDelay) {
				if (direction == Direction.RIGHT) {
					sleeping.getCurrentFrame().draw(x, y, scale);
				} else {
					sleeping.getCurrentFrame().getFlippedCopy(true, false).draw(x, y, scale);
				}
			} else {
				curImage = idle;
			}
		} else if (state == State.BLOCKING) {
			curImage = block;
		} else if (state == State.ATTACKING) {
			curImage = attack;
		} else if (state == State.JUMPING) {
			curImage = jump;
		} else if (state == State.FALLING) {
			curImage = jump;
		}

		if (curImage != null) {
			if (direction == Direction.RIGHT) {
				curImage.draw(x, y, scale);
			} else {
				curImage.getFlippedCopy(true, false).draw(x, y, scale);
			}
		}
	}

}
