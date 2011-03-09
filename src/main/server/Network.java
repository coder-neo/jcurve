package main.server;

import java.awt.Point;
import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Color;

import main.PlayerOptions;
import main.PlayerPoint;
import main.PlayerProperties;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Server;

public class Network {
	
	public static void registerClasses(EndPoint ep){
		Kryo kryo = ep.getKryo();
		kryo.register(PlayerOptions.class);
		kryo.register(HashMap.class);
		kryo.register(PlayerPoint.class);
		kryo.register(Point.class);
		kryo.register(Vector.class);
		kryo.register(Server.class);
		kryo.register(Color.class);
		kryo.register(PlayerProperties.class);
	}

}
