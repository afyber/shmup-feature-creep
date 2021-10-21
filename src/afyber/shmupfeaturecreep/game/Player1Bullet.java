package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class Player1Bullet extends DynamicObject {

	public Player1Bullet(float x, float y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
	}

	@Override
	public void create(WorldMiddleman world) {
		spriteIndex = "player_bullet";
		collisionIndex = "player_bullet";
		imageXScale = 1;
		imageYScale = 1;
		alarm[0] = 60;
	}

	@Override
	public void alarm1(WorldMiddleman world) {
		world.instanceDestroy(instanceID);
	}

	@Override
	public void update(WorldMiddleman world) {
		y -= 11;
	}
}
