package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class GameOverScreen extends DynamicObject {

	public int selection = 0;

	public GameOverScreen(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "game_over_screen";
	}

	@Override
	public void create(WorldMiddleman world) {
		Sound.stopAllSounds();
	}

	@Override
	public void update(WorldMiddleman world) {
		if (Keyboard.keyJustDown("down")) {
			selection++;
			if (selection > 1) {
				selection = 0;
			}
		}
		if (Keyboard.keyJustDown("up")) {
			selection--;
			if (selection < 0) {
				selection = 1;
			}
		}
		if (Keyboard.keyJustDown("enter") || Keyboard.keyJustDown("z")) {
			if (selection == 0) {
				world.changeRoom("roomBattle");
			}
			else {
				world.changeRoom("roomStart");
			}
		}
	}

	@Override
	public void draw(WorldMiddleman world) {
		drawTextExtCentered("GAME OVER", Game.WINDOW_WIDTH / 2.0, 170, 9, 9, -1, 1);
		if (selection == 0) {
			drawTextExtCentered("] RESTART", Game.WINDOW_WIDTH / 2.0, 360, 3, 3, -1, 1);
		}
		else {
			drawTextExtCentered("RESTART", Game.WINDOW_WIDTH / 2.0, 360, 3, 3, -1, 1);
		}
		if (selection == 1) {
			drawTextExtCentered("] TO MAIN MENU", Game.WINDOW_WIDTH / 2.0, 400, 3, 3, -1, 1);
		}
		else {
			drawTextExtCentered("TO MAIN MENU", Game.WINDOW_WIDTH / 2.0, 400, 3, 3, -1, 1);
		}
	}
}
