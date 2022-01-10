package afyber.shmupfeaturecreep.game.stage1;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class EnemyShipParentBW extends DynamicObject {

	public int health = -100;

	public EnemyShipParentBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "enemy_ship_parent_bw";
	}

	@Override
	public void postUpdate(WorldMiddleman world) {
		int collision = world.isColliding(this, "player_bullet_parent_bw");
		if (collision > -1) {
			world.instanceDestroy(collision);
			if (health != -100) {
				health--;
				if (health <= 0) {
					world.instanceDestroy(instanceID);
					world.createInstance("explosion_small_bw", x, y, depth);
				}
			}
		}
	}
}
