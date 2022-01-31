package afyber.shmupfeaturecreep.game.stage1;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.Global;
import afyber.shmupfeaturecreep.engine.world.RectangleCollision;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class FireRatePowerupBW extends DynamicObject {

	public FireRatePowerupBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "fire_rate_powerup_bw";
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "powerup_fire_rate_bw";
		collision = new RectangleCollision(6, 6, 6, 6);
		imageXScale = 3;
		imageYScale = 3;
	}

	@Override
	public void update(WorldMiddleman world) {
		int collision = world.isColliding(this, "player_ship_bw");
		if (collision > -1) {
			PlayerShipBW ship = ((PlayerShipBW)world.getObject(collision));
			if (ship.fireRatePowerUp < Global.getIntGlobal("boostsUnlock")) {
				ship.fireRatePowerUp++;
				world.instanceDestroy(instanceID);
			}
		}
		y += 5;
	}
}
