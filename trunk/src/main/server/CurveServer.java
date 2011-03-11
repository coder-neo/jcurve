package main.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import main.GameConstants;
import main.NetworkConstants;
import main.Player;
import main.PlayerOptions;
import main.PlayerPoint;
import main.PlayerProperties;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class CurveServer extends Listener {
	private Server server;
	private HashMap<Integer, Player> playerCons = new HashMap<Integer, Player>();

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
		playerCons.put(connection.getID(), new Player(connection));
		server.sendToTCP(connection.getID(), "server: hallo client");
		
		// TODO: erst schicken, wenn alle spieler bereit sind
		Vector<PlayerProperties> props = new Vector<PlayerProperties>();
		Iterator<Player> players = playerCons.values().iterator();
		while (players.hasNext()){
			props.add(players.next().getProperties());
		}
//		server.sendToTCP(connection.getID(), playerCons);
		server.sendToTCP(connection.getID(), props);
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		Player p = playerCons.get(connection.getID());
		if (object instanceof Integer) {
//			int cmd = Integer.valueOf(object.toString());
//			if (cmd != NetworkConstants.PLAYER_BOOST && p.isBoost()){
//				p.setBoost(false);
//				System.out.println("disable boost");
//			}
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
			default:
				break;
			}
		} else if (object instanceof PlayerOptions){
			PlayerOptions pOptions = (PlayerOptions)object;
			System.out.println("Color: "+Integer.valueOf(pOptions.getColor(), 16).intValue());
			p.getProperties().setColorCode((Integer.valueOf(pOptions.getColor(), 16).intValue()));
			p.getProperties().setName(pOptions.getName());
			System.out.println("size: "+playerCons.size());
			System.out.println("name: "+playerCons.get(connection.getID()).getProperties().getName());
			System.out.println("color: "+playerCons.get(connection.getID()).getProperties().getColorCode());
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
	public void sendAllPlayerCoordinates(){
		HashMap<Integer, PlayerPoint> newPoints = new HashMap<Integer, PlayerPoint>();
		Iterator<Integer> conIDs = playerCons.keySet().iterator();
		while (conIDs.hasNext()){
			int conID = (int)conIDs.next();
			newPoints.put(conID, playerCons.get(conID).getProperties().getPoints().lastElement());
		}
		server.sendToAllUDP(newPoints);
	}

}
