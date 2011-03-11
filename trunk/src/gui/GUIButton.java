package gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

import utils.ResourceManager;

public class GUIButton extends BasicGUIElement {

	private int width, height;

	private GUIContext context = null;
	private MouseOverArea area = null;

	public GUIButton(String text, GUIContext context, float x, float y) {
		super(x, y, ResourceManager.getFont("standard").getWidth(text), ResourceManager.getFont("standard").getHeight(text));

		this.context = context;
		this.width = ResourceManager.getFont("standard").getWidth(text);
		this.height = ResourceManager.getFont("standard").getHeight(text);

		Image image = null;
		try {
			image = new Image(width, height);
			image.getGraphics().drawString(text, 10, 10);
		} catch (SlickException e) {
			e.printStackTrace();
		}

		Rectangle shape = new Rectangle(x, y, width, height);
		area = new MouseOverArea(context, image, shape);
	}

	@Override
	public void render(Graphics g) {
		super.render(g);

		area.render(context, g);
	}

}
