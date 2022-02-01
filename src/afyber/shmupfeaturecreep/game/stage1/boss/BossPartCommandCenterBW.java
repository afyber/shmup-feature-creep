package afyber.shmupfeaturecreep.game.stage1.boss;

import afyber.shmupfeaturecreep.engine.MathUtil;
import afyber.shmupfeaturecreep.engine.RandomUtil;
import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.SpriteCollision;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;
import afyber.shmupfeaturecreep.game.Fader;
import afyber.shmupfeaturecreep.game.stage1.enemies.EnemyMineSmallBW;
import afyber.shmupfeaturecreep.game.stage1.enemies.EnemySmallBulletBW;

public class BossPartCommandCenterBW extends BossPartParentBW {

	public double siner = 0;
	public double initialX;
	public boolean dying = false;
	public boolean rising = true;
	public double speed = 5;

	public int attackState = 0;

	public int leftCannon = -1;
	public int rightCannon = -1;

	public BossPartCommandCenterBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "boss_part_command_center_bw";
		initialX = x;
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "boss_part_command_center_bw";
		collision = new SpriteCollision("boss_part_command_center_bw");
		imageXScale = 3;
		imageYScale = 3;
		DynamicObject obj = world.createInstance("boss_part_cannon_bw", x - 191, y - 21, depth);
		((BossPartCannonBW)obj).parentObject = instanceID;
		leftCannon = obj.instanceID;
		obj = world.createInstance("boss_part_cannon_bw", x + 191, y - 21, depth);
		((BossPartCannonBW)obj).parentObject = instanceID;
		rightCannon = obj.instanceID;
		health = -1000;
		alarm[0] = 42;
		alarm[6] = 60;
	}

	@Override
	public void update(WorldMiddleman world) {
		if (rising) {
			y += speed;
		}
		else if (speed > 0) {
			y += speed;
			speed = MathUtil.interpolateExp(speed, 0, 10, 0.2);
		}
		if (!dying) {
			x = initialX + Math.sin(Math.toRadians(siner)) * 20;
			siner += 1.5;
		}
		else {
			x = MathUtil.interpolateExp(x, initialX, 10, 1);
			y -= 0.3;
		}

		if (health == -1000) {
			boolean cannonsAlive = false;
			for (DynamicObject object: world.getObjectList("boss_part_cannon_bw", false)) {
				if (!((BossPartCannonBW)object).disabled) {
					cannonsAlive = true;
					break;
				}
			}
			if (!cannonsAlive && !dying) {
				health = 1000;
			}
		}
	}

	@Override
	public void alarm7(WorldMiddleman world) {
		sprite = "boss_part_command_center_bw_death";
		spriteIndex = 0;
		dying = true;
		health = -1000;
		for (DynamicObject object: world.getObjectList("boss_part_cannon_bw", false)) {
			object.alarm[5] = 20;
		}
		alarm[1] = 75;
		alarm[2] = 10;
		alarm[6] = -1;
	}

	@Override
	public void alarm6(WorldMiddleman world) {
		boolean attacked = false;
		while (!attacked) {
			if (attackState == 0 || attackState == 2) {
				attack(world);
				attacked = true;
			}
			if (attackState == 1 && world.instanceExists(leftCannon) && !((BossPartCannonBW)(world.getObject(leftCannon))).disabled) {
				((BossPartCannonBW)world.getObject(leftCannon)).attack(world);
				attacked = true;
			}
			if (attackState == 3 && world.instanceExists(rightCannon) && !((BossPartCannonBW)(world.getObject(rightCannon))).disabled) {
				((BossPartCannonBW)world.getObject(rightCannon)).attack(world);
				attacked = true;
			}
			attackState++;
			if (attackState >= 4) {
				attackState = 0;
			}
		}
	}

	@Override
	public void alarm0(WorldMiddleman world) {
		rising = false;
	}

	private void attack(WorldMiddleman world) {
		int pattern = RandomUtil.randInt(6);
		switch (pattern) {
			case 0 -> {
				DynamicObject obj = world.createInstance("enemy_mine_small_bw", x - 64, y + 64, 0);
				((EnemyMineSmallBW)obj).speed = 5;
				obj = world.createInstance("enemy_mine_small_bw", x + 64, y + 64, 0);
				((EnemyMineSmallBW)obj).speed = 5;
				alarm[6] = 60;
			}
			case 1 -> {
				DynamicObject obj = world.createInstance("enemy_mine_small_bw", x - 64, y + 64, 0);
				((EnemyMineSmallBW)obj).direction = 110;
				((EnemyMineSmallBW)obj).speed = 5;
				obj = world.createInstance("enemy_mine_small_bw", x + 64, y + 64, 0);
				((EnemyMineSmallBW)obj).direction = 70;
				((EnemyMineSmallBW)obj).speed = 5;
				obj = world.createInstance("enemy_mine_small_bw", x, y + 76, 0);
				((EnemyMineSmallBW)obj).speed = 5;
				alarm[6] = 60;
			}
			case 2 -> {
				DynamicObject obj = world.createInstance("enemy_small_bullet_bw", x + 8, y + 76, 0);
				((EnemySmallBulletBW)obj).direction = 80;
				((EnemySmallBulletBW)obj).speed = 5;
				obj = world.createInstance("enemy_small_bullet_bw", x - 8, y + 76, 0);
				((EnemySmallBulletBW)obj).direction = 100;
				((EnemySmallBulletBW)obj).speed = 5;
				obj = world.createInstance("enemy_small_bullet_bw", x + 32, y + 70, 0);
				((EnemySmallBulletBW)obj).direction = 60;
				((EnemySmallBulletBW)obj).speed = 5;
				obj = world.createInstance("enemy_small_bullet_bw", x - 32, y + 70, 0);
				((EnemySmallBulletBW)obj).direction = 120;
				((EnemySmallBulletBW)obj).speed = 5;
				obj = world.createInstance("enemy_small_bullet_bw", x + 76, y + 64, 0);
				((EnemySmallBulletBW)obj).direction = 50;
				((EnemySmallBulletBW)obj).speed = 5;
				obj = world.createInstance("enemy_small_bullet_bw", x - 76, y + 64, 0);
				((EnemySmallBulletBW)obj).direction = 130;
				((EnemySmallBulletBW)obj).speed = 5;
				alarm[6] = 45;
			}
			case 3 -> {
				DynamicObject obj = world.createInstance("enemy_mine_small_bw", x - 64, y + 64, 0);
				((EnemyMineSmallBW)obj).offset = 45;
				((EnemyMineSmallBW)obj).speed = 5;
				obj = world.createInstance("enemy_mine_small_bw", x + 64, y + 64, 0);
				((EnemyMineSmallBW)obj).offset = 45;
				((EnemyMineSmallBW)obj).speed = 5;
				alarm[6] = 60;
			}
			case 4 -> {
				DynamicObject obj = world.createInstance("enemy_small_bullet_bw", x + 8, y + 76, 0);
				((EnemySmallBulletBW)obj).direction = 89;
				((EnemySmallBulletBW)obj).speed = 5;
				obj = world.createInstance("enemy_small_bullet_bw", x - 8, y + 76, 0);
				((EnemySmallBulletBW)obj).direction = 91;
				((EnemySmallBulletBW)obj).speed = 5;
				obj = world.createInstance("enemy_small_bullet_bw", x + 16, y + 73, 0);
				((EnemySmallBulletBW)obj).direction = 84;
				((EnemySmallBulletBW)obj).speed = 5;
				obj = world.createInstance("enemy_small_bullet_bw", x - 16, y + 73, 0);
				((EnemySmallBulletBW)obj).direction = 96;
				((EnemySmallBulletBW)obj).speed = 5;
				obj = world.createInstance("enemy_small_bullet_bw", x + 32, y + 70, 0);
				((EnemySmallBulletBW)obj).direction = 79;
				((EnemySmallBulletBW)obj).speed = 5;
				obj = world.createInstance("enemy_small_bullet_bw", x - 32, y + 70, 0);
				((EnemySmallBulletBW)obj).direction = 101;
				((EnemySmallBulletBW)obj).speed = 5;
				alarm[6] = 30;
			}
			case 5 -> {
				DynamicObject obj = world.createInstance("enemy_mine_small_bw", x - 64, y + 64, 0);
				((EnemyMineSmallBW)obj).direction = 110;
				((EnemyMineSmallBW)obj).speed = 5;
				obj = world.createInstance("enemy_mine_small_bw", x + 64, y + 64, 0);
				((EnemyMineSmallBW)obj).direction = 70;
				((EnemyMineSmallBW)obj).speed = 5;
				obj = world.createInstance("enemy_mine_small_bw", x, y + 76, 0);
				((EnemyMineSmallBW)obj).offset = 45;
				((EnemyMineSmallBW)obj).speed = 5;
				alarm[6] = 60;
			}
		}
	}

	@Override
	public void alarm1(WorldMiddleman world) {
		alarm[1] = 90;
		spriteIndex++;
		if (!world.instanceExists("fader")) {
			Fader fade = ((Fader)world.createInstance("fader", 0, 0, 1100));
			fade.rgbColor = 0xffffff;
			fade.time = 200;
		}
		if (spriteIndex >= 4) {
			world.changeRoom("roomItsOverLol");
		}
	}

	@Override
	public void alarm2(WorldMiddleman world) {
		world.createInstance("explosion_small_bw", x + RandomUtil.randInt(228) - 116, y + RandomUtil.randInt(228) - 116, 1000);
		world.createInstance("explosion_small_bw", x + RandomUtil.randInt(228) - 116, y + RandomUtil.randInt(228) - 116, 1000);
		Sound.playMusic("small_explosion_bw");
		alarm[2] = RandomUtil.randInt(5, 20);
	}
}
