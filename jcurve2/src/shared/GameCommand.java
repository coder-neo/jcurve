package shared;

public class GameCommand {

	private int connectionID = -1;
	private int command;

	public GameCommand() {
		this(-1);
	}

	public GameCommand(int command) {
		this.command = command;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

}
