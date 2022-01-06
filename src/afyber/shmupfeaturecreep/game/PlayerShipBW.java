package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class PlayerShipBW extends DynamicObject {

	public PlayerShipBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "player_ship_bw";
	}

	@Override
	public void create(WorldMiddleman world) {
		sprite = "player_ship_bw_1";
	}

	@Override
	public void update(WorldMiddleman world) {
		if (Keyboard.keyDown("up")) {
			y -= 4;
		}
		if (Keyboard.keyDown("down")) {
			y += 4;
		}
		if (Keyboard.keyDown("left")) {
			x -= 4;
		}
		if (Keyboard.keyDown("right")) {
			x += 4;
		}
		if (Keyboard.keyJustDown("z")) {
			alarm[0] = 1;
		}
	}

	@Override
	public void draw(WorldMiddleman world) {
		drawSelf();
	}

	@Override
	public void alarm0(WorldMiddleman world) {
		world.createInstance("player_bullet_bw", x, y - 8, 10);
		if (Keyboard.keyDown("z")) {
			alarm[0] = 60;
		}
	}
}
