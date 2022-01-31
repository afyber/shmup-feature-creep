package afyber.shmupfeaturecreep.game.stage1.enemies;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class EnemyMineSmallBW extends DynamicObject {

	public double direction = 90;
	public double speed = 3;
	public int con = 0;
	public double offset = 0;

	public EnemyMineSmallBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "enemy_mine_small_bw";
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "enemy_mine_small_bw";
		imageXScale = 3;
		imageYScale = 3;
		alarm[4] = 30;
		imageSpeed = 0.1;
	}

	@Override
	public void update(WorldMiddleman world) {
		y += Math.sin(Math.toRadians(direction)) * speed;
		x += Math.cos(Math.toRadians(direction)) * speed;
		if (con == 1) {
			world.instanceDestroy(instanceID);
			DynamicObject obj = world.createInstance("enemy_small_bullet_bw", x, y, depth);
			((EnemySmallBulletBW)obj).direction = 45 + offset;
			((EnemySmallBulletBW)obj).speed = 3;
			obj = world.createInstance("enemy_small_bullet_bw", x, y, depth);
			((EnemySmallBulletBW)obj).direction = 135 + offset;
			((EnemySmallBulletBW)obj).speed = 3;
			obj = world.createInstance("enemy_small_bullet_bw", x, y, depth);
			((EnemySmallBulletBW)obj).direction = 225 + offset;
			((EnemySmallBulletBW)obj).speed = 3;
			obj = world.createInstance("enemy_small_bullet_bw", x, y, depth);
			((EnemySmallBulletBW)obj).direction = 315 + offset;
			((EnemySmallBulletBW)obj).speed = 3;
		}
	}

	@Override
	public void alarm4(WorldMiddleman world) {
		con++;
	}
}
