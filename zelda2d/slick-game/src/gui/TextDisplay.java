package gui;

import gui.base.BasicElement;
import main.Writer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import utils.ResourceManager;

public class TextDisplay extends BasicElement {

	private Writer writer;

	public TextDisplay(String text, float x, float y, int width, int height) {
		super(x, y, width, height);
		writer = new Writer(text, ResourceManager.getFont("triforce20"), 1);
		writer.setElement(this);
		writer.setColor(Color.red);
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		writer.update(delta);
	}

	@Override
	public void render(Graphics g) {
		super.render(g);
		writer.draw(g);
	}

}
