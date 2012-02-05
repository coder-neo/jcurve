package utils;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ResourceManager {

	private static HashMap<String, Image> images = new HashMap<String, Image>();

	static {
		addImage("linkStanding01", "data/sprites/link/standing01.png");
		addImage("linkStanding03", "data/sprites/link/standing03.png");
		
		addImage("linkRunning01", "data/sprites/link/running01.png");
		addImage("linkRunning03", "data/sprites/link/runningSwordShield.png");
		
		addImage("linkBlocking", "data/sprites/link/blocking.png");
	}

	private static void addImage(String key, String ref) {
		try {
			images.put(key, new Image(ref));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public static Image getImage(String key) {
		return images.get(key);
	}

}
