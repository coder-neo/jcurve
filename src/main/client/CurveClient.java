package main.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Vector;

import main.GameConstants;
import main.JCurve;
import main.PlayerPoint;
import main.PlayerProperties;
import main.server.Network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * Die CurveClient Klasse repräsentiert die Client Seite des Spiels. Da jeder
 * Spieler lokal nur genau einen Client braucht, ist die Klasse ein Singleton.
 * Sie handelt die empfangenen Pakete.
 * 
 * @author Adam
 * @author Benjamin
 * @author Benedikt
 */
public class CurveClient extends Listener {
	private Client client = null;
	private static CurveClient thisObject = null;

	/**
	 * Hier werden die für das Rendern relevanten Spielerinformationen gespeichert.
	 */
	private Vector<PlayerProperties> playerProperties = new Vector<PlayerProperties>();

	/**
	 * Initialisiert den Client, verbindet sich aber nicht zu einem Server und
	 * wartet in einer Art Stand-By-Modus.
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
		CurveClient.getInstance().getClient().sendTCP(JCurve.userData);
	}

	/**
	 * @param hostName 
	 * @see CurveClient#connect(InetAddress)
	 */
	public void connect(String hostName) {
		try {
			client.connect(5000, hostName, GameConstants.PORT_TCP, GameConstants.PORT_UDP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		CurveClient.getInstance().getClient().sendTCP(JCurve.userData);
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
		}
		// Eine Sammlung von Spielern, wenn ein neuer Spieler die Spielerliste
		// bei Eintritt in die Lobby anfordert.
		else if (object instanceof Vector) {
			playerProperties = (Vector<PlayerProperties>) object;
		}
		// Ein einzelner neuer Spieler, der nach einem selbst connected.
		else if (object instanceof PlayerProperties) {
			PlayerProperties playerProp = (PlayerProperties)object;
			if (playerProp.isDisconnected()){
				System.out.println("### REMOVE PLAYER");
				for (int i = 0; i < playerProperties.size(); i++){
					if (playerProperties.get(i).getConnectionID() == playerProp.getConnectionID()){
						playerProperties.remove(i);
						break;
					}
				}
				JCurve.getLobby().removePlayer(playerProp);
			} else {
				System.out.println("### ADD PLAYER");
				playerProperties.add(playerProp);
				JCurve.getLobby().addPlayer(playerProp);
			}
			System.out.println("Elements: "+playerProperties.size());
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
