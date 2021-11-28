package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.sound.Sounds;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class EnemyShipParent extends DynamicObject {

	protected int health = -100;

	public EnemyShipParent(float x, float y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
	}

	@Override
	public void postUpdate(WorldMiddleman world) {
		if (world.isColliding(this, "player_bullet", false) && health != -100 && --health <= 0) {
				world.instanceDestroy(instanceID);
				Sounds.playSound("test.wav");
		}
	}
}
