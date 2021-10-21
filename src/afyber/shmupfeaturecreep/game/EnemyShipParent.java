package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class EnemyShipParent extends DynamicObject {

	private int health = 6;

	public EnemyShipParent(float x, float y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
	}

	@Override
	public void preUpdate(WorldMiddleman world) {
		if (world.isColliding(this, Player1Bullet.class)) {
			health -= 1;
			if (health <= 0) {
				world.instanceDestroy(instanceID);
			}
		}
	}
}
