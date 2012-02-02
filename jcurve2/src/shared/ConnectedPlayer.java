package shared;

public class ConnectedPlayer {

	private boolean host = false;

	private int connectionID = -1;
	private PlayerProperties properties = new PlayerProperties();

	public ConnectedPlayer() {
		this(-1);
	}

	public ConnectedPlayer(int connectionID) {
		this.connectionID = connectionID;
	}

	public PlayerProperties getProperties() {
		return properties;
	}

	public void setProperties(PlayerProperties properties) {
		this.properties = properties;
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

	public boolean isHost() {
		return host;
	}

	public void setHost(boolean host) {
		this.host = host;
	}

}
