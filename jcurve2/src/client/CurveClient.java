package client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import main.JCurve;
import shared.ChatMessage;
import shared.ConnectedPlayer;
import shared.GameConstants;
import shared.Network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class CurveClient extends Listener {

	private static CurveClient object = null;
	private static Client client = null;

	private ArrayList<ConnectedPlayer> connectedPlayers = new ArrayList<ConnectedPlayer>();
	private ArrayList<ChatMessage> chatMessages = new ArrayList<ChatMessage>();

	public static CurveClient getInstance() {
		if (object == null) {
			object = new CurveClient();
		}

		return object;
	}

	public Client getClient() {
		return client;
	}

	private CurveClient() {
		client = new Client();
		Network.registerClasses(client);
		client.addListener(this);
		client.start();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void received(Connection connection, Object object) {
		super.received(connection, object);

		if (object instanceof ArrayList) {
			ArrayList<Object> list = (ArrayList<Object>) object;
			if (list.get(0) instanceof ConnectedPlayer) {
				ArrayList<ConnectedPlayer> playersFromServer = (ArrayList<ConnectedPlayer>) object;
				connectedPlayers = playersFromServer;
			} else if (list.get(0) instanceof ChatMessage) {
				ArrayList<ChatMessage> messagesFromServer = (ArrayList<ChatMessage>) object;
				chatMessages = messagesFromServer;
			}
		}
	}

	private void sendProperties() {
		client.sendTCP(JCurve.userData);
	}

	public void sendChatMessage(String text) {
		ChatMessage msg = new ChatMessage();
		msg.setName(JCurve.userData.getName());
		msg.setText(text);
		client.sendTCP(msg);
	}

	public ArrayList<ConnectedPlayer> getConnectedPlayers() {
		return connectedPlayers;
	}

	public ArrayList<ChatMessage> getChatMessages() {
		return chatMessages;
	}

	public void connect(InetAddress ip) {
		try {
			client.connect(5000, ip, GameConstants.PORT_TCP, GameConstants.PORT_UDP);
			sendProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connect(String hostName) {
		try {
			client.connect(5000, hostName, GameConstants.PORT_TCP, GameConstants.PORT_UDP);
			sendProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
