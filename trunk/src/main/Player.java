package main;

import java.awt.Point;
import java.io.IOException;
import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

import utils.ResourceManager;

import com.esotericsoftware.kryonet.Connection;

/**
 * Die Klasse Player repräsentiert den Spieler bzw. seine "Schlange".
 * Sie beinhaltet statisch alle Spieler, alle Connections, alle Punkte der jeweiligen Schlange,
 * sowie alle relevanten Spielerinformationen.



 * 
 * @author Adam Laszlo
 *

 */
public class Player {
	private static Vector<Player> players = new Vector<Player>();
	private Connection connection;
//	private Vector<PlayerPoint> points = new Vector<PlayerPoint>();
//	private String name;
//	private int score;
//	private Color color;
//	private Image image;





	private PlayerProperties properties;
	private PlayerPoint nextPoint;
	private float angle = 70;
	private float speed = 3;
	private boolean dirLeft = false;
	private boolean dirRight = false;
	private boolean isReady = false;
	
	private boolean boost = false;

	private boolean isWrappedUp = false;
	private ParticleSystem boostSystem = null;
	private ConfigurableEmitter emitter = null;

	/**
	 * Erstellt einen neuen Player und knüpft ihn an eine Connection

	 * @param connection
	 */
	public Player(Connection connection){
		this.connection = connection;
		properties = new PlayerProperties();
		initPlayerPosition();
		properties = new PlayerProperties(connection.getID());
//		this.image = ResourceManager.getImage("laser");
		properties.setImageKey("laser");
//		properties.setImage(ResourceManager.getImage("laser"));
		initPlayerPosition();
		players.add(this);
		try {
			boostSystem = new ParticleSystem("data/emitters/particle.tga", 1000);
			emitter = ParticleIO.loadEmitter("data/emitters/boostEmitter.xml");
			emitter.setEnabled(false);
			boostSystem.addEmitter(emitter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initPlayerPosition(){
		int x = (int)Math.round((Math.random()*(GameConstants.APP_WIDHT-50)));
		int y = (int)Math.round((Math.random()*(GameConstants.APP_HEIGHT-50)));
		int middleX = GameConstants.APP_WIDHT/2;
		int middleY = GameConstants.APP_HEIGHT/2;
		if (x > middleX){
			angle = (float) (Math.atan((middleY-y)/(double)(middleX-x))-Math.PI);














		} else {
			angle = (float) Math.atan((middleY-y)/(double)(middleX-x));

		}
//		points.add(new PlayerPoint(x,y, angle));
		properties.getPoints().add(new PlayerPoint(x,y, angle));
		PlayerPoint pp = new PlayerPoint(x,y, angle);
		properties.getPoints().add(pp);
		nextPoint = pp;
	}
	

	public void render(Graphics g) {
		properties.render(g);
//		if (boost){
			boostSystem.render();
//		}
	}
	
	public void update(int delta){
		emitter.setPosition(nextPoint.x, nextPoint.y);
		boostSystem.update(delta);
	}
	
	/**
	 * Berechnet den neuen Punkt für die Schlange.

	 * @return false, wenn eine Kollision stattgefunden hat
	 * @author Adam Laszlo
	 */
	public boolean move(){

		computeNextPoint();
		if (boost) {
			computeNextPoint();
		}
		return !checkCollision();
	}
	
	private void computeNextPoint(){
		Point lastPoint = properties.getPoints().lastElement();
		int deltaX = (int)Math.round(Math.cos(angle)*speed);
		int deltaY = (int)Math.round(Math.sin(angle)*speed);


		int nextX = lastPoint.x + deltaX;
		int nextY = lastPoint.y + deltaY;
		nextPoint = new PlayerPoint(nextX+deltaX, nextY+deltaY, angle);
		if (dirLeft) {
			angle -= .1;
		} else if (dirRight){

			angle += .1;
		}
		PlayerPoint nextPoint = new PlayerPoint(nextX, nextY, angle);
		properties.getPoints().add(nextPoint);
		return !checkCollision();
		PlayerPoint newPoint = new PlayerPoint(nextX, nextY, angle);
		properties.getPoints().add(newPoint);
		
	}
	

	/**
	 * Prüft die Kollision der Schlange mit sich selbst und den anderen.

	 * @author Adam Laszlo
	 * @return true, wenn Kollision stattgefunden hat
	 */
	private boolean checkCollision(){
		for (int i = 0; i < players.size(); i++){


			Player other = players.get(i);
			int end = other.getProperties().getPoints().size();
			if (this == other){
				// damit die Schlange nicht in jedem Durchgang mit den vorherigen Punkten kollidiert,
				// werden die letzten Punkte ignoriert, wenn die eigene Schlange geprüft wird.
				end -= 5;	






			}
			// ----- grobe Prüfung -----
			for (int j = 0; j < end; j++){

				PlayerPoint p = other.getProperties().getPoints().get(j);
				if (this.getProperties().getPoints().lastElement().distance(p) < 10){

					// ----- pixelgenaue Prüfung -----
					Image img = ResourceManager.getImage(properties.getImageKey());
					Shape rect = new Rectangle(p.x, p.y, img.getWidth(), img.getHeight());
					rect = rect.transform(Transform.createRotateTransform(p.getAngle(), rect.getCenterX(), rect.getCenterY()));
					Shape rect2 = new Rectangle(this.getProperties().getPoints().lastElement().x, properties.getPoints().lastElement().y, img.getWidth(), img.getHeight());
					rect2 = rect2.transform(Transform.createRotateTransform(this.getProperties().getPoints().lastElement().getAngle(), rect2.getCenterX(), rect2.getCenterY()));
					if (rect.intersects(rect2)){

						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void steerLeft(){


		dirLeft = true;
		dirRight = false;
	}
	
	public void steerRight(){


		dirLeft = false;
		dirRight = true;
	}
	
	public void steerStraight(){


		dirLeft = false;
		dirRight = false;
	}
	

	public void setBoost(boolean b) {
		if (!b) {
			if (!isWrappedUp && emitter.isEnabled()){
				emitter.wrapUp();
				isWrappedUp = true;
			}
		} else {
			if (isWrappedUp){
				isWrappedUp = false;
				try {
					emitter = ParticleIO.loadEmitter("data/emitters/boostEmitter.xml");
					boostSystem.removeAllEmitters();
					boostSystem.addEmitter(emitter);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				emitter.setEnabled(true);
				
			}
		}
		boost = b;
	}
	
	public boolean isBoost(){
		return boost;
	}
	
	// ------------------- Getter & Setter ---------------------
	
//	public Vector<PlayerPoint> getPoints() {
//		return points;
//	}
//	
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public int getScore() {
//		return score;
//	}
//	public void setScore(int score) {
//		this.score = score;
//	}
//	public Color getColor() {
//		return color;
//	}
//	public void setColor(Color color) {
//		this.color = color;
//	}
	
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

//	public Image getImage() {
//		return image;
//	}
//
//	public void setImage(Image image) {
//		this.image = image;
//	}

	public static Vector<Player> getPlayers() {
		return players;
	}

	public PlayerProperties getProperties() {
		return properties;
	}
}
