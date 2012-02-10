package main;

import gui.base.BasicElement;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Sound;

import utils.ResourceManager;

public class Writer {

	private static Sound typeSound = ResourceManager.getSound("typeLetter");

	private String text;
	private Font font;
	private Color color = Color.white;
	private BasicElement element;

	private int speed = 1;
	private int letterDelta = 50;
	private int curDelta, curPos, length, paddingX, paddingY;

	private boolean finished = false;

	public Writer(String text, Font font, int speed) {
		this.text = text;
		this.speed = speed;
		this.font = font;
		this.length = text.length();
	}

	public void draw(Graphics g) {
		if (element == null) {
			return;
		}

		font.drawString(element.getX(), element.getY(), text.substring(0, curPos), color);
	}

	public void update(int delta) {
		if (finished) {
			return;
		}

		curDelta += delta;
		if (curDelta >= letterDelta) {
			curDelta = 0;
			curPos += 1;
			typeSound.play();
			if (curPos > length) {
				curPos = length;
				finished = true;
			}
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public BasicElement getElement() {
		return element;
	}

	public void setElement(BasicElement element) {
		this.element = element;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getPaddingX() {
		return paddingX;
	}

	public void setPaddingX(int paddingX) {
		this.paddingX = paddingX;
	}

	public int getPaddingY() {
		return paddingY;
	}

	public void setPaddingY(int paddingY) {
		this.paddingY = paddingY;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
