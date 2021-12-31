package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.Global;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class BattleController extends DynamicObject {

	public BattleController(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "battle_controller";
	}

	@Override
	public void create(WorldMiddleman world) {
		Global.setIntGlobal("wave", 1);
		Global.setIntGlobal("playerLevel", 2);
		Sound.loopMusic("untitled");
		Sound.setMusicPanning("untitled", -1);
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
	}
}
