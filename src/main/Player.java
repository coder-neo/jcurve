package main;

import java.awt.Point;
import java.io.IOException;
import java.util.Vector;

import main.powerup.Powerup;
import main.powerup.PowerupBoost;
import main.powerup.PowerupShot;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

import utils.ResourceManager;

import com.esotericsoftware.kryonet.Connection;

/**
 * Die Klasse Player repräsentiert den Spieler bzw. seine "Schlange". Sie beinhaltet statisch alle Spieler, alle Connections, alle Punkte der jeweiligen Schlange, sowie alle relevanten Spielerinformationen.
 * 
 * @author Adam Laszlo
 * 
 */
public class Player {
	private static Vector<Player> players = new Vector<Player>();
	private Connection connection;
	private PlayerProperties properties;
	private PlayerPoint nextPoint;
	private float angle = 70;
	private float speed = 3;
	private boolean dirLeft = false;
	private boolean dirRight = false;
	private boolean isReady = false;
	private boolean alive = true;
	private int dyingTimeout = 1000;
	private int dyingInterval = 80;
	private int dyingDelta = 0;
	private int tmpColorCode;
	private int dyingColorCode = 0xffffff;
	
	private int numBullets = 1;
	private int maxBullets = 3;
	
	private float boostLevel = 0.4f;

	private boolean boost = false;

	private boolean isWrappedUp = false;
	private ParticleSystem particleSystem = null;
	private ConfigurableEmitter boostEmitter = null;

	private int explosionDeltaMax = 3;
	private int explosionDeltaCur = 0;
	private boolean exploding = false;
	private boolean wrappedUp = false;
	private ParticleSystem explosionSystem = null;

	private Vector<Bullet> bullets = new Vector<Bullet>();

	public Player() {
		properties = new PlayerProperties();
		properties.setImageKey("laser");
		players.add(this);
	}

	/**
	 * Erstellt einen neuen Player und knüpft ihn an eine Connection
	 * 
	 * @param connection
	 */
	public Player(Connection connection) {
		this.connection = connection;
		properties = new PlayerProperties(connection.getID());
		properties.setImageKey("laser");
		initPlayerPosition();
		players.add(this);
		try {
			particleSystem = new ParticleSystem("data/emitters/particle.tga", 1000);
			boostEmitter = ParticleIO.loadEmitter("data/emitters/boostEmitter.xml");
			explosionSystem = ParticleIO.loadConfiguredSystem("data/emitters/explosionSystem.xml");
			boostEmitter.setEnabled(false);
			particleSystem.addEmitter(boostEmitter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initPlayerPosition() {
		int x = (int) Math.round((Math.random() * (GameConstants.APP_WIDHT - 50)));
		int y = (int) Math.round((Math.random() * (GameConstants.APP_HEIGHT - 50)));
		int middleX = GameConstants.APP_WIDHT / 2;
		int middleY = GameConstants.APP_HEIGHT / 2;
		if (x > middleX) {
			angle = (float) (Math.atan((middleY - y) / (double) (middleX - x)) - Math.PI);
		} else {
			angle = (float) Math.atan((middleY - y) / (double) (middleX - x));
		}
		properties.getPoints().add(new PlayerPoint(x, y, angle));
		PlayerPoint pp = new PlayerPoint(x, y, angle);
		properties.getPoints().add(pp);
		nextPoint = pp;
	}

	public void render(Graphics g) {
		properties.render(g);
		try {
			particleSystem.render();
		} catch (NullPointerException e) {
			System.out.println("HIER KACKT ER AB (render)");
			System.out.println(particleSystem.getEmitterCount());
			for (int i = 0; i < particleSystem.getEmitterCount(); i++) {
				System.out.println("----- Emitter " + i + ": " + particleSystem.getEmitter(i));
			}
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		if (exploding) {
			explosionSystem.render();
		}
		drawBoostBar(g);
		drawBulletBar(g);
	}
	
	private void drawBulletBar(Graphics g) {
		ResourceManager.getFont("small").drawString(7, 33, "Bullets:");
		Image img = ResourceManager.getImage("shotThumb");
		for (int i = 0; i < numBullets; i++){
			g.drawImage(img, 80+(i*20), 38);
		}
	}

	private void drawBoostBar(Graphics g){
		ResourceManager.getFont("small").drawString(7, 5, "Boost:");
		g.setColor(Color.gray);
		g.drawRect(80, 13, 150, 10);
		g.setColor(new Color((1-boostLevel), boostLevel, 0));
		g.fillRect(81, 14, 149*boostLevel, 9);
	}

	public void update(int delta) {
		if (!alive){
			if (dyingTimeout > 0){
				dyingTimeout -= delta;
				dyingDelta += delta;
				if (dyingDelta > dyingInterval){
					dyingDelta = 0;
					if (properties.getColorCode() == dyingColorCode){
						properties.setColorCode(tmpColorCode);
					} else if (properties.getColorCode() == tmpColorCode) {
						properties.setColorCode(dyingColorCode);
					}
				}
			} else {
				properties.setColorCode(tmpColorCode);
			}
			return;
		}
		boostEmitter.setPosition(nextPoint.x, nextPoint.y);
		try {
			particleSystem.update(delta);
		} catch (NullPointerException e) {
			System.out.println("HIER KACKT ER AB (update)");
			System.out.println(particleSystem.getEmitterCount());
			for (int i = 0; i < particleSystem.getEmitterCount(); i++) {
				System.out.println("----- Emitter " + i + ": " + particleSystem.getEmitter(i));
			}
		}
		if (exploding) {
			explosionSystem.update(delta);
			explosionDeltaCur++;
			if (explosionDeltaCur > explosionDeltaMax) {
				explosionDeltaCur = 0;
				explosionSystem.getEmitter(0).wrapUp();
				explosionSystem.getEmitter(1).wrapUp();
				wrappedUp = true;
			}
		}
		if (boost){
			boostLevel -= 0.004f;
			if (boostLevel < 0){
				boostLevel = 0;
			}
		}
		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			b.update(delta);
			if (!b.isInsideScreen()) {
				bullets.remove(b);
			}
		}
	}

	/**
	 * Berechnet den neuen Punkt für die Schlange.
	 * 
	 * @return false, wenn eine Kollision stattgefunden hat
	 * @author Adam Laszlo
	 */
	public boolean move() {
		if (!alive){
			return true;
		}
		computeNextPoint();
		if (boost && boostLevel > 0) {
			computeNextPoint();
		}
		return !checkCollision();
	}

	private void computeNextPoint() {
		Point lastPoint = properties.getPoints().lastElement();
		int deltaX = (int) Math.round(Math.cos(angle) * speed);
		int deltaY = (int) Math.round(Math.sin(angle) * speed);

		int nextX = lastPoint.x + deltaX;
		int nextY = lastPoint.y + deltaY;
		if (dirLeft) {
			angle -= .12;
		} else if (dirRight) {

			angle += .12;
		}
		PlayerPoint comingPoint = new PlayerPoint(nextX, nextY, angle);
		nextPoint = new PlayerPoint(nextX + deltaX, nextY + deltaY, angle);
		properties.getPoints().add(comingPoint);
	}

	/**
	 * Prüft die Kollision der Schlange mit sich selbst und den anderen.
	 * 
	 * @author Adam Laszlo
	 * @return true, wenn Kollision stattgefunden hat
	 */
	private boolean checkCollision() {
		if (checkCollisionBounds()){
			return true;
		}
		Image img = ResourceManager.getImage(properties.getImageKey());
		for (int i = 0; i < players.size(); i++) {
			Player other = players.get(i);
			int end = other.getProperties().getPoints().size();
			if (this == other) {
				// damit die Schlange nicht in jedem Durchgang mit den vorherigen Punkten kollidiert,
				// werden die letzten Punkte ignoriert, wenn die eigene Schlange geprüft wird.
				end -= 5;
			}
			// ----- grobe Prüfung -----
			for (int j = 0; j < end; j++) {
				PlayerPoint p = other.getProperties().getPoints().get(j);
				if (!p.isActive()) {
					continue;
				}
				if (this.getProperties().getPoints().lastElement().distance(p) < 10) {
					// ----- pixelgenaue Prüfung -----
					Shape rect = new Rectangle(p.x, p.y, img.getWidth(), img.getHeight());
					rect = rect.transform(Transform.createRotateTransform(p.getAngle(), rect.getCenterX(), rect.getCenterY()));
					Shape rect2 = new Rectangle(this.getProperties().getPoints().lastElement().x, properties.getPoints().lastElement().y, img.getWidth(), img.getHeight());
					rect2 = rect2.transform(Transform.createRotateTransform(this.getProperties().getPoints().lastElement().getAngle(), rect2.getCenterX(), rect2.getCenterY()));
					if (rect.intersects(rect2)) {
						return true;
					}
				}
			}
			checkCollisionBullets();
			checkCollisionPowerups();
		}
		return false;
	}

	private boolean checkCollisionBounds() {
		Point lastPoint = properties.getPoints().lastElement();
		if (lastPoint.x < 0 || lastPoint.x > GameConstants.APP_WIDHT || lastPoint.y < 0 || lastPoint.y > GameConstants.APP_HEIGHT){
			return true;
		}
		return false;
	}

	private void checkCollisionBullets() {
		for (int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			for (int j = 0; j < p.getBullets().size(); j++) {
				Bullet b = p.getBullets().get(j);
				int numPoints = p.getProperties().getPoints().size();
				// falls die Schlange weniger als 10 Elemente hat, wird keine Kollision erkannt
				if (numPoints < 10) {
					continue;
				}
				for (int k = 0; k < numPoints - 10; k++) {
					PlayerPoint pp = p.getProperties().getPoints().get(k);
					if (!pp.isActive()) {
						continue;
					}
					if (b.getPosition().distance(pp) < 13) {
						// Schuss kollidiert mit Schlange
						p.getBullets().remove(b);
						for (int l = -7; l < 7; l++) {
							if (p.getProperties().getPoints().size() > k + l && k + l > 0) {
								p.getProperties().getPoints().get(k + l).setActive(false);
							}
						}
						initExplosion(b.getPosition());
						return;
					}
				}
			}
		}
	}

	private void checkCollisionPowerups() {
		Image img = ResourceManager.getImage(properties.getImageKey());
		PlayerPoint pp = properties.getPoints().lastElement();
		Shape rect = new Rectangle(pp.x, pp.y, img.getWidth(), img.getHeight());
		rect = rect.transform(Transform.createRotateTransform(pp.getAngle(), rect.getCenterX(), rect.getCenterY()));
		for (int j = 0; j < Powerup.getPowerups().size(); j++) {
			Powerup pu = Powerup.getPowerups().get(j);
			Shape circle = new RoundedRectangle(pu.getPosition().x - pu.getImage().getWidth()/2, pu.getPosition().y - pu.getImage().getHeight()/2, pu.getImage().getWidth(), pu.getImage().getHeight(), pu.getImage().getWidth()/2-1);
			if (rect.intersects(circle)) {
				Powerup.getPowerups().remove(pu);
				if (pu instanceof PowerupBoost){
					boostLevel += .5f;
					if (boostLevel > 1){
						boostLevel = 1;
					}
				} else if (pu instanceof PowerupShot){
					if (numBullets < maxBullets){
						numBullets++;
					}
				}
			}
		}
	}

	private void initExplosion(PlayerPoint point) {
		if (wrappedUp) {
			try {
				explosionSystem = ParticleIO.loadConfiguredSystem("data/emitters/explosionSystem.xml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		exploding = true;
		explosionSystem.setPosition(point.x, point.y);
	}

	public void steerLeft() {
		dirLeft = true;
		dirRight = false;
	}

	public void steerRight() {
		dirLeft = false;
		dirRight = true;
	}

	public void steerStraight() {
		dirLeft = false;
		dirRight = false;
	}

	public void shoot() {
		if (alive){
			if (numBullets > 0){
				bullets.add(new Bullet(nextPoint));
				numBullets--;
			}
		}
	}

	public void setBoost(boolean b) {
		if (!b || boostLevel <= 0) {
			if (!isWrappedUp && boostEmitter.isEnabled()) {
				boostEmitter.wrapUp();
				isWrappedUp = true;
			}
		} else {
			if (isWrappedUp) {
				isWrappedUp = false;
				try {
					boostEmitter = ParticleIO.loadEmitter("data/emitters/boostEmitter.xml");
					// boostSystem.removeAllEmitters();
					particleSystem.addEmitter(boostEmitter);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				boostEmitter.setEnabled(true);

			}
		}
		boost = b;
	}
	
	public void die() {
		tmpColorCode = properties.getColorCode();
		if (tmpColorCode == 0xffffff){
			dyingColorCode = 0x000000;
		}
		alive = false;
	}

	// ------------------- Getter & Setter ---------------------

	public boolean isBoost() {
		return boost;
	}

	public float getAngle() {
		return angle;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public static Vector<Player> getPlayers() {
		return players;
	}

	public PlayerProperties getProperties() {
		return properties;
	}

	public Vector<Bullet> getBullets() {
		return bullets;
	}

	public boolean isAlive() {
		return alive;
	}
}
