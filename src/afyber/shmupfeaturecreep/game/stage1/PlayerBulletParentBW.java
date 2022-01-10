package afyber.shmupfeaturecreep.game.stage1;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class PlayerBulletParentBW extends DynamicObject {

	public PlayerBulletParentBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "player_bullet_parent_bw";
	}

	@Override
	public void postUpdate(WorldMiddleman world) {
		if (y < -32 || y > Game.WINDOW_HEIGHT + 32 || x < -32 || x > Game.WINDOW_WIDTH + 32) {
			world.instanceDestroy(instanceID);
		}
	}
}
