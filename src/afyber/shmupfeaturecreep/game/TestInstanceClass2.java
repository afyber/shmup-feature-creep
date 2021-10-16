package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.RandomUtil;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;

public class TestInstanceClass2 extends DynamicObject {

	public TestInstanceClass2(float x, float y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		spriteIndex = "sprite_1";
	}

	@Override
	public void update() {
		x = RandomUtil.randInt(639);
		y = RandomUtil.randInt(399);
	}
}
