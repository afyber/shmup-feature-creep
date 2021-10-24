package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class EnemyShipCannonFodder extends EnemyShipParent {

	public EnemyShipCannonFodder(float x, float y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
	}

	@Override
	public void create(WorldMiddleman world) {
		spriteIndex = "enemy_ship_cannon_fodder";
		collisionIndex = "enemy_ship_cannon_fodder";
		imageXScale = 3;
		imageYScale = 3;
		health = 1;
	}

	@Override
	public void update(WorldMiddleman world) {
		y += 3;
		if (y > MainClass.WINDOW_HEIGHT + 8) {
			world.instanceDestroy(instanceID);
		}
	}
}
