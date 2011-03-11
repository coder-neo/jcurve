package main;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import utils.ResourceManager;

/**
 * PlayerProperties ist eine Klasse, die Spieler Informationen enthält für die
 * Übertragung über das Netzwerk. Sie enthält nur die Informationen, die für
 * jeden Client zum rendern notwendig sind.
 * 
 * @author Adam Laszlo
 * 
 */
public class PlayerProperties {
	private int connectionID;
	private Vector<PlayerPoint> points = new Vector<PlayerPoint>();
	private String name = "Unbekannt";
	private int score = 0;
	private int colorCode = 0xffffff;
	private String imageKey;

	public PlayerProperties() {
		this.connectionID = -1;
	}

	public PlayerProperties(int connectionID) {
		this.connectionID = connectionID;
	}

	public void render(Graphics g) {
		Image tmpImg = null;
		Color color = new Color(colorCode);
		for (int i = 0; i < points.size(); i++) {
			tmpImg = ResourceManager.getImage(imageKey).copy();
			tmpImg.setRotation((float) Math.toDegrees(points.get(i).getAngle()));
			g.drawImage(tmpImg, points.get(i).x - tmpImg.getWidth()/2, points.get(i).y - tmpImg.getHeight()/2, color);
		}
	}
	
	public Vector<PlayerPoint> getPoints() {
		return points;
	}

	public void setPoints(Vector<PlayerPoint> points) {
		this.points = points;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getImageKey() {
		return imageKey;
	}

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}

	public int getColorCode() {
		return colorCode;
	}

	public void setColorCode(int colorCode) {
		this.colorCode = colorCode;
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

}
