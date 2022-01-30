package afyber.shmupfeaturecreep.game.stage1.boss;

import afyber.shmupfeaturecreep.engine.MathUtil;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.SpriteCollision;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class BossPartCommandCenterBW extends BossPartParentBW {

	public double siner = 0;
	public double initialX;
	public boolean dying = false;
	public boolean rising = true;
	public double speed = 5;

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
		obj = world.createInstance("boss_part_cannon_bw", x + 191, y - 21, depth);
		((BossPartCannonBW)obj).parentObject = instanceID;
		health = -1000;
		alarm[0] = 42;
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
	}

	@Override
	public void alarm0(WorldMiddleman world) {
		rising = false;
	}
}
