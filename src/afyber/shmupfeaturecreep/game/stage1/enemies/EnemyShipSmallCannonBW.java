package afyber.shmupfeaturecreep.game.stage1.enemies;

import afyber.shmupfeaturecreep.engine.world.RectangleCollision;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class EnemyShipSmallCannonBW extends EnemyShipParentBW {

	public EnemyShipSmallCannonBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "enemy_ship_small_cannon_bw";
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "enemy_ship_small_cannon_bw";
		collision = new RectangleCollision(0, 0, 0, 0);
	}
}
