package gui;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Speichert alle offenen Server in der ServerListState.
 * 
 * @author Benjamin
 */
public class GUIServerList extends BasicGUIElement {

	private Vector<ServerEntry> server = new Vector<ServerEntry>();

	public GUIServerList(float x, float y, int width, int height) {
		super(x, y, width, height);
	}

	/**
	 * Aktualisiert die Liste von allen offenen Server.
	 * 
	 * @param list
	 *            - die List mit den Server.
	 */
	public void updateServerList(List<InetAddress> list) {
		Iterator<InetAddress> iter = list.iterator();
		while (iter.hasNext()) {
			ServerEntry serverEntry = new ServerEntry(iter.next());
			server.add(serverEntry);
		}
	}

	/**
	 * Liefert alle vorhandenen Server.
	 * 
	 * @return List
	 */
	public Vector<ServerEntry> getServer() {
		return server;
	}

	/**
	 * Leert die Serverliste.
	 */
	public void clear() {
		if (server == null)
			return;

		server.clear();
	}

	public class ServerEntry {
		private InetAddress ip = null;
		private String name;

		public ServerEntry(InetAddress ip) {
			this.ip = ip;
			this.name = ip.getCanonicalHostName();
		}

		public InetAddress getIp() {
			return ip;
		}

		public void setIp(InetAddress ip) {
			this.ip = ip;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

}
