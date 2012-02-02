package client;

import java.io.IOException;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

import shared.GameConstants;
import utils.ResourceManager;

public class Bullet {
	private PlayerPoint position;
	private int speed = 4;
	private Image img;
	
	private ParticleSystem bulletSystem = null;
	private ConfigurableEmitter emitter = null;
	
	private int curDelta = 0;
	private int maxDelta = 1;
	
	public Bullet(PlayerPoint position) {
		this.position = position;
		img = ResourceManager.getImage("bullet").copy();
		img.setRotation((float) Math.toDegrees(this.position.getAngle()) + 90);
		try {
			bulletSystem = new ParticleSystem("data/emitters/particle.tga", 1000);
			emitter = ParticleIO.loadEmitter("data/emitters/shootEmitter.xml");
			bulletSystem.addEmitter(emitter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update(int delta) {
		curDelta += delta;
		if (curDelta > maxDelta) {
			curDelta = 0;
			position.x += Math.round(Math.cos(position.getAngle()) * 3) * speed;
			position.y += Math.round(Math.sin(position.getAngle()) * 3) * speed;
			emitter.setPosition(position.x, position.y);
		}
		bulletSystem.update(delta);
	}
	
	public void render(Graphics g) {
		img.drawCentered(position.x, position.y);
		bulletSystem.render();
	}

	public PlayerPoint getPosition() {
		return position;
	}
	
	public boolean isInsideScreen(){
		if (position.x + img.getWidth() < 0 || position.y + img.getHeight() < 0 || position.x > GameConstants.APP_WIDTH || position.y > GameConstants.APP_HEIGHT){
			return false;
		}
		return true;
	}

}
