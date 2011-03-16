package main.powerup;

import java.awt.Point;
import java.util.Vector;

import main.GameConstants;

import org.newdawn.slick.Image;

import utils.ResourceManager;

/**
 * Eine abstrakte Klasse von der alle Powerups erben.
 * @author Adam Laszlo
 *
 */
public abstract class Powerup {
	private static Vector<Powerup> powerups = new Vector<Powerup>();
	private Point position;
	private Image image;
	private int timeout = 10000;
	private int curTimeout = -10000;
	private boolean blendingIn = true;
	
	private static double spawnProbability = 0;
	
	public Powerup(String imageKey){
		image = ResourceManager.getImage(imageKey).copy();
		int x = (int) Math.round((Math.random()*(GameConstants.APP_WIDHT-image.getWidth())-image.getWidth())+image.getWidth()/2);
		int y = (int) Math.round((Math.random()*(GameConstants.APP_HEIGHT-image.getHeight())-image.getHeight())+image.getHeight()/2);
		position = new Point(x, y);
		powerups.add(this);
	}
	
	public static void powerupSpawner(){
		spawnProbability += 0.0001;
		if (Math.random() < spawnProbability){
			spawnProbability = 0;
			// Powerup spawnen
			int choice = (int) Math.round(Math.random());
			switch (choice) {
			case 0:
				new PowerupShot();
				break;
			case 1:
				new PowerupBoost();
				break;
			}
		}
	}
	
	public void render() {
		image.drawCentered(position.x, position.y);
	}
	
	public void update(int delta){
		curTimeout += delta;
		if (blendingIn){
			curTimeout += delta * 3;
			if (curTimeout>=0){
				blendingIn = false;
			}
		}
		if (curTimeout >= timeout){
			powerups.remove(this);
			return;
		}
		image.setAlpha(1-Math.abs(curTimeout/(float)timeout));
	}

	public static Vector<Powerup> getPowerups() {
		return powerups;
	}

	public Point getPosition() {
		return position;
	}

	public Image getImage() {
		return image;
	}
}
