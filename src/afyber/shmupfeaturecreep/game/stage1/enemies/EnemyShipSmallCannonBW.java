package afyber.shmupfeaturecreep.game.stage1.enemies;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.engine.MathUtil;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.RectangleCollision;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class EnemyShipSmallCannonBW extends EnemyShipParentBW {

	public boolean left = false;
	public double speed = 1;
	public int timer;
	public int con;

	public EnemyShipSmallCannonBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "enemy_ship_small_cannon_bw";
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "enemy_ship_small_cannon_bw";
		collision = new RectangleCollision(0, 0, 0, 0);
		if (x < Game.WINDOW_WIDTH / 2.0) {
			left = true;
		}
		health = 12;
		imageXScale = left ? 3 : -3;
		imageYScale = 3;
		con = 0;
		alarm[4] = 60;
	}

	@Override
	public void update(WorldMiddleman world) {
		if (con == 0) {
			speed = MathUtil.interpolateExp(speed, 4, 30, 0.1);
			y += speed;
		}
		if (con == 1) {
			alarm[4] = 30;
			con = 2;
		}
		if (con == 2) {
			speed = MathUtil.interpolateExp(speed, 0, 20, 0.15);
			y += speed;
		}
		if (con == 3) {
			alarm[4] = 30;
			con = 4;
		}
		if (con == 5) {
			DynamicObject bul = world.createInstance("enemy_small_bullet_bw", x, y, depth);
			((EnemySmallBulletBW)bul).direction = left ? 65 : 115;
			((EnemySmallBulletBW)bul).speed = 3;
			alarm[4] = 30;
			con = 6;
		}
		if (con == 7) {
			DynamicObject bul = world.createInstance("enemy_small_bullet_bw", x, y, depth);
			((EnemySmallBulletBW)bul).direction = left ? 70 : 110;
			((EnemySmallBulletBW)bul).speed = 3;
			alarm[4] = 30;
			con = 8;
		}
		if (con == 9) {
			DynamicObject bul = world.createInstance("enemy_small_bullet_bw", x, y, depth);
			((EnemySmallBulletBW)bul).direction = left ? 75 : 105;
			((EnemySmallBulletBW)bul).speed = 3;
			con = 10;
		}

		if (con > 4) {
			speed = MathUtil.interpolateExp(speed, 4, 30, 0.1);
			x += left ? -speed : speed;
		}
	}

	@Override
	public void alarm4(WorldMiddleman world) {
		con++;
	}
}
