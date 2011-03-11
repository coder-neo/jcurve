package gui;

import java.util.Vector;

import org.newdawn.slick.Graphics;

import utils.ResourceManager;
import utils.StaticUtils;

/**
 * Der Chat in der Lobby des Spiels. TODO: scrolling vielleicht?
 * 
 * @author Benjamin
 */
public class GUIChat extends BasicGUIElement {

	private Vector<ChatMessage> messages = new Vector<ChatMessage>();

	private int curWriteLine = 0;
	private int curMaxView = 13;

	public static final int CHAT_PADDING_LEFT = 3;
	public static final int CHAT_PADDING_LINE = 20;
	public static final String CHAT_LINE_BREAK = System.getProperty("line.separator");

	public GUIChat(float x, float y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void render(Graphics g) {
		super.render(g);

		if (messages.size() <= 0)
			return;

		for (int i = 0; i < messages.size(); i++) {
			if (messages.get(i).isVisible())
				ResourceManager.getFont("chatFont").drawString(getX() + CHAT_PADDING_LEFT, getY() + (messages.get(i).getLine() * CHAT_PADDING_LINE), messages.get(i).getText());
		}
	}

	@Override
	public void update(int delta) {
		super.update(delta);

		ChatMessage hiddenOne = null;
		if (curWriteLine > curMaxView) {
			boolean hidden = false;
			for (int i = 0; i < messages.size(); i++) {
				messages.get(i).setLine(messages.get(i).getLine() - 1);
				if (messages.get(i).isVisible() && !hidden) {
					messages.get(i).setVisible(false);
					hiddenOne = messages.get(i);
					hidden = true;
				}
			}
			curWriteLine -= hiddenOne.getLineCount();
		}
	}

	/**
	 * Löscht alle vorhandenen Nachrichten im Chat.
	 */
	public void clear() {
		messages.removeAllElements();
	}

	/**
	 * Fügt eine neue Nachricht des Systems in den Chat hinzu, zB wenn ein
	 * Spieler die Lobby betritt oder verlässt.
	 * 
	 * @param msg
	 *            - die Nachricht
	 */
	public void addSystemMessage(String msg) {
		addMessage("System", msg);
	}

	/**
	 * Fügt eine neue Nachricht in den Chat hinzu. Ist diese zu lang für eine
	 * Zeile, wird sie automatisch umgebrochen.
	 * 
	 * @param name
	 *            - der Spieler, der die Nachricht abschickt
	 * @param msg
	 *            - die Nachricht
	 */
	public void addMessage(String name, String msg) {
		if (msg.trim().isEmpty())
			return;

		msg = "<" + name + ">: " + msg;

		String newMsg = "";
		int strWidth = ResourceManager.getFont("chatFont").getWidth(msg);
		int startWidth = getWidth() - 20;
		int maxWidth = startWidth, curLine = 1;
		if (strWidth >= maxWidth) {
			String[] words = msg.split(" ");
			int curWidth = 0;
			for (int i = 0; i < words.length; i++) {
				curWidth = ResourceManager.getFont("chatFont").getWidth(newMsg.replace(CHAT_LINE_BREAK.toCharArray()[0], ' ')) + ResourceManager.getFont("chatFont").getWidth(words[i]);
				if (curWidth >= maxWidth) {
					curLine++;
					maxWidth += startWidth;
					newMsg += CHAT_LINE_BREAK + words[i];
				} else {
					newMsg += " " + words[i];
				}
			}
		}

		if (newMsg != "")
			msg = newMsg;

		ChatMessage chatMsg = new ChatMessage(msg.trim(), curWriteLine);
		chatMsg.setLineCount(StaticUtils.countCharacter(chatMsg.getText(), CHAT_LINE_BREAK));
		messages.add(chatMsg);

		curWriteLine += curLine;
	}

	/**
	 * Liefert alle momentanen Nachrichten des Chats.
	 * 
	 * @return Vector
	 */
	public Vector<ChatMessage> getMessages() {
		return messages;
	}

	/**
	 * Eine einzelne Nachricht im Chat.
	 * 
	 * @author Benjamin
	 */
	public class ChatMessage {
		private String text;
		private int line;
		private int lineCount;
		private boolean visible = true;

		public ChatMessage(String txt, int line) {
			this.text = txt;
			this.line = line;
		}

		public boolean isVisible() {
			return visible;
		}

		public void setVisible(boolean v) {
			visible = v;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public int getLine() {
			return line;
		}

		public void setLine(int line) {
			this.line = line;
		}

		public int getLineCount() {
			return lineCount;
		}

		public void setLineCount(int lineCount) {
			this.lineCount = lineCount + 1;
		}
	}

}
