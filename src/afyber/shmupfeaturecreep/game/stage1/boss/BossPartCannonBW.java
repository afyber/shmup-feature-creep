package afyber.shmupfeaturecreep.game.stage1.boss;

import afyber.shmupfeaturecreep.engine.MathUtil;
import afyber.shmupfeaturecreep.engine.RandomUtil;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.SpriteCollision;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;
import afyber.shmupfeaturecreep.game.stage1.enemies.EnemyMineSmallBW;
import afyber.shmupfeaturecreep.game.stage1.enemies.EnemySmallBulletBW;

public class BossPartCannonBW extends BossPartParentBW {

	public int parentObject;
	public boolean left;
	public boolean disabled;
	public boolean dive;
	public double speed = 1;

	public BossPartCannonBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "boss_part_cannon_bw";
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "boss_part_cannon_bw";
		collision = new SpriteCollision("boss_part_cannon_bw");
		left = x < 320;
		imageXScale = left ? 3 : -3;
		imageYScale = 3;
		health = 700;
	}

	@Override
	public void update(WorldMiddleman world) {
		if (!dive) {
			x = world.getObject(parentObject).x + (left ? -191 : 191);
			y = world.getObject(parentObject).y - 21;
		}
		else {
			y -= speed;
			if (y < -128) {
				world.instanceDestroy(instanceID);
			}
			speed = MathUtil.interpolateExp(speed, 10, 10, 0.25);
		}
	}

	@Override
	public void alarm7(WorldMiddleman world) {
		spriteIndex = 1;
		disabled = true;
		health = -1000;
	}

	@Override
	public void alarm5(WorldMiddleman world) {
		dive = true;
		xSpeed = left ? -1 : 1;
	}

	public void attack(WorldMiddleman world) {
		int pattern = RandomUtil.randInt(4);
		switch (pattern) {
			case 0 -> {
				DynamicObject obj = world.createInstance("enemy_small_bullet_bw", left ? x + 12 : x - 12, y + 16, 0);
				((EnemySmallBulletBW)obj).direction = 55;
				((EnemySmallBulletBW)obj).speed = 4;
				obj = world.createInstance("enemy_small_bullet_bw", left ? x + 12 : x - 12, y + 16, 0);
				((EnemySmallBulletBW)obj).direction = 80;
				((EnemySmallBulletBW)obj).speed = 4;
				obj = world.createInstance("enemy_small_bullet_bw", left ? x + 12 : x - 12, y + 16, 0);
				((EnemySmallBulletBW)obj).direction = 100;
				((EnemySmallBulletBW)obj).speed = 4;
				obj = world.createInstance("enemy_small_bullet_bw", left ? x + 12 : x - 12, y + 16, 0);
				((EnemySmallBulletBW)obj).direction = 125;
				((EnemySmallBulletBW)obj).speed = 4;
				world.getObject(parentObject).alarm[6] = 45;
			}
			case 1 -> {
				world.createInstance("enemy_mine_small_bw", left ? x + 12 : x - 12, y + 16, 0);
				world.getObject(parentObject).alarm[6] = 45;
			}
			case 2 -> {
				DynamicObject obj = world.createInstance("enemy_mine_small_bw", left ? x + 12 : x - 12, y + 16, 0);
				((EnemyMineSmallBW)obj).direction = 115;
				((EnemyMineSmallBW)obj).speed = 5;
				obj = world.createInstance("enemy_mine_small_bw", left ? x + 12 : x - 12, y + 16, 0);
				((EnemyMineSmallBW)obj).direction = 65;
				((EnemyMineSmallBW)obj).speed = 5;
				world.getObject(parentObject).alarm[6] = 55;
			}
			case 3 -> {
				DynamicObject obj = world.createInstance("enemy_mine_small_bw", left ? x + 12 : x - 12, y + 16, 0);
				((EnemyMineSmallBW)obj).direction = 115;
				((EnemyMineSmallBW)obj).offset = 45;
				((EnemyMineSmallBW)obj).speed = 5;
				obj = world.createInstance("enemy_mine_small_bw", left ? x + 12 : x - 12, y + 16, 0);
				((EnemyMineSmallBW)obj).direction = 65;
				((EnemyMineSmallBW)obj).offset = 45;
				((EnemyMineSmallBW)obj).speed = 5;
				world.getObject(parentObject).alarm[6] = 60;
			}
		}
	}
}
