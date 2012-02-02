package shared;

import java.util.ArrayList;
import java.util.Vector;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {

	public static void registerClasses(EndPoint ep) {
		Kryo kryo = ep.getKryo();
		kryo.register(Vector.class);
		kryo.register(ArrayList.class);
		kryo.register(ChatMessage.class);
		kryo.register(ConnectedPlayer.class);
		kryo.register(PlayerProperties.class);
	}

}
