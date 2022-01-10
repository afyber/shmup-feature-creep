package afyber.shmupfeaturecreep.game.stage1;

import afyber.shmupfeaturecreep.engine.world.RectangleCollision;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class PlayerBulletBasic extends PlayerBulletParentBW {

	public PlayerBulletBasic(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "player_bullet_basic_bw";
		collision = new RectangleCollision(1, 0, 1, 0);
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "player_bullet_bw_1";
	}

	@Override
	public void update(WorldMiddleman world) {
		y -= 11;
	}
}
