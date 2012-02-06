package utils;

import java.util.ResourceBundle;

public class Localization {

	private static ResourceBundle resourceBundle;

	public static String get(String key) {
		if (resourceBundle == null) {
			resourceBundle = ResourceBundle.getBundle("languages.strings");
		}

		return resourceBundle.getString(key);
	}

}
