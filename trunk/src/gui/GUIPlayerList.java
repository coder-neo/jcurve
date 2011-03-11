package gui;

import java.util.Vector;

import main.GameConstants;
import main.Player;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import utils.ResourceManager;
import utils.StaticUtils;

public class GUIPlayerList extends BasicGUIElement {

	private Vector<Player> players = new Vector<Player>();

	public GUIPlayerList(float x, float y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void render(Graphics g) {
		super.render(g);

		ResourceManager.getFont("chatFont").drawString(getX() + GUIChat.CHAT_PADDING_LEFT, getY(), "Spieler in der Lobby (" + players.size() + "/" + GameConstants.MAX_PLAYERS + ")");

		int startY = (int) (getY() + GUIChat.CHAT_PADDING_LINE) * 2;
		for (int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			ResourceManager.getFont("chatFont").drawString(getX() + GUIChat.CHAT_PADDING_LEFT, startY + (i * GUIChat.CHAT_PADDING_LINE), p.getName(), p.getColor());
			ResourceManager.getFont("chatFont").drawString(getX() + GUIChat.CHAT_PADDING_LEFT, startY + (i * GUIChat.CHAT_PADDING_LINE), p.getProperties().getName(), StaticUtils.getColorByCode(p.getProperties().getColorCode()));
		}
	}

	public void addPlayers(List<Player> players) {
		players.addAll(players);
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	public void setPlayers(Vector<Player> players) {
	public void updatePlayerVector(Vector<Player> players) {
		this.players = players;
	}

}
