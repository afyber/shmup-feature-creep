package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;

public class TestInstanceClass extends DynamicObject {

	private int siner;

	public TestInstanceClass(float x, float y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		spriteIndex = "sprite_2";
		collisionIndex = "sprite_2";
		siner = 0;
	}

	@Override
	public void update() {
		if (Keyboard.keyDown("left")) {
			x -= 1;
		}
		if (Keyboard.keyDown("right")) {
			x += 1;
		}
		if (Keyboard.keyJustDown("down")) {
			y += 10;
		}
		if (Keyboard.keyJustDown("up")) {
			y -= 10;
		}
		imageXScale = (float)(Math.sin(Math.toRadians(siner))) * 3;
		imageYScale = (float)(Math.sin(Math.toRadians(siner + 95))) * 2.4f;
		siner++;
	}

	@Override
	public void draw() {
		drawSelf();
		draw(spriteIndex, x + 16 * imageXScale, y, imageXScale, imageYScale);
	}
}
