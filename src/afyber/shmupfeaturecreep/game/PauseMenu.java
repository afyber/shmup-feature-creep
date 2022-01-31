package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.screen.Screen;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class PauseMenu extends DynamicObject {

	public int[][] background;
	public int selection = 0;

	public boolean thisPaused = false;

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
				thisPaused = false;
			}
			else {
				background = Screen.screenShot();
				world.pause();
				thisPaused = true;
			}
		}
		if (!Screen.windowHasFocus() && !world.isPaused()) {
			background = Screen.screenShot();
			world.pause();
			thisPaused = true;
		}

		if (thisPaused) {
			if (Keyboard.keyJustDown("t")) {
				world.changeRoom("roomItsOverLol");
			}
			if (Keyboard.keyJustDown("down")) {
				selection++;
				if (selection > 2) {
					selection = 0;
				}
			}
			if (Keyboard.keyJustDown("up")) {
				selection--;
				if (selection < 0) {
					selection = 2;
				}
			}
			if (Keyboard.keyJustDown("enter") || Keyboard.keyJustDown("z")) {
				if (selection == 0) {
					world.unpause();
					thisPaused = false;
				}
				else if (selection == 1) {
					// TODO: settings
				}
				else if (selection == 2) {
					Screen.closeWindow();
				}
			}
		}
	}

	@Override
	public void draw(WorldMiddleman world) {
		if (thisPaused) {
			Screen.applySpriteDataToFrame(background, 0, 0, 0.5);
			drawTextExtCentered("PAUSED", Game.WINDOW_WIDTH / 2, 192, 4, 4, -1, 1);

			if (selection == 0) {
				drawTextExtCentered("] RESUME GAME", Game.WINDOW_WIDTH / 2, 296, 3, 3, -1, 1);
			}
			else {
				drawTextExtCentered("RESUME GAME", Game.WINDOW_WIDTH / 2, 296, 3, 3, -1, 1);
			}
			if (selection == 1) {
				drawTextExtCentered("] SETTINGS", Game.WINDOW_WIDTH / 2, 360, 3, 3, -1, 1);
			}
			else {
				drawTextExtCentered("SETTINGS", Game.WINDOW_WIDTH / 2, 360, 3, 3, -1, 1);
			}
			if (selection == 2) {
				drawTextExtCentered("] QUIT", Game.WINDOW_WIDTH / 2, 424, 3, 3, -1, 1);
			}
			else {
				drawTextExtCentered("QUIT", Game.WINDOW_WIDTH / 2, 424, 3, 3, -1, 1);
			}
		}
	}
}
