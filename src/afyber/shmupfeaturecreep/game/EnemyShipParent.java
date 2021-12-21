package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class EnemyShipParent extends DynamicObject {

	protected int health = -100;

	public EnemyShipParent(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
	}

	@Override
	public void postUpdate(WorldMiddleman world) {
		int collide = world.isColliding(this, "player_bullet", false);
		if (collide > 0 && health != -100 && --health <= 0) {
			world.instanceDestroy(instanceID);
			world.instanceDestroy(collide);
		}
	}
}
