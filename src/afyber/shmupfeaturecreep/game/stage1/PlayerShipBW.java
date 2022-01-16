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

	public PlayerShipBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "player_ship_bw";
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "player_ship_bw_1";
		collision = new RectangleCollision(-16, -16, -16, -14);
		imageXScale = 3;
		imageYScale = 3;
		iFrames = 0;
		health = 5;
	}

	@Override
	public void update(WorldMiddleman world) {
		if (Keyboard.keyDown("up")) {
			y = Math.max(y - 6, 0);
		}
		if (Keyboard.keyDown("down")) {
			y = Math.min(y + 6, Game.WINDOW_HEIGHT);
		}
		if (Keyboard.keyDown("left")) {
			x = Math.max(x - 6, 0);
		}
		if (Keyboard.keyDown("right")) {
			x = Math.min(x + 6, Game.WINDOW_WIDTH);
		}
		if (Keyboard.keyJustDown("z") && alarm[0] == 0) {
			alarm[0] = 2;
		}

		int collision = world.isColliding(this, "enemy_ship_parent_bw");
		if (collision > -1) {
			EnemyShipParentBW enemy = (EnemyShipParentBW)world.getObject(collision);
			if (enemy.health != -100) {
				enemy.health = 0;
			}
			if (iFrames == 0) {
				iFrames = 110;
				health--;
				Sound.playSound("player_hit");
				Sound.setSoundGain("player_hit", 1.2);
			}
		}

		if (iFrames > 0) {
			iFrames--;
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
		world.createInstance("player_bullet_basic_bw", x, y - 16, 10);
		if (Keyboard.keyDown("z")) {
			alarm[0] = 6;
		}
	}
}
