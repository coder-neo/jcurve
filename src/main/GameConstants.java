package main;

import java.awt.GraphicsEnvironment;

public class GameConstants {

	public final static String APP_NAME = "JCurve";

	public static final boolean APP_IS_FULLSCREEN = true;

	public final static int APP_WIDHT = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayMode().getWidth();
	public final static int APP_HEIGHT = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayMode().getHeight();

	public final static int PORT_TCP = 54555;
	public final static int PORT_UDP = 54777;

	public final static int STATE_GAME = 1;

	public static final int MAX_PLAYERS = 8;

	/**
	 * State IDs
	 */

	public final static int STATE_SERVER_CREATE = 1;

}
