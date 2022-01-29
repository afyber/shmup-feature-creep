package afyber.shmupfeaturecreep.game.stage1.enemies;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.engine.MathUtil;
import afyber.shmupfeaturecreep.engine.RandomUtil;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.RectangleCollision;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class EnemyShipTinyCannonBW extends EnemyShipParentBW {

	public double speed;
	public int timer;
	public boolean left = false;

	public EnemyShipTinyCannonBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "enemy_ship_tiny_cannon_bw";
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "enemy_ship_tiny_cannon_bw";
		collision = new RectangleCollision(-3, -3, -9, -6);
		if (x < Game.WINDOW_WIDTH / 2.0) {
			left = true;
		}
		imageXScale = left ? 3 : -3;
		imageYScale = 3;
		health = 4;
		speed = 1;
		alarm[4] = 45;
	}

	@Override
	public void update(WorldMiddleman world) {
		speed = MathUtil.interpolateExp(speed, 4, 30, 0.1);
		y += speed;

		timer--;
		if (timer <= 0) {
			world.createParticle("small_enemy_thrust_bw", x + RandomUtil.randInt(-6, 6), y - 8);
			timer = (int)(RandomUtil.randInt(6, 30) / speed);
		}
	}

	@Override
	public void alarm4(WorldMiddleman world) {
		DynamicObject bul = world.createInstance("enemy_small_bullet_bw", x + (left ? 6 : -6), y - 3, depth);
		((EnemySmallBulletBW)bul).direction = left ? 65 : 115;
		((EnemySmallBulletBW)bul).speed = 5;
		alarm[4] = 48;
	}
}
