package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class PlayerBulletBW extends DynamicObject {

	public PlayerBulletBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "player_bullet_bw";
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "player_bullet_bw_1";
		imageXScale = 2;
		imageYScale = 2;
	}

	@Override
	public void update(WorldMiddleman world) {
		y -= 7;
		if (y < 0) {
			world.instanceDestroy(instanceID);
		}
	}
}
