package main;

import java.util.Vector;

/**
 * PlayerProperties ist eine Klasse, die Spieler Informationen enth�lt f�r die
 * �bertragung �ber das Netzwerk. Sie enth�lt nur die Informationen, die f�r
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
	// private Color color;
	private int colorCode = 0xffffff;
	private String imageKey;

	// private Image image;

	public PlayerProperties() {
		this.connectionID = -1;
	}

	public PlayerProperties(int connectionID) {
		this.connectionID = connectionID;
	}

	public PlayerProperties(){}
	
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

	// public Color getColor() {
	// return color;
	// }
	// public void setColor(Color color) {
	// this.color = color;
	// }
	// public Image getImage() {
	// return image;
	// }
	// public void setImage(Image image) {
	// this.image = image;
	// }
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
