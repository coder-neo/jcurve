package physics;

import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.Circle;
import org.newdawn.fizzy.CompoundShape;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.Shape;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import utils.ResourceManager;

public class Renderer {
	private Image img;

	public Renderer() throws SlickException { // throws SlickException
		img = ResourceManager.getImage("linkStanding01");
	}

	/**
	 * Draw a body to the canvas
	 * 
	 * @param g
	 *            The graphics context on which to draw
	 * @param body
	 *            The body to be rendered
	 * @throws SlickException
	 */
	public void drawBody(Graphics g, Body body) throws SlickException {
		Shape shape = body.getShape();
		drawShape(g, body, shape);
	}

	/**
	 * Draw a shape
	 * 
	 * @param g
	 *            The graphics context to render to
	 * @param body
	 *            The body to be rendered
	 * @param shape
	 *            The shape representing the body
	 * @throws SlickException
	 */
	private void drawShape(Graphics g, Body body, Shape shape) throws SlickException {
		if (shape instanceof Circle) {
			drawCircle(g, body, (Circle) shape);
		}

		if (shape instanceof Rectangle) {
			drawRectangle(g, body, (Rectangle) shape);
		}
		/*
		 * if (shape instanceof Polygon) { drawPolygon(g, body, (Polygon) shape); } if (shape instanceof CompoundShape) { drawCompound(g, body, (CompoundShape) shape); }
		 */
	}

	/**
	 * Draw a compound shape
	 * 
	 * @param g
	 *            The graphics context to render to
	 * @param body
	 *            The body to be rendered
	 * @param shape
	 *            The shape representing the body
	 * @throws SlickException
	 */
	private void drawCompound(Graphics g, Body body, CompoundShape shape) throws SlickException {
		int count = shape.getShapeCount();
		for (int i = 0; i < count; i++) {
			drawShape(g, body, shape.getShape(i));
		}
	}

	/**
	 * Draw a body represented by a circle
	 * 
	 * @param g
	 *            The graphics context to render to
	 * @param body
	 *            The body to be rendered
	 * @param shape
	 *            The shape representing the body
	 */
	private void drawCircle(Graphics g, Body body, Circle shape) {
		// ympyrän piirto oikeaan paikkaan
		// otetaan huomioon radius, x, y, rotation
		// piirron lopussa nollataan globaalit arvot jotta seuraava objekti piirtyy oikein

		// g.rotate(body.getX(), body.getY(), body.getRotation());
		g.drawOval(body.getX() - (shape.getRadius()), body.getY() - (shape.getRadius()), shape.getRadius() * 2, shape.getRadius() * 2);
		// g.rotate(body.getX(), body.getY(), -body.getRotation());
	}

	/**
	 * Draw a body represented by a rectangle
	 * 
	 * @param g
	 *            The graphics context on which to render
	 * @param body
	 *            The body to be rendered
	 * @param shape
	 *            The shape representing the body
	 * @throws SlickException
	 */

	private void drawRectangle(Graphics g, Body body, Rectangle shape) throws SlickException {

		// img.draw(body.getX()-(shape.getWidth()/2), body.getY()-(shape.getHeight()/2));
		// float toDegree = (float) Math.toDegrees(body.getRotation());
		// img.setRotation( (float) Math.toDegrees(body.getRotation()) );

		// hacky way of doing things
		g.rotate(body.getX(), body.getY(), (float) Math.toDegrees(body.getRotation())); // JBOX USES RADIANS!!!
//		g.drawImage(img, body.getX() - (shape.getWidth() / 2), body.getY() - (shape.getHeight() / 2));
		g.drawRect(body.getX() - (shape.getWidth() / 2), body.getY() - (shape.getHeight() / 2), shape.getWidth(), shape.getHeight());
		g.rotate(body.getX(), body.getY(), -(float) Math.toDegrees(body.getRotation()));
		/*
		 * g = (Graphics2D) g.create(); g.translate(body.getX(), body.getY()); g.rotate(body.getRotation()); g.translate(shape.getXOffset(), shape.getYOffset()); g.rotate(shape.getAngleOffset());
		 * 
		 * float width = shape.getWidth(); float height = shape.getHeight();
		 * 
		 * g.setColor(Color.black); g.drawRect((int) -(width/2),(int) -(height/2),(int) width,(int) height);
		 */
	}
}
