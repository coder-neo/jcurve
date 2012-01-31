package main.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import main.GameConstants;
import main.NetworkConstants;
import main.Player;
import main.PlayerPoint;
import main.PlayerProperties;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class CurveServer extends Listener {
	private Server server;
	private HashMap<Integer, Player> playerCons = new HashMap<Integer, Player>();

	private Vector<Player> players = new Vector<Player>();

	public CurveServer() {
		try {
			server = new Server();
			Network.registerClasses(server);
			server.addListener(this);
			server.start();
			server.bind(GameConstants.PORT_TCP, GameConstants.PORT_UDP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		server.stop();
	}

	public InetAddress getIP() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void connected(Connection connection) {
		super.connected(connection);

		playerCons.put(connection.getID(), new Player(connection));

		sendPropertiesToAllPlayers();
	}

	private void sendPropertiesToAllPlayers() {
		Vector<PlayerProperties> props = new Vector<PlayerProperties>();
		Iterator<Player> players = playerCons.values().iterator();
		while (players.hasNext()) {
			PlayerProperties curProps = players.next().getProperties();
			System.out.println("[SERVER] sending to client name: " + curProps.getName());
			props.add(curProps);
		}
		server.sendToAllTCP(props);
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);

		playerCons.remove(connection.getID());
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);

		Player sendingPlayer = playerCons.get(connection.getID());

		if (object instanceof Integer) {
			switch (Integer.valueOf(object.toString())) {
			case NetworkConstants.GAME_START:
				sendingPlayer.setReady(true);
				break;
			case NetworkConstants.PLAYER_MOVE_LEFT:
				sendingPlayer.steerLeft();
				break;
			case NetworkConstants.PLAYER_MOVE_RIGHT:
				sendingPlayer.steerRight();
				break;
			case NetworkConstants.PLAYER_MOVE_STRAIGHT:
				sendingPlayer.steerStraight();
				break;
			case NetworkConstants.PLAYER_BOOST_ENABLE:
				sendingPlayer.setBoost(true);
				break;
			case NetworkConstants.PLAYER_BOOST_DISABLE:
				sendingPlayer.setBoost(false);
				break;
			case NetworkConstants.PLAYER_SHOOT:
				sendingPlayer.shoot();
				break;
			case NetworkConstants.PLAYER_DISCONNECT:
				playerCons.remove(connection.getID());
				break;
			default:
				break;
			}
		} else if (object instanceof PlayerProperties) {
			PlayerProperties properties = (PlayerProperties) object;
			sendingPlayer.setProperties(properties);
			sendPropertiesToAllPlayers();
		}
	}

	public HashMap<Integer, Player> getPlayerCons() {
		return playerCons;
	}

	public void setPlayerCons(HashMap<Integer, Player> playerCons) {
		this.playerCons = playerCons;
	}

	/**
	 * Sendet allen Clients die neuen Koordinaten aller Spieler.
	 */
	public void sendAllPlayerCoordinates() {
		HashMap<Integer, PlayerPoint> newPoints = new HashMap<Integer, PlayerPoint>();
		Iterator<Integer> conIDs = playerCons.keySet().iterator();
		while (conIDs.hasNext()) {
			int conID = (int) conIDs.next();
			newPoints.put(conID, playerCons.get(conID).getProperties().getPoints().lastElement());
		}
		server.sendToAllUDP(newPoints);
	}

	public Vector<Player> getPlayers() {
		return players;
	}

	public void setPlayers(Vector<Player> players) {
		this.players = players;
	}

}
