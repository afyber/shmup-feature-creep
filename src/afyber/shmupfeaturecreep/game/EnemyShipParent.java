package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class EnemyShipParent extends DynamicObject {

	protected int health = -100;

	public EnemyShipParent(float x, float y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
	}

	@Override
	public void postUpdate(WorldMiddleman world) {
		if (world.isColliding(this, Player1Bullet.class)) {
			if (health != -100) {
				health -= 1;
				if (health <= 0) {
					world.instanceDestroy(instanceID);
				}
			}
		}
	}
}
