package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.Global;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class BattleController extends DynamicObject {

	public BattleController(float x, float y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
	}

	@Override
	public void create(WorldMiddleman world) {
		Global.setGlobal("wave", 1);
	}

	@Override
	public void update(WorldMiddleman world) {
		if (Keyboard.keyJustDown("c")) {
			world.createInstance(EnemyShip1L.class, 100, -50, 0);
		}
		if (Keyboard.keyJustDown("x")) {
			for (int i = 0; i < 10; i++) {
				world.createInstance(EnemyShipCannonFodder.class, 32 + i * 63, -50 - i * 20, 0);
			}
		}
	}
}
