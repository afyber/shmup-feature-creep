package afyber.shmupfeaturecreep.game.stage1;

import afyber.shmupfeaturecreep.engine.RandomUtil;
import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class WaveControllerBW extends DynamicObject {

	public WaveControllerBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "wave_controller_bw";
	}

	@Override
	public void update(WorldMiddleman world) {
		if (Keyboard.keyJustDown("x")) {
			world.createInstance("enemy_ship_cannon_fodder_bw", RandomUtil.randInt(640), -16, 0);
		}
	}
}
