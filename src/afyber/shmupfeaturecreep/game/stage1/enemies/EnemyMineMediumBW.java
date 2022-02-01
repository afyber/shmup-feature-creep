package afyber.shmupfeaturecreep.game.stage1.enemies;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.RectangleCollision;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class EnemyMineMediumBW extends EnemyShipParentBW {

	public double direction = 90;
	public double speed = 3;
	public int con = 0;

	public EnemyMineMediumBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "enemy_mine_medium_bw";
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "enemy_mine_medium_bw";
		collision = new RectangleCollision(-6, -6, -6, -6);
		imageXScale = 3;
		imageYScale = 3;
		if (y < 0) {
			alarm[4] = 45;
		}
		else {
			alarm[4] = 30;
		}
		imageSpeed = 0.1;
		health = -1000;
	}

	@Override
	public void update(WorldMiddleman world) {
		y += Math.sin(Math.toRadians(direction)) * speed;
		x += Math.cos(Math.toRadians(direction)) * speed;
		if (con == 1) {
			world.instanceDestroy(instanceID);
			DynamicObject obj = world.createInstance("enemy_small_bullet_bw", x, y, depth);
			((EnemySmallBulletBW)obj).direction = 0;
			((EnemySmallBulletBW)obj).speed = 3;
			obj = world.createInstance("enemy_small_bullet_bw", x, y, depth);
			((EnemySmallBulletBW)obj).direction = 45;
			((EnemySmallBulletBW)obj).speed = 3;
			obj = world.createInstance("enemy_small_bullet_bw", x, y, depth);
			((EnemySmallBulletBW)obj).direction = 90;
			((EnemySmallBulletBW)obj).speed = 3;
			obj = world.createInstance("enemy_small_bullet_bw", x, y, depth);
			((EnemySmallBulletBW)obj).direction = 135;
			((EnemySmallBulletBW)obj).speed = 3;
			obj = world.createInstance("enemy_small_bullet_bw", x, y, depth);
			((EnemySmallBulletBW)obj).direction = 180;
			((EnemySmallBulletBW)obj).speed = 3;
			obj = world.createInstance("enemy_small_bullet_bw", x, y, depth);
			((EnemySmallBulletBW)obj).direction = 225;
			((EnemySmallBulletBW)obj).speed = 3;
			obj = world.createInstance("enemy_small_bullet_bw", x, y, depth);
			((EnemySmallBulletBW)obj).direction = 270;
			((EnemySmallBulletBW)obj).speed = 3;
			obj = world.createInstance("enemy_small_bullet_bw", x, y, depth);
			((EnemySmallBulletBW)obj).direction = 315;
			((EnemySmallBulletBW)obj).speed = 3;
		}
	}

	@Override
	public void alarm4(WorldMiddleman world) {
		con++;
	}
}
