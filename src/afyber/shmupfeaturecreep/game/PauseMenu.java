package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.screen.Screen;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class PauseMenu extends DynamicObject {

	public PauseMenu(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "pause_menu";
		pauseable = false;
	}

	@Override
	public void update(WorldMiddleman world) {
		if (Keyboard.keyJustDown("escape")) {
			if (world.isPaused()) {
				world.unpause();
			}
			else {
				world.pause();
			}
		}
		if (!Screen.windowHasFocus()) {
			world.pause();
		}
	}

	@Override
	public void draw(WorldMiddleman world) {
		if (world.isPaused()) {
			drawTextExtCentered("PAUSED", Game.WINDOW_WIDTH / 2, 200, 4, 4, -1, 1);
		}
	}
}
