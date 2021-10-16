package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.RandomUtil;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class TestInstanceClass2 extends DynamicObject {

	public TestInstanceClass2(float x, float y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
	}

	@Override
	public void create(WorldMiddleman world) {
		spriteIndex = "sprite_1";
	}

	@Override
	public void update(WorldMiddleman world) {
		x = RandomUtil.randInt(639);
		y = RandomUtil.randInt(399);
	}
}
