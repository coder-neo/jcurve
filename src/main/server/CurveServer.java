package main.server;

import java.io.IOException;
import java.util.HashMap;

import main.GameConstants;
import main.NetworkConstants;
import main.Player;
import main.PlayerOptions;

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
		if (object instanceof Integer) {
			switch (Integer.valueOf(object.toString())) {
			case NetworkConstants.GAME_START:
				System.out.println("game start!!!");
				break;
			default:
				break;
			}
		} else if (object instanceof PlayerOptions){
			PlayerOptions pOptions = (PlayerOptions)object;
			Player p = playerCons.get(connection.getID());
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

}
