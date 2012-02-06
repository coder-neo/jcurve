package utils;

public class StaticUtils {

	public static int getRandomIntegerBetween(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

}
