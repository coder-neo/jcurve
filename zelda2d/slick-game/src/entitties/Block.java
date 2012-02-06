package entitties;

import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.StaticBody;

public class Block extends Entity {

	public Block(float x, float y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void onCollision(Entity otherEntity) {
	}

	@Override
	public void initBody() {
		body = new StaticBody<Object>(new Rectangle(width, height), x + width / 2, y + height / 2);
		body.setFriction(1f);
		body.setRestitution(0f);
	}

}
