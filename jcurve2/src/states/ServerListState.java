package states;

import gui.GUIButton;
import gui.GUIServerList;

import java.net.InetAddress;
import java.util.List;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import shared.GameConstants;
import utils.ResourceManager;
import client.CurveClient;

/**
 * In der ServerListState sieht der User alle laufenden Server im LAN und kann
 * dann auf einen Server seiner Wahl verbinden und gelangt so in die
 * entsprechende LobbyState.
 * 
 * @author Benjamin
 */
public class ServerListState extends JCurveState {

	private Vector<GUIButton> serverButtons = new Vector<GUIButton>();
	private Vector<String> addedButtonNames = new Vector<String>();

	private static final String MSG_WAIT = "Looking for servers, please wait";
	private int dotDelta = 0;
	private int maxDotDelta = 500;
	private int curDotPos = 1;

	private GUIServerList serverList = null;
	private GUIButton buttonSearch = null;
	private boolean isSearching;

	public ServerListState(int id) {
		super(id);
	}

	@Override
	public void init(GameContainer container, final StateBasedGame game) throws SlickException {
		super.init(container, game);

		serverList = new GUIServerList(100, 200, 400, GameConstants.APP_HEIGHT - 400);
		serverList.setBorder(Color.red);

		buttonSearch = new GUIButton("Start search", container, GameConstants.APP_WIDHT / 2 - 150, GameConstants.APP_HEIGHT - 100);
		buttonSearch.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent source) {
				resetServerList();
				searchServer();
			}
		});

		GUIButton buttonCancel = new GUIButton("Cancel", container, GameConstants.APP_WIDHT / 2 + 100, GameConstants.APP_HEIGHT - 100);
		buttonCancel.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent source) {
				game.enterState(GameConstants.STATE_MAIN_MENU);
			}
		});

		addGUIElements(serverList, buttonCancel, buttonSearch);
	}

	/**
	 * Leert die aktuelle Serverliste und setzt den Suchzustand zurück auf die
	 * Ausgangsposition.
	 */
	private void resetServerList() {
		serverList.clear();
		curDotPos = 1;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		searchServer();
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		resetServerList();
	}

	@Override
	public void update(GameContainer container, final StateBasedGame game, int delta) throws SlickException {
		super.update(container, game, delta);

		// Aus allen vorhandenen Server werden Buttons gebastelt
		for (int i = 0; i < serverList.getServer().size(); i++) {
			String name = serverList.getServer().get(i).getName() + " (" + serverList.getServer().get(i).getIp().getHostAddress() + ")";
			if (addedButtonNames.contains(name))
				continue;

			final GUIButton button = new GUIButton(name, container, -GameConstants.APP_WIDHT, -GameConstants.APP_HEIGHT);
			button.setValue(serverList.getServer().get(i).getIp());
			button.addListener(new ComponentListener() {
				@Override
				public void componentActivated(AbstractComponent source) {
					connectToServer((InetAddress) button.getValue(), game);
				}
			});

			addedButtonNames.add(name);
			serverButtons.add(button);
			addGUIElements(button);
		}

		dotDelta += delta;
		if (dotDelta >= maxDotDelta) {
			dotDelta = 0;
			curDotPos++;
			if (curDotPos > 3)
				curDotPos = 1;
		}
	}

	/**
	 * Verbindet den Spieler zu einem Server.
	 * 
	 * @param ipAddress
	 *            - die IP-Adresse
	 * @param game
	 *            - das StateBasedGame-Objekt
	 */
	private void connectToServer(InetAddress ipAddress, StateBasedGame game) {
		CurveClient.getInstance().connect(ipAddress);
		game.enterState(GameConstants.STATE_LOBBY);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);

		ResourceManager.getFont("header").drawString(100, 100, "Available servers", Color.red);

		// Server gefunden (min. 1)
		if (serverList.getServer() != null && serverList.getServer().size() > 0) {
			int startY = 215;
			for (int i = 0; i < serverButtons.size(); i++) {
				serverButtons.get(i).getMouseOverArea().setLocation(115, startY);
				startY += 30;
			}
		}
		// keine Server gefunden
		else if (serverList.getServer() != null && serverList.getServer().size() <= 0 && !isSearching) {
			ResourceManager.getFont("standard").drawString(115, 215, "No open servers");
		}

		// Suche läuft noch
		if (isSearching) {
			container.getInput().pause();

			Rectangle blacker = new Rectangle(0, 0, GameConstants.APP_WIDHT, GameConstants.APP_HEIGHT);
			Color blackerColor = new Color(0, 0, 0, .3f);
			Color oldColor = g.getColor();
			g.setColor(blackerColor);
			g.fill(blacker);
			g.setColor(oldColor);

			int strWidth = ResourceManager.getFont("standard").getWidth(MSG_WAIT) / 2;
			ResourceManager.getFont("standard").drawString(GameConstants.APP_WIDHT / 2 - strWidth, GameConstants.APP_HEIGHT / 2, MSG_WAIT + " " + LobbyState.MSG_WAIT_DOTS.substring(0, curDotPos));
		} else {
			container.getInput().resume();
		}
	}

	/**
	 * Sucht nach offenen Server im LAN und gibt diese als List zurück.
	 */
	private void searchServer() {
		isSearching = true;
		Thread serverSearch = new Thread(new Runnable() {
			@Override
			public void run() {
				List<InetAddress> server = getClient().discoverHosts(GameConstants.PORT_UDP, 1000);
				if (server != null && server.size() > 0)
					serverList.updateServerList(server);

				isSearching = false;
			}
		});
		serverSearch.start();
	}

}
