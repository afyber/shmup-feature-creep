package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.Global;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class Player1 extends DynamicObject {

	public Player1(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "player_object";
	}

	@Override
	public void create(WorldMiddleman world) {
		spriteIndex = "player_sprite_normal";
		collisionIndex = "player_sprite_normal";
		imageXScale = 2;
		imageYScale = 2;
	}

	@Override
	public void update(WorldMiddleman world) {
		float movementFactor = 5;
		if (Keyboard.keyDown("left")) {
			x -= movementFactor;
			if (x < 2) {
				x = 2;
			}
		}
		if (Keyboard.keyDown("right")) {
			x += movementFactor;
			if (x >= MainClass.WINDOW_WIDTH * 3f/4f - 2) {
				x = MainClass.WINDOW_WIDTH * 3f/4f - 2;
			}
		}
		if (Keyboard.keyDown("down")) {
			y += movementFactor;
			if (y + movementFactor >= MainClass.WINDOW_HEIGHT - 2) {
				y = MainClass.WINDOW_HEIGHT - 2;
			}
		}
		if (Keyboard.keyDown("up")) {
			y -= movementFactor;
			if (y < 2) {
				y = 2;
			}
		}
		if (Keyboard.keyDown("z") && alarm[7] < 0) {
			alarm[7] = 1;
		}
	}

	@Override
	public void alarm7(WorldMiddleman world) {
		int newAlarm = -1;
		switch (Global.getIntGlobal("playerLevel")) {
			case 0 -> {
				world.createInstance("player_bullet", x, y - 8, depth);
				newAlarm = 6;
			}
			case 1 -> {
				world.createInstance("player_bullet", x - 6, y - 4, depth);
				world.createInstance("player_bullet", x + 6, y - 4, depth);
				newAlarm = 6;
			}
			case 2 -> {
				world.createInstance("player_bullet", x - 6, y - 4, depth);
				world.createInstance("player_bullet", x + 6, y - 4, depth);
				newAlarm = 3;
			}
			default -> {
				// this is a bad state so something has gone wrong, maybe I should report it?
				// eh.
			}
		}
		if (Keyboard.keyDown("z")) {
			alarm[7] = newAlarm;
		}
	}
}
