package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class EnemyShip1L extends EnemyShipParent {

	private float targetX;
	private float targetY;
	private float speed;
	private boolean endOfPath;

	public EnemyShip1L(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "enemy_ship_1_L";
		health = 40;
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "enemy_ship_1_L";
		collision = "enemy_ship_1_L";
		imageXScale = 2;
		imageYScale = 2;
		targetX = 200;
		targetY = 400;
		endOfPath = false;
		speed = 3;
	}

	@Override
	public void update(WorldMiddleman world) {
		if (x != targetX && Math.abs(x - targetX) >= speed) {
			x += x < targetX ? speed : -speed;
		}
		if (y != targetY && Math.abs(y - targetY) >= speed) {
			y += y < targetY ? speed : -speed;
		}
		if (Math.abs(x - targetX) < speed && Math.abs(y - targetY) < speed && !endOfPath) {
			endOfPath = true;
			alarm[1] = 20;
			alarm[0] = 120;
		}
	}

	@Override
	public void alarm0(WorldMiddleman world) {
		world.instanceDestroy(instanceID);
	}

	@Override
	public void alarm1(WorldMiddleman world) {
		targetX -= 400;
	}
}
