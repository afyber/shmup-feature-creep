package afyber.shmupfeaturecreep.game.stage1.enemies;

import afyber.shmupfeaturecreep.engine.world.RectangleCollision;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class EnemySmallBulletBW extends EnemyBulletParentBW {

	public double direction;
	public double speed;

	public EnemySmallBulletBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "enemy_small_bullet_bw";
	}

	@Override
	public void create(WorldMiddleman world) {
		collision = new RectangleCollision(3, 3, 3, 3);
		direction = 0;
		speed = 0;
	}

	@Override
	public void draw(WorldMiddleman world) {
		drawRect(x - 4, y - 4, x + 4, y + 4, 0xffffff);
	}

	@Override
	public void update(WorldMiddleman world) {
		x += Math.cos(Math.toRadians(direction)) * speed;
		y += Math.sin(Math.toRadians(direction)) * speed;
	}
}
