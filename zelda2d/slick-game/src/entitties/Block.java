package entitties;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Block extends Entity {

	public Block(float x, float y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void render(Graphics g) {
		Color oldColor = g.getColor();
		g.setColor(Color.red);
		g.draw(body);
		g.setColor(oldColor);
	}

	@Override
	public void onCollision(Entity otherEntity) {
	}

}
