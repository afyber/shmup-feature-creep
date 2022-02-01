package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.screen.Screen;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class MainMenuScreen extends DynamicObject {

	public int selection = 0;

	public MainMenuScreen(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "main_menu_screen";
	}

	@Override
	public void create(WorldMiddleman world) {

	}

	@Override
	public void update(WorldMiddleman world) {
		if (alarm[0] <= 0) {
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
					alarm[0] = 60;
					Sound.playSound("boss_hit_ineffective_bw");
				}
				if (selection == 1) {
					// TODO: settings menu
				}
				if (selection == 2) {
					Screen.closeWindow();
				}
			}
		}
	}

	@Override
	public void draw(WorldMiddleman world) {
		drawTextExtCentered("SHMUP", Game.WINDOW_WIDTH / 2.0, 80, 8, 8, -1, 1);
		drawTextExtCentered("FEATURE CREEP", Game.WINDOW_WIDTH / 2.0, 180, 4, 4, -1, 1);
		if (alarm[0] < 15 || alarm[0] < 45 && alarm[0] >= 30) {
			drawTextExtCentered("START GAME", Game.WINDOW_WIDTH / 2.0, 340, 3, 3, -1, 1);
		}
		draw("selection_icon_bw", 0, Game.WINDOW_WIDTH / 2.0 - 140, 354, 3, 3);
		draw("selection_icon_bw", 0, Game.WINDOW_WIDTH / 2.0 + 140, 354, -3, 3);
	}

	@Override
	public void alarm0(WorldMiddleman world) {
		world.changeRoom("roomBattle");
	}
}
