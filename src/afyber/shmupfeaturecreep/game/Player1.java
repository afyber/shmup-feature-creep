package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class Player1 extends DynamicObject {

	public Player1(float x, float y, int depth, int instanceID) {
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
		if (Keyboard.keyDown("left")) {
			x -= 2;
		}
		if (Keyboard.keyDown("right")) {
			x += 2;
		}
		if (Keyboard.keyDown("down")) {
			y += 2;
		}
		if (Keyboard.keyDown("up")) {
			y -= 2;
		}
		if (Keyboard.keyDown("z") && alarm[7] < 0) {
			alarm[7] = 1;
		}
	}

	@Override
	public void alarm8(WorldMiddleman world) {
		int bul = world.createInstance(Player1Bullet.class, x - 6, y - 4, depth);
		//world.setAlarm(bul, 0, 10);
		world.createInstance(Player1Bullet.class, x + 6, y - 4, depth);
		if (Keyboard.keyDown("z")) {
			alarm[7] = 7;
		}
	}
}
