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
	private float angle;
	private float speed;
	
	public Player(String name, Connection connection) {
		this.name = name;
		this.connection = connection;
	}
	
	public Player(Connection connection){
		this.connection = connection;
	}
	
	public void move(){
//		Math.
//		Point nextPoint = new 
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
	public void setAngle(float angle) {
		this.angle = angle;
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
}
