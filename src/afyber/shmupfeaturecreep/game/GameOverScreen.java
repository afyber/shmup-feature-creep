package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class GameOverScreen extends DynamicObject {

	public GameOverScreen(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "game_over_screen";
	}

	@Override
	public void create(WorldMiddleman world) {
		Sound.stopAllSounds();
	}
}
