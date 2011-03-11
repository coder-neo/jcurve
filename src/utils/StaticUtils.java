package utils;

import org.newdawn.slick.Color;

/**
 * Nützliche statische Hilfsmethoden.
 * 
 * @author Benjamin
 */
public class StaticUtils {

	/**
	 * Liefert ein Color-Objekt anhand des Colorcodes
	 * 
	 * @param code
	 *            - der Colorcode als int
	 * @return Color
	 */
	public static Color getColorByCode(int code) {
		return new Color(code);
	}

	/**
	 * Zählt, wie oft ein gewisser String in einem anderen vorkommt.
	 * 
	 * @param input
	 *            - der String, in dem gesucht werden soll
	 * @param countString
	 *            - der String, nach dem gesucht werden soll
	 * @return int - Anzahl
	 */
	public static int countCharacter(String input, String countString) {
		return input.split("\\Q" + countString + "\\E", -1).length - 1;
	}

}
