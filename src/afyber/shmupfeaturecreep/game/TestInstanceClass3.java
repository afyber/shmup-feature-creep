package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;

public class TestInstanceClass3 extends DynamicObject {

	public TestInstanceClass3(float x, float y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		spriteIndex = "sprite_3";
		collisionIndex = "sprite_3";
		imageXScale = 3;
	}
}
