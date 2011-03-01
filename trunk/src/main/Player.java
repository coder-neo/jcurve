package main;

import java.awt.Point;
import java.util.Vector;

import org.newdawn.slick.Color;

import com.esotericsoftware.kryonet.Connection;

public class Player {
	private Connection connection;
	private Vector<Point> points = new Vector<Point>();
	private String name;
	private int score;
	private Color color;
	private float angle = 70;
	private float lastAngle;
	private float speed = 3;
	private boolean dirLeft = false;
	private boolean dirRight = false;
	private boolean isReady = false;
	
	public Player(Connection connection){
		this.connection = connection;
		lastAngle = angle;
		points.add(new Point(50,50));
		points.add(new Point(51,51));
	}
	
	public void move(){
		Point lastPoint = points.lastElement();
		int deltaX = (int)Math.round(Math.cos(angle)*speed);
		int deltaY = (int)Math.round(Math.sin(angle)*speed);
		int nextX = lastPoint.x + deltaX;
		int nextY = lastPoint.y + deltaY;
		if (dirLeft) {
			angle -= .1;
		} else if (dirRight){
			angle += .1;
		}
		if (lastAngle == angle){
			points.remove(points.size()-1);
		}
		lastAngle = angle;
		Point nextPoint = new Point(nextX, nextY);
		points.add(nextPoint);
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
	
	public Vector<Point> getPoints() {
		return points;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
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
}
