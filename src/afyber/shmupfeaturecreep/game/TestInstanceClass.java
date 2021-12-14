package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class TestInstanceClass extends DynamicObject {

	private int siner;

	public TestInstanceClass(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "test_1";
	}

	@Override
	public void create(WorldMiddleman world) {
		spriteIndex = "sprite_2";
		collisionIndex = "sprite_2";
		siner = 0;
	}

	@Override
	public void update(WorldMiddleman world) {
		imageXScale = Math.sin(Math.toRadians(siner)) * 3;
		//imageYScale = (float)(Math.sin(Math.toRadians(siner + 95))) * 2.4f;
		siner++;
	}

	@Override
	public void draw(WorldMiddleman world) {
		drawSelf();
		/*if (world.isColliding(this, TestInstanceClass3.class)) {
			drawSimple("sprite_3", 320, 0);
		}*/
	}
}
