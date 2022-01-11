package afyber.shmupfeaturecreep.game.stage1;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.RectangleCollision;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;
import afyber.shmupfeaturecreep.game.stage1.enemies.EnemyShipParentBW;

public class PlayerShipBW extends DynamicObject {

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
		}
	}

	@Override
	public void draw(WorldMiddleman world) {
		drawSelf();
	}

	@Override
	public void alarm0(WorldMiddleman world) {
		world.createInstance("player_bullet_basic_bw", x, y - 16, 10);
		if (Keyboard.keyDown("z")) {
			alarm[0] = 6;
		}
	}
}
