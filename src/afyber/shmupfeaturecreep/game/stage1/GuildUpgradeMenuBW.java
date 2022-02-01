package afyber.shmupfeaturecreep.game.stage1;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.input.Keyboard;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.Global;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

public class GuildUpgradeMenuBW extends DynamicObject {

	public int selection = 0;
	public boolean confirmationDialogue = false;
	public int confirmationSelection = 1;
	public int maxPowerup = 2;
	public int maxEnemies = 2;
	public int maxBoosts = 2;

	public GuildUpgradeMenuBW(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "guild_upgrade_menu_bw";
	}

	@Override
	public void create(WorldMiddleman world) {
		Global.setIntGlobal("guildCoins", Global.getIntGlobal("guildCoins") + 1);
	}

	@Override
	public void update(WorldMiddleman world) {
		if (!confirmationDialogue) {
			if (selection < 5) {
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
				if (Keyboard.keyJustDown("down")) {
					selection = 6;
				}
			}
			else {
				if (Keyboard.keyJustDown("down") && selection == 5) {
					selection = 1;
				}
				if (Keyboard.keyJustDown("up") && selection == 6) {
					selection = 1;
				}
			}
			if (Keyboard.keyJustDown("enter") || Keyboard.keyJustDown("z")) {
				if (selection < 3) {
					if (Global.getIntGlobal("guildCoins") >= 1) {
						if (selection == 0 && Global.getIntGlobal("powerupsUnlock") < maxPowerup) {
							confirmationDialogue = true;
						} else if (selection == 1 && Global.getIntGlobal("enemiesUnlock") < maxEnemies) {
							confirmationDialogue = true;
						} else if (selection == 2 && Global.getIntGlobal("boostsUnlock") < maxBoosts) {
							confirmationDialogue = true;
						} else {
							Sound.playSound("player_hit_bw_1");
						}
					}
					else {
						Sound.playSound("player_hit_bw_1");
					}
				}
				else if (selection == 5) {
					if (Global.getIntGlobal("guildCoins") >= 4 && Global.getIntGlobal("bossUnlock") == 0) {
						confirmationDialogue = true;
					}
					else {
						Sound.playSound("player_hit_bw_1");
					}
				}
				else if (selection == 6) {
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
						Global.setIntGlobal("guildCoins", Global.getIntGlobal("guildCoins") - 1);
					}
					else if (selection == 1) {
						Global.setIntGlobal("enemiesUnlock", Global.getIntGlobal("enemiesUnlock") + 1);
						Global.setIntGlobal("guildCoins", Global.getIntGlobal("guildCoins") - 1);
					}
					else if (selection == 2) {
						Global.setIntGlobal("boostsUnlock", Global.getIntGlobal("boostsUnlock") + 1);
						Global.setIntGlobal("guildCoins", Global.getIntGlobal("guildCoins") - 1);
					}
					else if (selection == 5) {
						Global.setIntGlobal("bossUnlock", 1);
						Global.setIntGlobal("guildCoins", Global.getIntGlobal("guildCoins") - 4);
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
		drawRect(140, 100, 500, 550, 0xffffff);
		drawRect(146, 106, 494, 544, 0x000000);

		draw("guild_coin_icon_bw", 0, 36, 600, 3, 3);
		drawTextExt(String.valueOf(Global.getIntGlobal("guildCoins")), 56, 587, 3, 3, -1, 1);

		drawTextExtCentered("UPGRADES", Game.WINDOW_WIDTH / 2.0, 150, 4, 4, -1, 1);
		draw("button_player_powerups_bw", selection == 0 ? 1 : 0, Game.WINDOW_WIDTH / 2.0 - 100, 440, 3, 3);
		for (int i = 0; i < Global.getIntGlobal("powerupsUnlock"); i++) {
			draw("small_pip_bw", 0,Game.WINDOW_WIDTH / 2.0 - 99, 403 - i * 24, 3, 3);
		}

		draw("button_enemy_types_bw", selection == 1 ? 1 : 0, Game.WINDOW_WIDTH / 2.0, 440, 3, 3);
		for (int i = 0; i < Global.getIntGlobal("enemiesUnlock"); i++) {
			draw("small_pip_bw", 0,Game.WINDOW_WIDTH / 2.0 + 1, 403 - i * 24, 3, 3);
		}

		draw("button_player_booster_bw", selection == 2 ? 1 : 0, Game.WINDOW_WIDTH / 2.0 + 100, 440, 3, 3);
		for (int i = 0; i < Global.getIntGlobal("boostsUnlock"); i++) {
			draw("small_pip_bw", 0,Game.WINDOW_WIDTH / 2.0 + 101, 403 - i * 24, 3, 3);
		}
		draw("button_boss_unlock_bw", selection == 5 ? 1 : 0, Game.WINDOW_WIDTH / 2.0, 300, 3, 3);

		draw("button_cancel_bw", selection == 6 ? 1 : 0, Game.WINDOW_WIDTH / 2.0, 510, 3, 3);
		if (confirmationDialogue) {
			drawRect(164, 194, 476, 406, 0xffffff);
			drawRect(170, 200, 470, 400, 0x000000);
			drawTextExtCentered("ARE YOU SURE", Game.WINDOW_WIDTH / 2.0, 220, 3, 3, -1, 1);
			draw("button_yes_bw", confirmationSelection == 0 ? 1 : 0, Game.WINDOW_WIDTH / 2.0, 310, 3, 3);
			if (confirmationSelection == 0) {
				draw("selection_icon_bw", 0, 250, 310, 3, 3);
				draw("selection_icon_bw", 0, 390, 310, -3, 3);
			}
			draw("button_no_bw", confirmationSelection == 1 ? 1 : 0, Game.WINDOW_WIDTH / 2.0, 370, 3, 3);
			if (confirmationSelection == 1) {
				draw("selection_icon_bw", 0, 250, 370, 3, 3);
				draw("selection_icon_bw", 0, 390, 370, -3, 3);
			}
		}
	}
}
