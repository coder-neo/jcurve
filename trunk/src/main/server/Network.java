package main.server;

import java.awt.Point;
import java.util.HashMap;

import main.PlayerOptions;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
	
	public static void registerClasses(EndPoint ep){
		Kryo kryo = ep.getKryo();
		kryo.register(PlayerOptions.class);
		kryo.register(HashMap.class);
		kryo.register(Point.class);
	}

}
