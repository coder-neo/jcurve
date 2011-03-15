package main;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import utils.ResourceManager;

public class Bullet {
	private PlayerPoint position;
	private int speed = 4;
	private Image img;
	
	private int curDelta = 0;
	private int maxDelta = 1;
	
	public Bullet(PlayerPoint position) {
		this.position = position;
//		quantizeAngle();
		img = ResourceManager.getImage("bullet").copy();
		
		img.setRotation((float) Math.toDegrees(this.position.getAngle()) + 90);
	}
	
//	private void quantizeAngle(){
//		double angleDegrees = Math.toDegrees(this.position.getAngle());
//		angleDegrees = Math.round(angleDegrees / 22.5) * 22.5;
//		this.position.setAngle((float) Math.toRadians(angleDegrees));
//	}
	
	public void update(int delta) {
		curDelta += delta;
		if (curDelta > maxDelta) {
			curDelta = 0;
			position.x += Math.round(Math.cos(position.getAngle()) * 3) * speed;
			position.y += Math.round(Math.sin(position.getAngle()) * 3) * speed;
		}
	}
	
	public void render(Graphics g) {
		img.drawCentered(position.x, position.y);
	}

	public PlayerPoint getPosition() {
		return position;
	}
	
	public boolean isInsideScreen(){
		if (position.x + img.getWidth() < 0 || position.y + img.getHeight() < 0 || position.x > GameConstants.APP_WIDHT || position.y > GameConstants.APP_HEIGHT){
			return false;
		}
		return true;
	}

}
