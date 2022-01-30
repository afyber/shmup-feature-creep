package afyber.shmupfeaturecreep.game.stage1.boss;

import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class BossPartParentBW extends DynamicObject {

	public int health = -1000;
	public boolean doNextFlash = false;
	public int flashTimer = 0;
	public boolean flash;

	public BossPartParentBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "boss_part_parent_bw";
	}

	@Override
	public void postUpdate(WorldMiddleman world) {
		int collision = world.isColliding(this, "player_bullet_parent_bw");
		if (collision > -1) {
			world.instanceDestroy(collision);
			if (health != -1000) {
				health--;
				if (health <= 0) {
					alarm[7] = 1;
				}
				Sound.playSound("boss_hit_effective_bw");
				Sound.setSoundGain("boss_hit_effective_bw", 0.3);
				doNextFlash = true;
				if (flashTimer <= 0) {
					flashTimer = 10;
				}
			}
			else {
				Sound.playSound("boss_hit_ineffective_bw");
				Sound.setSoundGain("boss_hit_ineffective_bw", 0.2);
			}
		}

		if (flashTimer > 0) {
			flashTimer--;
			if (flashTimer <= 0 && doNextFlash) {
				flashTimer = 10;
				doNextFlash = false;
			}
		}
		flash = flashTimer <= 5;
	}

	@Override
	public void draw(WorldMiddleman world) {
		drawExtended(sprite, spriteIndex, x, y, imageXScale, imageYScale, flash ? 1 : 0.8);
	}
}
