package afyber.shmupfeaturecreep.game.stage1;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class ExplosionSmallBW extends DynamicObject {

	public ExplosionSmallBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "explosion_small_bw";
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "explosion_small_bw";
		imageSpeed = 0.5;
		imageXScale = 3;
		imageYScale = 3;
	}

	@Override
	public void update(WorldMiddleman world) {
		if (spriteIndex > 4) {
			world.instanceDestroy(instanceID);
		}
	}
}
