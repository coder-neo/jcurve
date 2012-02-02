package client;

import java.awt.Point;

public class PlayerPoint extends Point{
	private float angle;
	private boolean isActive = true;

	public PlayerPoint(int x, int y, float angle) {
		super(x, y);
		this.angle = angle;
	}
	
	public PlayerPoint(){}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getAngle() {
		return angle;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
