package main.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
 */
public class CurveClient extends Listener {
	private Client client = null;
	private static CurveClient thisObject = null;

	private HashMap<Integer, Vector<PlayerPoint>> coordinates = new HashMap<Integer, Vector<PlayerPoint>>();

//	private HashMap<Integer, Vector<PlayerPoint>> coordinates = new HashMap<Integer, Vector<PlayerPoint>>();
	private Vector<PlayerProperties> playerProperties = new Vector<PlayerProperties>();
	
	private CurveClient() {
		client = new Client();
		Network.registerClasses(client);
		client.addListener(this);
		client.start();
		try {
			// client.connect(5000, "192.168.22.1", GameConstants.PORT_TCP, GameConstants.PORT_UDP);
			client.connect(5000, "localhost", GameConstants.PORT_TCP, GameConstants.PORT_UDP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static CurveClient getInstance() {
		if (thisObject == null) {
			thisObject = new CurveClient();
		}
		return thisObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		if (object instanceof HashMap) {
//			if (((HashMap<?, ?>) object).values() instanceof PlayerPoint) {
				System.out.println("KO received: " + object.toString());
				@SuppressWarnings("unchecked")
				HashMap<Integer, PlayerPoint> points = (HashMap<Integer, PlayerPoint>) object;
				Iterator<Integer> conIDs = points.keySet().iterator();
				while (conIDs.hasNext()) {
					int conID = (int) conIDs.next();
					if (coordinates.containsKey(conID)) {
						coordinates.get(conID).add(points.get(conID));
					} else {
						Vector<PlayerPoint> playerPoints = new Vector<PlayerPoint>();
						playerPoints.add(points.get(conID));
						coordinates.put(conID, playerPoints);
					}
			// hier werden Koordinaten empfangen
			System.out.println("KO received: " + object.toString());
			HashMap<Integer, PlayerPoint> newPoints = (HashMap<Integer, PlayerPoint>) object;
			for (int i = 0; i < playerProperties.size(); i++) {
				int conID = playerProperties.get(i).getConnectionID();
				if (newPoints.containsKey(conID)){
					playerProperties.get(i).getPoints().add(newPoints.get(conID));
				}
			}
//			Iterator<Integer> conIDs = newPoints.keySet().iterator();
//			while (conIDs.hasNext()) {
//				int conID = (int) conIDs.next();
//				playerProperties
//				if (coordinates.containsKey(conID)) {
//					coordinates.get(conID).add(newPoints.get(conID));
//				} else {
//					Vector<PlayerPoint> playerPoints = new Vector<PlayerPoint>();
//					playerPoints.add(newPoints.get(conID));
//					coordinates.put(conID, playerPoints);
//				}
//			}
			// coordinates.putAll(points);
			// points.put(pp.g, value)
		} else if (object instanceof Vector){
			@SuppressWarnings("unchecked")
			Vector<PlayerProperties> props = (Vector<PlayerProperties>) object;
			
			// hier werden bei Spielbeginn die Eigenschaften der Spieler übertragen,
			// damit jeder Client die Spieler rendern kann.
			playerProperties = (Vector<PlayerProperties>) object;
//			Vector<PlayerProperties> props = (Vector<PlayerProperties>) object;
//			for (int i = 0; i < props.size(); i++) {
//				
//			}
			System.out.println("properties!");
		}
	}

	public Client getClient() {
		return client;
	}

	public HashMap<Integer, Vector<PlayerPoint>> getCoordinates() {
		return coordinates;
	public Vector<PlayerProperties> getPlayerProperties() {
		return playerProperties;
	}

//	public HashMap<Integer, Vector<PlayerPoint>> getCoordinates() {
//		return coordinates;
//	}

}
