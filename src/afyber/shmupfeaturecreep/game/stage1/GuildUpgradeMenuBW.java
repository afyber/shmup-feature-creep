package afyber.shmupfeaturecreep.game.stage1;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.Global;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class GuildUpgradeMenuBW extends DynamicObject {

	public int selection = 0;
	public boolean confirmationDialogue = false;
	public int confirmationSelection = 1;

	public GuildUpgradeMenuBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "guild_upgrade_menu_bw";
	}

	@Override
	public void update(WorldMiddleman world) {
		if (!confirmationDialogue) {
			if (Keyboard.keyJustDown("right") && selection != 5) {
				selection++;
				selection = Math.min(2, selection);
			}
			if (Keyboard.keyJustDown("left") && selection != 5) {
				selection--;
				selection = Math.max(selection, 0);
			}
			if (Keyboard.keyJustDown("up")) {
				selection = 5;
			}
			if (Keyboard.keyJustDown("down") && selection == 5) {
				selection = 1;
			}
			if (Keyboard.keyJustDown("enter") || Keyboard.keyJustDown("z")) {
				if (selection < 3) {
					confirmationDialogue = true;
				}
			}
		}
		else {
			if (Keyboard.keyJustDown("up")) {
				confirmationSelection--;
				if (confirmationSelection < 0) {
					confirmationSelection = 1;
				}
			}
			if (Keyboard.keyJustDown("down")) {
				confirmationSelection++;
				if (confirmationSelection > 1) {
					confirmationSelection = 0;
				}
			}
			if (Keyboard.keyJustDown("enter")) {
				if (confirmationSelection == 0) {
					if (selection == 0) {
						Global.setIntGlobal("powerupsUnlock", Global.getIntGlobal("powerupsUnlock") + 1);
					}
					else if (selection == 1) {
						Global.setIntGlobal("enemiesUnlock", Global.getIntGlobal("enemiesUnlock") + 1);
					}
					else if (selection == 2) {
						Global.setIntGlobal("boostsUnlock", Global.getIntGlobal("boostsUnlock") + 1);
					}
					else if (selection == 5) {
						Global.setIntGlobal("bossUnlock", 1);
					}
					world.instanceDestroy(instanceID);
				}
				else {
					confirmationDialogue = false;
					confirmationSelection = 1;
				}
			}
		}
	}

	@Override
	public void draw(WorldMiddleman world) {
		drawRect(140, 100, 500, 500, 0xffffff);
		drawRect(146, 106, 494, 494, 0x000000);
		drawTextExtCentered("UPGRADES", Game.WINDOW_WIDTH / 2, 150, 4, 4, -1, 1);
		draw("button_player_powerups_bw", selection == 0 ? 1 : 0, Game.WINDOW_WIDTH / 2 - 100, 440, 3, 3);
		draw("button_enemy_types_bw", selection == 1 ? 1 : 0, Game.WINDOW_WIDTH / 2, 440, 3, 3);
		draw("button_player_booster_bw", selection == 2 ? 1 : 0, Game.WINDOW_WIDTH / 2 + 100, 440, 3, 3);
		draw("button_boss_unlock_bw", selection == 5 ? 1 : 0, Game.WINDOW_WIDTH / 2, 300, 3, 3);
		if (confirmationDialogue) {
			drawRect(164, 194, 476, 406, 0xffffff);
			drawRect(170, 200, 470, 400, 0x000000);
			drawTextExtCentered("ARE YOU SURE", Game.WINDOW_WIDTH / 2, 220, 3, 3, -1, 1);
			draw("button_yes_bw", confirmationSelection == 0 ? 1 : 0, Game.WINDOW_WIDTH / 2, 310, 3, 3);
			if (confirmationSelection == 0) {
				draw("selection_icon_bw", 0, 210, 310, 3, 3);
				draw("selection_icon_bw", 0, 430, 310, -3, 3);
			}
			draw("button_no_bw", confirmationSelection == 1 ? 1 : 0, Game.WINDOW_WIDTH / 2, 370, 3, 3);
			if (confirmationSelection == 1) {
				draw("selection_icon_bw", 0, 210, 370, 3, 3);
				draw("selection_icon_bw", 0, 430, 370, -3, 3);
			}
		}
	}
}
