package gui;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import shared.ConnectedPlayer;
import shared.GameConstants;
import shared.PlayerProperties;
import utils.ResourceManager;
import utils.StaticUtils;

public class GUIPlayerList extends BasicGUIElement {

	private ArrayList<ConnectedPlayer> connectedPlayers = new ArrayList<ConnectedPlayer>();

	public GUIPlayerList(float x, float y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void render(Graphics g) {
		super.render(g);

		ResourceManager.getFont("small").drawString(getX() + GUIChat.CHAT_PADDING_LEFT, getY(), "Connected players (" + connectedPlayers.size() + "/" + GameConstants.MAX_PLAYERS + ")");

		int startY = (int) (getY() + GUIChat.CHAT_PADDING_LINE) * 2;
		for (int i = 0; i < connectedPlayers.size(); i++) {
			ConnectedPlayer connectedPlayer = connectedPlayers.get(i);
			PlayerProperties p = connectedPlayer.getProperties();
			String name;
			if (connectedPlayer.isHost()) {
				name = "[Server] " + p.getName();
			} else {
				name = p.getName();
			}

			ResourceManager.getFont("small").drawString(getX() + GUIChat.CHAT_PADDING_LEFT, startY + (i * GUIChat.CHAT_PADDING_LINE), name, StaticUtils.getColorByCode(p.getColorCode()));
		}
	}

	public void updateList(ArrayList<ConnectedPlayer> connectedPlayers) {
		// no reference, so we copy this object
		this.connectedPlayers.clear();
		for (int i = 0; i < connectedPlayers.size(); i++) {
			this.connectedPlayers.add(connectedPlayers.get(i));
		}
	}

	public void clear() {
		connectedPlayers.clear();
	}

}
