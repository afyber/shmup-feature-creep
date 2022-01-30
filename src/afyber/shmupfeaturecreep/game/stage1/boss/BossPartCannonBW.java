package afyber.shmupfeaturecreep.game.stage1.boss;

import afyber.shmupfeaturecreep.engine.MathUtil;
import afyber.shmupfeaturecreep.engine.world.SpriteCollision;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

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
		health = 100;
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
}
