package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.sound.Sound;
import afyber.shmupfeaturecreep.engine.world.Global;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class BattleController extends DynamicObject {

	public BattleController(float x, float y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "battle_controller";
	}

	@Override
	public void create(WorldMiddleman world) {
		Global.setIntGlobal("wave", 1);
		Global.setIntGlobal("playerLevel", 2);
		Sound.playSound("alarm2");
	}

	@Override
	public void update(WorldMiddleman world) {
		if (Keyboard.keyJustDown("c")) {
			world.createInstance("enemy_ship_1_L", 100, -50, 0);
		}
		if (Keyboard.keyJustDown("x")) {
			for (int i = 0; i < 10; i++) {
				world.createInstance("enemy_ship_cannon_fodder", 32 + i * 63, -50 - i * 20, 0);
			}
		}
		if (Keyboard.keyJustDown("b")) {
			Sound.pauseSound("alarm2");
		}
		if (Keyboard.keyJustDown("n")) {
			Sound.resumeSound("alarm2");
		}
		if (Keyboard.keyJustDown("m")) {
			Sound.playSound("alarm2");
		}
		if (Keyboard.keyJustDown("v")) {
			Sound.stopSound("alarm2");
		}
	}
}
