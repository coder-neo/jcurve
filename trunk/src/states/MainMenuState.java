package states;

import gui.GUIButton;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import main.GameConstants;
import main.JCurve;
import main.Player;
import main.PlayerPoint;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.muffin.FileMuffin;
import org.newdawn.slick.state.StateBasedGame;

import utils.ResourceManager;

/**
 * Die erste State, die der Spieler sieht. Hier kann er ein Spiel starten oder
 * auf ein bestehendes joinen (beitreten). Auch kann er diverse Optionen
 * anpassen, wie seinen Namen und seine Spielerfarbe. Als kleines Gimmick huscht
 * rechts auf dem Bildschirm eine Schlange umher.
 * 
 * @author Benjamin
 */
public class MainMenuState extends JCurveState {

	private static final int BORDER_MAX_DISTANCE = 50;

	private String lastDirection = null;
	private Random random = new Random();
	private Player bot = null;
	private Color botColor = null;

	private int curMoveDelta = 0;
	private int maxMoveDelta = 10;

	public MainMenuState(int id) {
		super(id);

		ResourceManager.addImage("laser", "data/images/laser.png");

		bot = new Player();
		bot.getProperties().getPoints().add(new PlayerPoint((GameConstants.APP_WIDHT / 2) + 140, 200, 0));
		botColor = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));

		readConfigFile();
	}

	@Override
	public void init(GameContainer container, final StateBasedGame game) throws SlickException {
		super.init(container, game);

		GUIButton buttonPlay = new GUIButton("Neues Spiel starten", container, 100, 250);
		buttonPlay.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent source) {
				game.enterState(GameConstants.STATE_LOBBY);
			}
		});

		GUIButton buttonJoin = new GUIButton("Spiel beitreten", container, 100, 300);
		buttonJoin.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent source) {
				game.enterState(GameConstants.STATE_SERVER_LIST);
			}
		});

		GUIButton buttonOptions = new GUIButton("Optionen", container, 100, 350);
		buttonOptions.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent source) {
				game.enterState(GameConstants.STATE_OPTIONS);
			}
		});

		GUIButton buttonQuit = new GUIButton("Beenden", container, 100, 450);
		buttonQuit.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent source) {
				JCurve.app.exit();
			}
		});

		addGUIElements(buttonPlay, buttonJoin, buttonOptions, buttonQuit);
	}

	private void readConfigFile() {
		FileMuffin file = new FileMuffin();
		try {
			HashMap<Object, Object> data = file.loadFile(GameConstants.APP_LOCAL_OPTIONS_FILENAME);
			Iterator<Object> it = data.values().iterator();
			while (it.hasNext()) {
				System.out.println(it.next());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);

		renderBotSnake(g);

		ResourceManager.getFont("header").drawString(100, 100, GameConstants.APP_NAME, Color.red);

		int strWidth = ResourceManager.getFont("small").getWidth(GameConstants.APP_VERSION) / 2;
		ResourceManager.getFont("small").drawString(GameConstants.APP_WIDHT - (strWidth * 2) - 20, GameConstants.APP_HEIGHT - strWidth, GameConstants.APP_VERSION);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		super.update(container, game, delta);

		updateBotSnake(delta);
	}

	/**
	 * Zeichnet eine Schlange, die im Hauptmenü zufällig durch den Screen
	 * huscht.
	 * 
	 * @param g
	 *            - Graphics-Objekt
	 */
	private void renderBotSnake(Graphics g) {
		Image tmpImg = null;
		Player p = bot;
		for (int i = 0; i < p.getProperties().getPoints().size() - 1; i++) {
			tmpImg = ResourceManager.getImage(p.getProperties().getImageKey()).copy();
			tmpImg.setRotation((float) Math.toDegrees(p.getProperties().getPoints().get(i).getAngle()));
			g.drawImage(tmpImg, p.getProperties().getPoints().get(i).x, p.getProperties().getPoints().get(i).y, botColor);
		}
	}

	/**
	 * Updated die Richtung der Schlange.
	 * 
	 * @param delta
	 *            - ms seit letztem Update
	 */
	private void updateBotSnake(int delta) {
		bot.steerStraight();

		int borderLeft = GameConstants.APP_WIDHT / 2;
		int borderRight = GameConstants.APP_WIDHT;

		PlayerPoint lastPoint = bot.getProperties().getPoints().lastElement();

		double angle = Math.toDegrees(lastPoint.getAngle());
		angle %= 360;

		boolean atBorder = false;

		// rechts
		if (lastPoint.x + BORDER_MAX_DISTANCE > borderRight) {
			atBorder = true;
			if (lastDirection == null) {
				if (angle > 0 && angle < 270) {
					bot.steerRight();
					lastDirection = "RIGHT";
				} else {
					bot.steerLeft();
					lastDirection = "LEFT";
				}
			} else {
				if (lastDirection.equals("RIGHT"))
					bot.steerRight();
				else
					bot.steerLeft();
			}
		}
		// links
		else if (lastPoint.x - BORDER_MAX_DISTANCE < borderLeft) {
			atBorder = true;
			if (lastDirection == null) {
				if (angle > 180) {
					bot.steerRight();
					lastDirection = "RIGHT";
				} else {
					bot.steerLeft();
					lastDirection = "LEFT";
				}
			} else {
				if (lastDirection.equals("RIGHT"))
					bot.steerRight();
				else
					bot.steerLeft();
			}
		}
		// unten
		else if (lastPoint.y + BORDER_MAX_DISTANCE > GameConstants.APP_HEIGHT) {
			atBorder = true;
			if (lastDirection == null) {
				if (angle > 90 && angle < 180) {
					bot.steerRight();
					lastDirection = "RIGHT";
				} else {
					bot.steerLeft();
					lastDirection = "LEFT";
				}
			} else {
				if (lastDirection.equals("RIGHT"))
					bot.steerRight();
				else
					bot.steerLeft();
			}
		}
		// oben
		else if (lastPoint.y - BORDER_MAX_DISTANCE < 0) {
			atBorder = true;
			if (lastDirection == null) {
				if (angle > 270) {
					bot.steerRight();
					lastDirection = "RIGHT";
				} else {
					bot.steerLeft();
					lastDirection = "LEFT";
				}
			} else {
				if (lastDirection.equals("RIGHT"))
					bot.steerRight();
				else
					bot.steerLeft();
			}
		}

		if (!atBorder) {
			lastDirection = null;
			curMoveDelta++;
			if (curMoveDelta >= maxMoveDelta) {
				curMoveDelta = 0;
				if (Math.random() > .5)
					bot.steerLeft();
				else
					bot.steerRight();
			}
		}

		bot.move();
	}

}
