package gui;

import java.util.Vector;

import main.GameConstants;
import main.PlayerProperties;

import org.newdawn.slick.Graphics;

import utils.ResourceManager;
import utils.StaticUtils;

public class GUIPlayerList extends BasicGUIElement {

	private Vector<PlayerProperties> players = new Vector<PlayerProperties>();

	public GUIPlayerList(float x, float y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void render(Graphics g) {
		super.render(g);

		ResourceManager.getFont("small").drawString(getX() + GUIChat.CHAT_PADDING_LEFT, getY(), "Connected players (" + players.size() + "/" + GameConstants.MAX_PLAYERS + ")");

		int startY = (int) (getY() + GUIChat.CHAT_PADDING_LINE) * 2;
		for (int i = 0; i < players.size(); i++) {
			PlayerProperties p = players.get(i);
			ResourceManager.getFont("small").drawString(getX() + GUIChat.CHAT_PADDING_LEFT, startY + (i * GUIChat.CHAT_PADDING_LINE), p.getName(), StaticUtils.getColorByCode(p.getColorCode()));
		}
	}

	public void updatePlayerVector(Vector<PlayerProperties> players) {
		this.players = players;
	}

	/**
	 * Löscht alle Spieler aus der Lobby.
	 */
	public void clear() {
		players.removeAllElements();
	}

}
