package afyber.shmupfeaturecreep.game.stage1.enemies;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class EnemyShipParentBW extends DynamicObject {

	public int health = -1000;

	public EnemyShipParentBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "enemy_ship_parent_bw";
	}

	@Override
	public void postUpdate(WorldMiddleman world) {
		int collision = world.isColliding(this, "player_bullet_parent_bw");
		if (collision > -1) {
			world.instanceDestroy(collision);
			if (health != -1000) {
				health--;
			}
		}

		if (health <= 0 && health != -1000) {
			world.instanceDestroy(instanceID);
			world.createInstance("explosion_small_bw", x, y, depth);
			Sound.playMusic("small_explosion_bw");
		}

		if (x < -128 || x > Game.WINDOW_WIDTH + 128 || y < -128 || y > Game.WINDOW_HEIGHT + 128) {
			world.instanceDestroy(instanceID);
		}
	}
}
