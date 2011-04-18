package main.server;

import java.io.IOException;
import java.util.HashMap;

import main.GameConstants;
import main.NetworkConstants;
import main.Player;
import main.PlayerPoint;
import main.PlayerProperties;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public final class CurveServer extends Listener {
	private Server server;

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

	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		
		/*
		 *	Hier wird quasi eine Player-Hülle erzeugt. Die Eigenschaften werden danach
		 *	vom Player übermittelt. 
		 */
		new Player(connection);
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
		PlayerProperties playerProps = Player.getPlayer(connection.getID()).getProperties();
		playerProps.disconnect();
		server.sendToAllTCP(playerProps);
		Player.remove(connection.getID());
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);

		System.out.println("Server Received from ID: "+connection.getID());
		Player p = Player.getPlayer(connection.getID());

		if (object instanceof Integer) {
			switch (Integer.valueOf(object.toString())) {
				case NetworkConstants.GAME_START:
					p.setReady(true);
					break;
				case NetworkConstants.PLAYER_MOVE_LEFT:
					p.steerLeft();
					break;
				case NetworkConstants.PLAYER_MOVE_RIGHT:
					p.steerRight();
					break;
				case NetworkConstants.PLAYER_MOVE_STRAIGHT:
					p.steerStraight();
					break;
				case NetworkConstants.PLAYER_BOOST_ENABLE:
					p.setBoost(true);
					break;
				case NetworkConstants.PLAYER_BOOST_DISABLE:
					p.setBoost(false);
					break;
				case NetworkConstants.PLAYER_SHOOT:
					p.shoot();
					break;
				case NetworkConstants.PLAYER_DISCONNECT:
					Player.remove(connection.getID());
					break;
				default:
					break;
			}
		} else if (object instanceof PlayerProperties) {
			/**
			 * Ein einzelner Spieler teilt dem Server seine Properties mit.
			 */
			PlayerProperties properties = (PlayerProperties) object;
			properties.setConnectionID(connection.getID());
			p.setProperties(properties);
			System.out.println("server receive props: "+p.getProperties());
			server.sendToAllTCP(properties);
			server.sendToTCP(connection.getID(), Player.getAllPlayerProperties());
		}
	}

	/**
	 * Sendet allen Clients die letzte Koordinate aller Spieler.
	 */
	public void sendAllPlayerCoordinates() {
		HashMap<Integer, PlayerPoint> newPoints = new HashMap<Integer, PlayerPoint>();
		PlayerProperties playerProps;
		for (int i = 0; i < Player.getAllPlayerProperties().size(); i++){
			playerProps = Player.getAllPlayerProperties().get(i);
			newPoints.put(playerProps.getConnectionID(), playerProps.getPoints().lastElement());
		}
		server.sendToAllUDP(newPoints);
	}
}
