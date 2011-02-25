package main.client;

import java.io.IOException;

import main.GameConstants;
import main.server.Network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class CurveClient extends Listener {
	private Client client = null;
	private static CurveClient thisObject = null;

	private CurveClient() {
		client = new Client();
		Network.registerClasses(client);
		client.addListener(this);
		client.start();
		try {
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

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		System.out.println(object.toString());
	}
	
	public Client getClient(){
		return client;
	}
	
}
