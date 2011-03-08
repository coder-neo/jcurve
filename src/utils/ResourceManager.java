package utils;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

/**
 * Speichert alle Resourcen des Spiels, wie etwa Fonts oder Grafiken.
 * 
 * @author Benjamin
 */
public class ResourceManager {

	private static HashMap<String, UnicodeFont> fonts = new HashMap<String, UnicodeFont>();
	private static HashMap<String, Image> images = new HashMap<String, Image>();

	@SuppressWarnings("unchecked")
	public static void addFont(String fontName, String fileName, int fontSize, boolean isBold, boolean isItalic) {
		if (!fonts.containsKey(fontName)) {
			try {
				UnicodeFont font = new UnicodeFont(fileName, fontSize, isBold, isItalic);
				font.getEffects().add(new ColorEffect(java.awt.Color.white));
				font.addAsciiGlyphs();
				font.loadGlyphs();
				fonts.put(fontName, font);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static UnicodeFont getFont(String fontName) {
		return fonts.get(fontName);
	}

	public static void addImage(String key, String path){
		if (!images.containsKey(key)){
			try {
				images.put(key, new Image(path));
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Image getImage(String key){
		return images.get(key);
	}
}
