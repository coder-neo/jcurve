package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import main.JCurve;
import shared.ChatMessage;
import shared.ConnectedPlayer;
import shared.GameCommand;
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

	/**
	 * Liefert die IP-Adresse des eigenen Servers.
	 * 
	 * @return {@link InetAddress} - die IP
	 */
	public InetAddress getIP() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Liefert die Kryonet Serverinstanz.
	 * 
	 * @return {@link Server}
	 */
	public Server getKryonetServer() {
		return server;
	}

	/**
	 * Schließt den Server und kappt alle Connections.
	 */
	public void shutdown() {
		server.close();
	}

	@Override
	public void connected(Connection connection) {
		super.connected(connection);

		ConnectedPlayer connectedPlayer = new ConnectedPlayer(connection.getID());
		if (connectedPlayers.isEmpty()) {
			connectedPlayer.setHost(true);
		}
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

		if (object instanceof PlayerProperties) {
			PlayerProperties properties = (PlayerProperties) object;
			sendingPlayer.setProperties(properties);
			// neuen spieler bekannt machen
			sendConnectedPlayersToAllClients();
		} else if (object instanceof ChatMessage) {
			chatMessages.add((ChatMessage) object);
			// neue nachricht bekannt machen
			server.sendToAllTCP(chatMessages);
		} else if (object instanceof GameCommand) {
			GameCommand cmd = (GameCommand) object;
			// befehl des clients ausführen
			JCurve.runningGameState.parseCommand(cmd);
		}
	}

	/**
	 * Sendet eine Liste mit allen Spielern an alle Connections.
	 */
	public void sendConnectedPlayersToAllClients() {
		server.sendToAllTCP(connectedPlayers);
	}

	/**
	 * Liefert eine Liste mit allen Connections.
	 * 
	 * @return {@link ArrayList}
	 */
	public ArrayList<ConnectedPlayer> getConnectedPlayers() {
		return connectedPlayers;
	}

	/**
	 * Liefert einen {@link ConnectedPlayer} anhand des Connection-Objektes
	 * 
	 * @param connection
	 *            - das {@link Connection} Objekt
	 * @return {@link ConnectedPlayer}
	 */
	public ConnectedPlayer getPlayerByConnection(Connection connection) {
		for (int i = 0; i < connectedPlayers.size(); i++) {
			ConnectedPlayer cur = connectedPlayers.get(i);
			if (cur.getConnectionID() == connection.getID()) {
				return cur;
			}
		}

		return null;
	}

	/**
	 * Liefert alle Chatnachrichten.
	 * 
	 * @return {@link ArrayList}
	 */
	public ArrayList<ChatMessage> getChatMessages() {
		return chatMessages;
	}

}
