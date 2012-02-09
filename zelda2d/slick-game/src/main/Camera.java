package main;

import entitties.Entity;

public class Camera {

	private Entity entity;

	private float x, y;
	private float xOffset, yOffset;

	public void update(int delta) {
		if (entity != null) {
			if (entity.getBody() != null) {
				x = xOffset + (entity.getBody().getX() - GameConstants.SCREEN_WIDTH / 2);
				y = yOffset + (entity.getBody().getY() - GameConstants.SCREEN_HEIGHT / 2);

				if (x < 0) {
					x = 0;
				} else if (x > GameConstants.SCREEN_WIDTH) {
					x = GameConstants.SCREEN_WIDTH;
				}

				if (y < 0) {
					y = 0;
				} else if (y > GameConstants.SCREEN_HEIGHT) {
					y = GameConstants.SCREEN_HEIGHT;
				}
			}
		}
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
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

	public float getXOffset() {
		return xOffset;
	}

	public void setXOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	public float getYOffset() {
		return yOffset;
	}

	public void setYOffset(float yOffset) {
		this.yOffset = yOffset;
	}

}
