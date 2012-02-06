package gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

import utils.Localization;
import utils.ResourceManager;

public class LifeDisplay extends BasicElement {

	public LifeDisplay(float x, float y, int width, int height) {
		super(x, y, width, height);
		spriteSheet = new SpriteSheet(ResourceManager.getImage("hearts"), 20, 17);
	}

	@Override
	public void render(Graphics g) {
		int hearts = associatedEntity.getHealth();
		int heartWidth = spriteSheet.getSprite(currentSprite, 0).getWidth();

		ResourceManager.getFont("triforce20").drawString(30, 30, "- " + Localization.get("ENERGY").toLowerCase() + " -");

		for (int i = 0; i < hearts; i++) {
			if (i > 5) {
				float lowerRowX = ((i * 3) + x + (i * heartWidth)) - 7 * heartWidth + 2;
				spriteSheet.getSprite(currentSprite, 0).draw(lowerRowX, y + 20, color);
			} else {
				float upperRowX = (i * 3) + x + (i * heartWidth);
				spriteSheet.getSprite(currentSprite, 0).draw(upperRowX, y, color);
			}
		}
	}
}
