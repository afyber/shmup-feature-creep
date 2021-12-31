package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class Player1Bullet extends DynamicObject {

	public Player1Bullet(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "player_bullet";
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "player_bullet";
		collision = "player_bullet";
		ySpeed = -11;
	}

	@Override
	public void update(WorldMiddleman world) {
		if (y < -16) {
			world.instanceDestroy(instanceID);
		}
	}
}
