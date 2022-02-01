package afyber.shmupfeaturecreep.game.stage1;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.RectangleCollision;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;
import afyber.shmupfeaturecreep.game.stage1.enemies.EnemyShipParentBW;

public class PlayerShipBW extends DynamicObject {

	public int iFrames;
	public int health;
	public int powerUp;
	public int speedPowerUp;
	public int fireRatePowerUp;

	public int bulletCounter;

	public PlayerShipBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "player_ship_bw";
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "player_ship_bw_1";
		collision = new RectangleCollision(-12, -9, -12, -6);
		imageXScale = 3;
		imageYScale = 3;
		iFrames = 0;
		health = 5;
		powerUp = 0;
		speedPowerUp = 0;
		fireRatePowerUp = 0;
	}

	@Override
	public void update(WorldMiddleman world) {
		if (world.instanceExists("guild_upgrade_menu_bw")) {
			return;
		}

		int moveFactor = 6;
		if (speedPowerUp == 1) {
			moveFactor = 7;
		}
		if (speedPowerUp == 2) {
			moveFactor = 8;
		}
		if (Keyboard.keyDown("up")) {
			y = Math.max(y - moveFactor, 0);
		}
		if (Keyboard.keyDown("down")) {
			y = Math.min(y + moveFactor, Game.WINDOW_HEIGHT);
		}
		if (Keyboard.keyDown("left")) {
			x = Math.max(x - moveFactor, 0);
		}
		if (Keyboard.keyDown("right")) {
			x = Math.min(x + moveFactor, Game.WINDOW_WIDTH);
		}
		if (Keyboard.keyJustDown("z") && alarm[0] == 0) {
			alarm[0] = 2;
		}

		int collision = world.isColliding(this, "enemy_ship_parent_bw");
		if (collision > -1) {
			EnemyShipParentBW enemy = (EnemyShipParentBW)world.getObject(collision);
			if (enemy.health != -1000) {
				enemy.health -= 2;
			}
			hurt();
		}
		collision = world.isColliding(this, "boss_part_parent_bw");
		if (collision > -1) {
			hurt();
		}
		collision = world.isColliding(this, "enemy_bullet_parent_bw");
		if (collision > -1) {
			hurt();
		}

		if (health <= 0) {
			world.getObjectList("wave_controller", false).get(0).alarm[10] = 60;
			world.instanceDestroy(instanceID);
		}

		if (iFrames > 0) {
			iFrames--;
		}
	}

	private void hurt() {
		if (iFrames == 0) {
			iFrames = 110;
			health--;
			if (powerUp > 0) {
				powerUp--;
			}
			else if (fireRatePowerUp > 0) {
				fireRatePowerUp--;
			}
			Sound.playSound("player_hit_bw_1");
			Sound.setSoundGain("player_hit_bw_1", 0.75);
		}
	}

	@Override
	public void draw(WorldMiddleman world) {
		if (iFrames > 0) {
			if ((iFrames / 10) % 2 == 0) {
				drawSelf();
			}
		}
		else {
			drawSelf();
		}
	}

	@Override
	public void alarm0(WorldMiddleman world) {
		if (world.instanceExists("guild_upgrade_menu_bw")) {
			return;
		}

		int nextFire = 6;
		if (fireRatePowerUp == 1) {
			nextFire = 5;
		}
		else if (fireRatePowerUp == 2) {
			nextFire = 4;
		}
		else if (fireRatePowerUp == 3) {
			nextFire = 3;
		}
		if (powerUp == 0) {
			world.createInstance("player_bullet_basic_bw", x - 12, y + 12, 10);
			world.createInstance("player_bullet_basic_bw", x + 12, y + 12, 10);
		}
		else if (powerUp == 1) {
			world.createInstance("player_bullet_basic_bw", x - 6, y + 8, 10);
			world.createInstance("player_bullet_basic_bw", x + 6, y + 8, 10);
			world.createInstance("player_bullet_basic_bw", x - 17, y + 12, 10);
			world.createInstance("player_bullet_basic_bw", x + 17, y + 12, 10);
		}
		else if (powerUp == 2) {
			world.createInstance("player_bullet_basic_bw", x - 6, y + 8, 10);
			world.createInstance("player_bullet_basic_bw", x + 6, y + 8, 10);
			world.createInstance("player_bullet_basic_bw", x - 17, y + 12, 10);
			world.createInstance("player_bullet_basic_bw", x + 17, y + 12, 10);
			bulletCounter++;
			if (bulletCounter == 3) {
				world.createInstance("player_bullet_missile_bw", x - 48, y + 15, 10);
			}
			else if (bulletCounter >= 7) {
				world.createInstance("player_bullet_missile_bw", x + 48, y + 15, 10);
				bulletCounter = 0;
			}
		}
		if (Keyboard.keyDown("z")) {
			alarm[0] = nextFire;
		}
	}
}
