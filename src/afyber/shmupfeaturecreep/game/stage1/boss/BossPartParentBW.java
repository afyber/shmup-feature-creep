package afyber.shmupfeaturecreep.game.stage1.boss;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class BossPartParentBW extends DynamicObject {

	public int health = -1000;

	public BossPartParentBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "boss_part_parent_bw";
	}

	@Override
	public void postUpdate(WorldMiddleman world) {
		// TODO: SFX for invincible hits and non-invincible hits
		int collision = world.isColliding(this, "player_bullet_parent_bw");
		if (collision > -1) {
			world.instanceDestroy(collision);
			if (health != -1000) {
				health--;
			}
		}

		if (health <= 0 && health != -1000) {
			alarm[7] = 1;
		}
	}
}
