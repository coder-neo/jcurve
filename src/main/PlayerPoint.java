package main;

import java.awt.Point;

public class PlayerPoint extends Point{
	private float angle;

	public PlayerPoint(int x, int y, float angle) {
		super(x, y);
		this.angle = angle;
	}
	
	public PlayerPoint(){
		
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getAngle() {
		return angle;
	}

}
