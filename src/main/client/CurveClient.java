package main.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Vector;

import main.GameConstants;
import main.PlayerPoint;
import main.PlayerProperties;
import main.server.Network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * Die CurveClient Klasse repräsentiert die Client Seite des Spiels. Da jeder Spieler lokal nur genau einen Client braucht, ist die Klasse ein Singleton. Sie handelt die empfangenen Pakete.
 * 
 * Alle empfangenen Spielerkoordinaten werden (zugehörig zur ConnectionID) in einer Hashmap gespeichert.
 * 
 * @author Adam
 * @author Benjamin
 * @author Benedikt
 */
public class CurveClient extends Listener {
	private Client client = null;
	private static CurveClient thisObject = null;

	private Vector<PlayerProperties> playerProperties = new Vector<PlayerProperties>();

	/**
	 * Initialisiert den Client, verbindet sich aber nicht zu einem Server und wartet in einer Art Stand-By-Modus.
	 */
	private CurveClient() {
		client = new Client();
		Network.registerClasses(client);
		client.addListener(this);
		client.start();
	}

	/**
	 * Liefert die einzige Instanz dieser Klasse.
	 * 
	 * @return CurveClient
	 */
	public static CurveClient getInstance() {
		if (thisObject == null)
			thisObject = new CurveClient();

		return thisObject;
	}

	/**
	 * Verbindet den Client zu einem Server anhand der IP-Adresse
	 * 
	 * @param ip
	 *            - InetAddress-Objekt
	 */
	public void connect(InetAddress ip) {
		try {
			client.connect(5000, ip, GameConstants.PORT_TCP, GameConstants.PORT_UDP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see CurveClient#connect(InetAddress)
	 */
	public void connect(String hostName) {
		try {
			client.connect(5000, hostName, GameConstants.PORT_TCP, GameConstants.PORT_UDP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		if (object instanceof HashMap) {
			// hier werden Koordinaten empfangen
			HashMap<Integer, PlayerPoint> newPoints = (HashMap<Integer, PlayerPoint>) object;
			for (int i = 0; i < playerProperties.size(); i++) {
				int conID = playerProperties.get(i).getConnectionID();
				if (newPoints.containsKey(conID)) {
					playerProperties.get(i).getPoints().add(newPoints.get(conID));
				}
			}
		} else if (object instanceof Vector) {
			// dies sind die playerproperties der spieler
			Vector<PlayerProperties> props = (Vector<PlayerProperties>) object;
			for (PlayerProperties playerProperties : props) {
				System.out.println("[CLIENT] received props, name: " + playerProperties.getName());
			}
			playerProperties = props;
		}
	}

	/**
	 * Liefert das Client-Objekt dieses Spielers.
	 * 
	 * @return Client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Liefert die Eigenschaften dieses Spielers.
	 * 
	 * @return PlayerProperties
	 */
	public Vector<PlayerProperties> getPlayerProperties() {
		return playerProperties;
	}

}
