package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import entitties.Entity;

public class Camera {

	private GameContainer container;

	public float cameraX;
	public float cameraY;
	private float renderWidth;
	private float renderHeight;
	private Vector2f maxSpeed = null;
	private int horBorderPixel = 0;
	private int vertBorderPixel = 0;
	private Rectangle visibleRect = null;
	private Rectangle moveRect = null;

	private Entity follow;

	public Camera(GameContainer container, Entity toFollow, int width, int height) {
		this(container, toFollow, width, height, -1, -1, null);
	}

	public Camera(GameContainer container, Entity toFollow, int width, int height, int horBorderPixel, int vertBorderPixel, Vector2f maxSpeed) {
		this.container = container;
		this.cameraX = 0;
		this.cameraY = 0;
		this.renderWidth = width;
		this.renderHeight = height;
		this.follow = toFollow;
		this.horBorderPixel = horBorderPixel;
		this.vertBorderPixel = vertBorderPixel;
		this.maxSpeed = maxSpeed;
		if (toFollow != null) {
			// on startup position camera that toFollow is in the center of the
			// screen
			this.cameraX = follow.getX() - (this.renderWidth / 2);
			this.cameraY = follow.getY() - (this.renderHeight / 2);
		}
		this.visibleRect = new Rectangle(cameraX - horBorderPixel, cameraY - vertBorderPixel, renderWidth + horBorderPixel, renderHeight + vertBorderPixel);
		this.moveRect = new Rectangle(cameraX - horBorderPixel, cameraY - vertBorderPixel, renderWidth + horBorderPixel, renderHeight + vertBorderPixel);
		setCamera();
	}

	public void update(GameContainer container, int delta) throws SlickException {
		setCamera();
	}

	private void setCamera() {
		// position camera so that follow is on the center of the screen

		if (follow != null && !moveRect.contains(follow.getX() + follow.getWidth() / 2, follow.getY() + follow.getHeight() / 2)) {
			float targetCX = follow.getX() - (this.renderWidth / 2);
			float targetCY = follow.getY() - (this.renderHeight / 2);
			// now smoothly move camera on position cameraX, cameraY to position
			// targetCX, targetCY, using
			// maxSpeed
			if (maxSpeed != null) {
				if (Math.abs(targetCX - cameraX) > maxSpeed.x) {
					if (targetCX > cameraX)
						cameraX += maxSpeed.x * 2;
					else
						cameraX -= maxSpeed.x * 2;
				} else
					cameraX = targetCX;
				if (Math.abs(targetCY - cameraY) > maxSpeed.y) {
					if (targetCY > cameraY)
						cameraY += maxSpeed.y * 2;
					else
						cameraY -= maxSpeed.y * 2;
				} else
					cameraY = targetCY;
			} else {
				// move camera directly to new position
				cameraX = targetCX;
				cameraY = targetCY;
			}
			// recalculate worldX and worldY based on translateX and translateY
		}
		// Log.debug("setCamera(): cameraX = " + cameraX + ", cameraY = " +
		// cameraY + ", follow.x = " + follow.x + ", follow.y = " + follow.y);
		// do some border checking. we want to stay inside the world with our
		// container
		if (cameraX < 0)
			cameraX = 0;
		if (cameraX + renderWidth > container.getWidth())
			cameraX = container.getWidth() - renderWidth + 1;
		if (cameraY < 0)
			cameraY = 0;
		if (cameraY + renderHeight > container.getHeight())
			cameraY = container.getHeight() - renderHeight + 1;
		// Log.debug("setCamera2(): cameraX = " + cameraX + ", cameraY = " +
		// cameraY + ", follow.x = " + follow.x + ", follow.y = " + follow.y);
		// also calculate rendering rect to improve speed in contains() method
		// later on for rendering
		// visibleRect.setBounds(cameraX - horBorderPixel, cameraY - vertBorderPixel, renderWidth + horBorderPixel, renderHeight + vertBorderPixel);
		// moveRect.setBounds(cameraX + horBorderPixel / 2 - follow.getBody().getXVelocity(), cameraY + vertBorderPixel / 2, renderWidth - horBorderPixel + follow.getBody().getXVelocity(), renderHeight - vertBorderPixel);
	}

	public boolean contains(Entity e) {
		Rectangle entity = new Rectangle(e.getX(), e.getY(), e.getWidth(), e.getHeight());
		return visibleRect.intersects(entity);
	}

	public Entity getFollow() {
		return follow;
	}

	public void setFollow(Entity follow) {
		this.follow = follow;
	}

	public Rectangle getVisibleRect() {
		return visibleRect;
	}

	public Rectangle getMoveRect() {
		return moveRect;
	}

}
