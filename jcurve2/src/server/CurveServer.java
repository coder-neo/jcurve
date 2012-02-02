package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import shared.ChatMessage;
import shared.ConnectedPlayer;
import shared.GameConstants;
import shared.Network;
import shared.PlayerProperties;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class CurveServer extends Listener {

	private Server server = null;

	private ArrayList<ConnectedPlayer> connectedPlayers = new ArrayList<ConnectedPlayer>();
	private ArrayList<ChatMessage> chatMessages = new ArrayList<ChatMessage>();

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

	public InetAddress getIP() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void shutdown() {
		server.close();
	}

	@Override
	public void connected(Connection connection) {
		super.connected(connection);

		ConnectedPlayer connectedPlayer = new ConnectedPlayer(connection.getID());
		connectedPlayers.add(connectedPlayer);
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);

		ConnectedPlayer playerToRemove = getPlayerByConnection(connection);
		connectedPlayers.remove(playerToRemove);

		// wenn ein spieler die verbindung trennt, werden alle clients benachrichtigt.
		sendConnectedPlayersToAllClients();
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);

		ConnectedPlayer sendingPlayer = getPlayerByConnection(connection);

		// ein spieler hat seine properties geschickt
		if (object instanceof PlayerProperties) {
			PlayerProperties properties = (PlayerProperties) object;
			sendingPlayer.setProperties(properties);

			// wenn der spieler seine properties schickt, wird er den anderen
			// clients bekannt gemacht, vorher nicht.
			sendConnectedPlayersToAllClients();
		} else if (object instanceof ChatMessage) {
			chatMessages.add((ChatMessage) object);

			// wenn eine nachricht ankam, aktuelle liste an alle clients schicken
			server.sendToAllTCP(chatMessages);
		}
	}

	public void sendConnectedPlayersToAllClients() {
		server.sendToAllTCP(connectedPlayers);
	}

	public ArrayList<ConnectedPlayer> getConnectedPlayers() {
		return connectedPlayers;
	}

	public ConnectedPlayer getPlayerByConnection(Connection connection) {
		for (int i = 0; i < connectedPlayers.size(); i++) {
			ConnectedPlayer cur = connectedPlayers.get(i);
			if (cur.getConnectionID() == connection.getID()) {
				return cur;
			}
		}

		return null;
	}

	public ArrayList<ChatMessage> getChatMessages() {
		return chatMessages;
	}

}
