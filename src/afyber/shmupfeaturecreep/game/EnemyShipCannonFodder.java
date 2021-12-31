package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class EnemyShipCannonFodder extends EnemyShipParent {

	public EnemyShipCannonFodder(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "enemy_ship_cannon_fodder";
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "enemy_ship_cannon_fodder";
		collision = "enemy_ship_cannon_fodder";
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
