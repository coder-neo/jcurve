package utils;

import java.awt.Color;
import java.util.HashMap;

import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class ResourceManager {

	private static HashMap<String, Image> images = new HashMap<String, Image>();
	private static HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	private static HashMap<String, Font> fonts = new HashMap<String, Font>();

	static {
		// images
		addImage("linkStanding01", "data/sprites/link/standing01.png");
		addImage("linkStanding03", "data/sprites/link/standing03.png");

		addImage("linkRunning01", "data/sprites/link/running01.png");
		addImage("linkRunning03", "data/sprites/link/runningSwordShield.png");

		addImage("linkBlocking", "data/sprites/link/blocking.png");
		addImage("linkJumping", "data/sprites/link/jump.png");
		addImage("linkSleeping", "data/sprites/link/sleeping.png");

		addImage("linkAttacking01", "data/sprites/link/attack01.png");
		addImage("linkAttacking02", "data/sprites/link/attack02.png");
		addImage("linkAttacking03", "data/sprites/link/attack03.png");

		addImage("hearts", "data/sprites/link/hearts.png");

		// sounds
		addSound("linkAttack01", "data/sfx/link/attack01.wav");
		addSound("linkAttack02", "data/sfx/link/attack02.wav");
		addSound("linkAttack03", "data/sfx/link/attack03.wav");
		addSound("linkAttack04", "data/sfx/link/attack04.wav");

		addSound("linkShield", "data/sfx/link/shield.wav");

		addSound("linkJump01", "data/sfx/link/jump01.wav");
		addSound("linkJump02", "data/sfx/link/jump02.wav");
		
		addSound("typeLetter", "data/sfx/gui/letter.wav");

		// fonts
		addFont("triforce20", "data/fonts/triforce.ttf", 20);
	}

	private static void addImage(String key, String ref) {
		try {
			images.put(key, new Image(ref));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private static void addSound(String key, String ref) {
		try {
			sounds.put(key, new Sound(ref));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private static void addFont(String key, String ref, int size) {
		try {
			UnicodeFont font = new UnicodeFont(ref, size, false, false);
			font.getEffects().add(new ColorEffect(Color.white));
			font.addAsciiGlyphs();
			font.loadGlyphs();
			fonts.put(key, font);
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	public static Image getImage(String key) {
		return images.get(key);
	}

	public static Sound getSound(String key) {
		return sounds.get(key);
	}

	public static Font getFont(String key) {
		return fonts.get(key);
	}

}
