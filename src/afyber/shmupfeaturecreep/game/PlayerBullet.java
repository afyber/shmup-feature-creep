package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class PlayerBullet extends DynamicObject {

	public PlayerBullet(float x, float y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
	}

	@Override
	public void create(WorldMiddleman world) {
		spriteIndex = "player_bullet";
		collisionIndex = "player_bullet";
	}

	@Override
	public void alarm1(WorldMiddleman world) {
		world.instanceDestroy(instanceID);
	}

	@Override
	public void update(WorldMiddleman world) {
		y -= 14;
	}
}
