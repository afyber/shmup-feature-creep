package afyber.shmupfeaturecreep.game.stage1.enemies;

import afyber.shmupfeaturecreep.engine.MathUtil;
import afyber.shmupfeaturecreep.engine.RandomUtil;
import afyber.shmupfeaturecreep.engine.world.RectangleCollision;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class EnemyShipCannonFodderBW extends EnemyShipParentBW {

	public double speed;
	public int timer;

	public EnemyShipCannonFodderBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "enemy_ship_cannon_fodder_bw";
		collision = new RectangleCollision(-2, -1, -2, -2);
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "enemy_ship_cannon_fodder_bw";
		imageXScale = 3;
		imageYScale = 3;
		health = 1;
		speed = 1;
	}

	@Override
	public void update(WorldMiddleman world) {
		speed = MathUtil.interpolateExp(speed, 4, 30, 0.1);
		y += speed;

		timer--;
		if (timer <= 0) {
			world.createParticle("small_enemy_thrust_bw", x + RandomUtil.randInt(-9, 9), y - 8);
			timer = (int)(RandomUtil.randInt(12, 30) / speed);
		}
	}
}
