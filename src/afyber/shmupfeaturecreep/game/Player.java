package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class Player extends DynamicObject {

	public Player(float x, float y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
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
		if (Keyboard.keyDown("z") && alarm[7] < 0) {
			alarm[7] = 1;
		}
	}

	@Override
	public void alarm8(WorldMiddleman world) {
		int bul = world.createInstance(PlayerBullet.class, x, y + 2, depth);
		world.setAlarm(bul, 0, 120);
		if (Keyboard.keyDown("z")) {
			alarm[7] = 4;
		}
	}
}
