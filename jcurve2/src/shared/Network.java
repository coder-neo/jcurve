package shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import client.PlayerPoint;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {

	public static void registerClasses(EndPoint ep) {
		Kryo kryo = ep.getKryo();
		kryo.register(Vector.class);
		kryo.register(HashMap.class);
		kryo.register(ArrayList.class);
		kryo.register(PlayerPoint.class);
		kryo.register(GameCommand.class);
		kryo.register(ChatMessage.class);
		kryo.register(ConnectedPlayer.class);
		kryo.register(PlayerProperties.class);
	}

}
