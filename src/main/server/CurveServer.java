package main.server;

import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import main.GameConstants;
import main.NetworkConstants;
import main.Player;
import main.PlayerOptions;
import main.PlayerPoint;

import org.newdawn.slick.Color;

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
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		Player p = playerCons.get(connection.getID());
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
			default:
				break;
			}
		} else if (object instanceof PlayerOptions){
			PlayerOptions pOptions = (PlayerOptions)object;
			System.out.println("Color: "+Integer.valueOf(pOptions.getColor(), 16).intValue());
			p.setColor(new Color(Integer.valueOf(pOptions.getColor(), 16).intValue()));
			p.setName(pOptions.getName());
			System.out.println("size: "+playerCons.size());
			System.out.println("name: "+playerCons.get(connection.getID()).getName());
			System.out.println("name: "+playerCons.get(connection.getID()).getColor().toString());
		}
	}

	public HashMap<Integer, Player> getPlayerCons() {
		return playerCons;
	}

	public void setPlayerCons(HashMap<Integer, Player> playerCons) {
		this.playerCons = playerCons;
	}
	
	public void sendAllPlayerCoordinates(){
		HashMap<Integer, PlayerPoint> newPoints = new HashMap<Integer, PlayerPoint>();
		Iterator<Integer> conIDs = playerCons.keySet().iterator();
		while (conIDs.hasNext()){
			int conID = (int)conIDs.next();
			newPoints.put(conID, playerCons.get(conID).getPoints().lastElement());
		}
		server.sendToAllUDP(newPoints);
	}

}
