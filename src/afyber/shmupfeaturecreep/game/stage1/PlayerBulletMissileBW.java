package afyber.shmupfeaturecreep.game.stage1;

import afyber.shmupfeaturecreep.engine.MathUtil;
import afyber.shmupfeaturecreep.engine.world.RectangleCollision;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class PlayerBulletMissileBW extends PlayerBulletParentBW {

	public double speed = 2;

	public PlayerBulletMissileBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "player_bullet_missile_bw";
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "player_bullet_missile_bw";
		collision = new RectangleCollision(0, -3, 0, -3);
		imageXScale = 3;
		imageYScale = 3;
		damage = 10;
	}

	@Override
	public void update(WorldMiddleman world) {
		y -= speed;
		speed = MathUtil.interpolateExp(speed, 9, 15, 1);
	}

	@Override
	public void destroy(WorldMiddleman world) {
		if (y > -32) {
			world.createInstance("explosion_small_bw", x, y, depth);
		}
	}
}
