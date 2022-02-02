package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.screen.Screen;
import afyber.shmupfeaturecreep.engine.world.Global;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class PauseMenu extends DynamicObject {

	public int[][] background;
	public int selection = 0;

	public boolean thisPaused = false;

	public boolean settingsMenu = false;
	public int settingsSelection = 0;

	public PauseMenu(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "pause_menu";
		pauseable = false;
	}

	@Override
	public void update(WorldMiddleman world) {
		if (!settingsMenu) {
			if (Keyboard.keyJustDown("escape")) {
				if (world.isPaused()) {
					world.unpause();
					thisPaused = false;
				} else {
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
					} else if (selection == 1) {
						settingsMenu = true;
						settingsSelection = 0;
					} else if (selection == 2) {
						Screen.closeWindow();
					}
				}
			}
		}
		else {
			if ((Keyboard.keyJustDown("enter") || Keyboard.keyJustDown("z")) && settingsSelection == 2) {
				settingsMenu = false;
			}
			if (Keyboard.keyJustDown("escape")) {
				settingsMenu = false;
			}
			if (Keyboard.keyJustDown("down")) {
				settingsSelection++;
				if (settingsSelection > 2) {
					settingsSelection = 0;
				}
			}
			if (Keyboard.keyJustDown("up")) {
				settingsSelection--;
				if (settingsSelection < 0) {
					settingsSelection = 1;
				}
			}
			if (Keyboard.keyJustDown("right")) {
				if (settingsSelection == 0) {
					Global.setIntGlobal("settingsScreenSize", Math.min(1, Global.getIntGlobal("settingsScreenSize") + 1));
					Screen.changeScreenSize(Game.WINDOW_WIDTH * (Global.getIntGlobal("settingsScreenSize") + 1), Game.WINDOW_HEIGHT * (Global.getIntGlobal("settingsScreenSize") + 1));
				}
				else if (settingsSelection == 1) {
					Global.setIntGlobal("settingsVolume", Math.min(10, Global.getIntGlobal("settingsVolume") + 1));
					Sound.setGlobalGain(Global.getIntGlobal("settingsVolume") / 10.0);
				}
			}
			if (Keyboard.keyJustDown("left")) {
				if (settingsSelection == 0) {
					Global.setIntGlobal("settingsScreenSize", Math.max(Global.getIntGlobal("settingsScreenSize") - 1, 0));
					Screen.changeScreenSize(Game.WINDOW_WIDTH * (Global.getIntGlobal("settingsScreenSize") + 1), Game.WINDOW_HEIGHT * (Global.getIntGlobal("settingsScreenSize") + 1));
				}
				else if (settingsSelection == 1) {
					Global.setIntGlobal("settingsVolume", Math.max(Global.getIntGlobal("settingsVolume") - 1, 0));
					Sound.setGlobalGain(Global.getIntGlobal("settingsVolume") / 10.0);
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
			if (settingsMenu) {
				drawRectExt(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT, 0x000000, 0.5);
				drawRect(160, 200, 480, 450, 0xffffff);
				drawRect(166, 206, 474, 444, 0x000000);
				drawTextExtCentered("SETTINGS", Game.WINDOW_WIDTH / 2.0, 240, 4, 4, -1, 1);
				if (settingsSelection == 0) {
					drawTextExtCentered("] WINDOW " + (Global.getIntGlobal("settingsScreenSize") + 1) + "X", Game.WINDOW_WIDTH / 2.0, 300, 3, 3, -1, 1);
				}
				else {
					drawTextExtCentered("WINDOW " + (Global.getIntGlobal("settingsScreenSize") + 1) + "X", Game.WINDOW_WIDTH / 2.0, 300, 3, 3, -1, 1);
				}
				if (settingsSelection == 1) {
					drawTextExtCentered("] VOLUME " + Global.getIntGlobal("settingsVolume"), Game.WINDOW_WIDTH / 2.0, 350, 3, 3, -1, 1);
				}
				else {
					drawTextExtCentered("VOLUME " + Global.getIntGlobal("settingsVolume"), Game.WINDOW_WIDTH / 2.0, 350, 3, 3, -1, 1);
				}
				if (settingsSelection == 2) {
					drawTextExtCentered("] CLOSE", Game.WINDOW_WIDTH / 2.0, 400, 3, 3, -1, 1);
				}
				else {
					drawTextExtCentered("CLOSE", Game.WINDOW_WIDTH / 2.0, 400, 3, 3, -1, 1);
				}
			}
		}
	}
}
