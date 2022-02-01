package afyber.shmupfeaturecreep.game.stage1.enemies;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.engine.RandomUtil;
import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.Global;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;
import afyber.shmupfeaturecreep.game.stage1.PlayerBulletParentBW;
import afyber.shmupfeaturecreep.game.stage1.PlayerShipBW;

public class EnemyShipParentBW extends DynamicObject {

	public int health = -1000;
	public int chance = 30;

	public EnemyShipParentBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "enemy_ship_parent_bw";
	}

	@Override
	public void postUpdate(WorldMiddleman world) {
		int collision = world.isColliding(this, "player_bullet_parent_bw");
		if (collision > -1) {
			world.instanceDestroy(collision);
			if (health != -1000) {
				health -= ((PlayerBulletParentBW)world.getObject(collision)).damage;
			}
		}

		if (health <= 0 && health != -1000) {
			world.instanceDestroy(instanceID);
			world.createInstance("explosion_small_bw", x, y, depth);
			if (RandomUtil.randInt(chance) == 0) {
				PlayerShipBW ship = (PlayerShipBW)world.getObjectList("player_ship_bw", false).get(0);
				if (ship.powerUp < Global.getIntGlobal("powerupsUnlock") && !world.instanceExists("weapon_powerup_bw")) {
					world.createInstance("weapon_powerup_bw", x, y, 101);
				}
				else if (ship.fireRatePowerUp < Global.getIntGlobal("boostsUnlock") && !world.instanceExists("fire_rate_powerup_bw")) {
					world.createInstance("fire_rate_powerup_bw", x, y, 101);
				}
			}
			Sound.playMusic("small_explosion_bw");
		}

		if (x < -128 || x > Game.WINDOW_WIDTH + 128 || y < -128 || y > Game.WINDOW_HEIGHT + 128) {
			world.instanceDestroy(instanceID);
		}
	}
}
