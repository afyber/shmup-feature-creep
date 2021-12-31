package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class TestInstanceClass3 extends DynamicObject {

	public TestInstanceClass3(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "vertical_test_pattern";
		collision = "vertical_test_pattern";
		imageXScale = 3;
	}
}
