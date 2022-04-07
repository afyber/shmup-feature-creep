package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.Main;
import afyber.shmupfeaturecreep.engine.RandomUtil;
import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.output.EngineLogger;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.Global;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;
import afyber.shmupfeaturecreep.game.stage1.PlayerShipBW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaveController extends DynamicObject {

	public static final List<Wave> allWaves = new ArrayList<>();
	public static final List<EnemyWaveReference> allEnemies = new ArrayList<>();
	public static final int BOSS_WAVE = 4;

	private final ArrayList<EnemyWaveReference> availableEnemies = new ArrayList<>();
	private final ArrayList<Wave> availableWaves = new ArrayList<>();
	private final ArrayList<EnemyWaveQueueState> queuedEnemies = new ArrayList<>();
	private int timeToNextWave = 0;
	private int lastWaveIndex = -1;
	private int wavesUntilNext = -1;

	private double waveDifficultyAverage = 0.2;

	private int currentWave = 0;
	private int state = 0;
	private boolean shopDone = false;
	private int timer = -1;

	public WaveController(double x, double y, int depth, int instanceID) {
		super(x, y, depth, instanceID);
		objectName = "wave_controller";
	}

	@Override
	public void create(WorldMiddleman world) {
		if (Global.getStringGlobal("stage").equals("freestyle")) {
			availableWaves.addAll(allWaves);
			availableEnemies.addAll(allEnemies);
		}
		else {
			refresh();
		}
		alarm[9] = 60;
		Sound.stopMusic(Global.getStringGlobal("level_theme"));
		Sound.stopMusic(Global.getStringGlobal("boss_theme"));
	}

	private void refresh() {
		WaveProperties.Stage availableStage = WaveProperties.Stage.valueOf(Global.getStringGlobal("stage"));
		Global.setStringGlobal("level_theme", "level_theme_" + availableStage.toString().toLowerCase());
		Global.setStringGlobal("boss_theme", "boss_theme_" + availableStage.toString().toLowerCase());
		for (Wave wave: allWaves) {
			if (wave.properties().stage() == availableStage && Global.getIntGlobal("enemiesUnlock") >= wave.properties().batch()) {
				availableWaves.add(wave);
			}
		}
		for (EnemyWaveReference reference: allEnemies) {
			if (reference.stage() == availableStage && Global.getIntGlobal("enemiesUnlock") >= reference.batch()) {
				availableEnemies.add(reference);
			}
		}
	}

	@Override
	public void draw(WorldMiddleman world) {
		if (state == 2) {
			double scale = (timer + 360) / 120.0;
			if (currentWave % BOSS_WAVE != 0 || shopDone) {
				drawTextExtCentered("WAVE " + currentWave, Game.WINDOW_WIDTH / 2.0, 200, scale, scale, -1, (timer + 60) / 180.0);
			}
			else if (Global.getIntGlobal("bossUnlock") > 0) {
				drawTextExtCentered("BOSS INCOMING", Game.WINDOW_WIDTH / 2.0, 200, scale, scale, -1, Math.cos(Math.toRadians(timer * 3.0)) / 2 + 0.5);
			}
		}
		draw("health_icon_bw", 0, 580, 620, 3, 3);
		if (world.instanceExists("player_ship_bw")) {
			PlayerShipBW ship = ((PlayerShipBW)world.getObjectList("player_ship_bw", false).get(0));
			drawTextExt(String.valueOf(ship.health), 606, 606, 3, 3, -1, 1);
		}
		else {
			drawTextExt("0", 606, 606, 3, 3, -1, 1);
		}
		if (Game.DEBUG) {
			drawTextExt(allWaves.get(currentWave).properties().name(), 100, 100, 2, 2, -1, 1);
		}
	}

	@Override
	public void update(WorldMiddleman world) {
		if (state == 0) {
			if (timeToNextWave <= 0 && timeToNextWave != -1000) {
				if (wavesUntilNext <= 0) {
					currentWave++;
					wavesUntilNext = -1;
					timer = 120;
					state = 1;
					alarm[8] = 60;
					return;
				}

				int tmp = RandomUtil.randInt(availableWaves.size());
				if (lastWaveIndex >= 0) {
					// handle wave-choosing properties
					if (!availableWaves.get(lastWaveIndex).properties().repeatable()) {
						while (tmp == lastWaveIndex) {
							tmp = RandomUtil.randInt(availableWaves.size());
						}
					}
				}
				queueWave(availableWaves.get(tmp));
				lastWaveIndex = tmp;
				wavesUntilNext--;
			} else {
				for (EnemyWaveQueueState queueState: queuedEnemies) {
					if (--queueState.frames <= 0) {
						world.createInstance(queueState.objectName, queueState.x, queueState.y, 0);
					}
				}
				queuedEnemies.removeIf(enemy -> enemy.frames <= 0);

				timeToNextWave--;
			}
		} else if (state == 2) {
			timer--;
			if (timer <= 0) {
				if (currentWave % BOSS_WAVE != 0 || shopDone) {
					state = 0;
					wavesUntilNext = RandomUtil.randInt(4, 8);
					shopDone = false;
				} else {
					if (Global.getIntGlobal("bossUnlock") == 1) {
						// it's boss time
						state = 3;
						wavesUntilNext = -1;
						world.createInstance("boss_part_command_center_bw", 320, -128, 200);
					}
					else {
						state = 4;
						world.createInstance("guild_upgrade_menu_bw", 0, 0, 200);
						// go to the upgrade menu
					}
				}
			}
		} else if (state == 4) {
			if (!world.instanceExists("guild_upgrade_menu_bw")) {
				alarm[8] = 40;
				timer = 120;
				state = 1;
				shopDone = true;
				refresh();
			}
		}
	}

	@Override
	public void alarm8(WorldMiddleman world) {
		if (currentWave % BOSS_WAVE == 0 && !shopDone) {
			world.createParticle("plus_one_coins_bw", Game.WINDOW_WIDTH / 2.0, 160);
		}
		if (currentWave % BOSS_WAVE == 0 && Global.getIntGlobal("bossUnlock") > 0 && !shopDone) {
			Sound.stopMusic(Global.getStringGlobal("level_theme"));
			Sound.playMusic(Global.getStringGlobal("boss_theme"));
		}
		state = 2;
	}

	@Override
	public void alarm9(WorldMiddleman world) {
		Sound.playMusic(Global.getStringGlobal("level_theme"));
	}

	@Override
	public void alarm10(WorldMiddleman world) {
		world.changeRoom("roomGameOver");
	}

	private void queueWave(Wave wave) {
		ArrayList<EnemyWaveQueueState> toQueue = new ArrayList<>();

		for (EnemyWaveSlot slot: wave.slots()) {
			ArrayList<EnemyTag> disqualifyingTags = new ArrayList<>();
			ArrayList<EnemyTag> recommendedTags = new ArrayList<>();
			ArrayList<EnemyTag> requiredTags = new ArrayList<>();
			for (TagReference tag: slot.tags()) {
				if (tag.state() == TagReference.TagState.DISALLOWED) {
					disqualifyingTags.add(tag.tag());
				}
				else if (tag.state() == TagReference.TagState.RECOMMENDED) {
					recommendedTags.add(tag.tag());
				}
				else if (tag.state() == TagReference.TagState.REQUIRED) {
					requiredTags.add(tag.tag());
				}
			}

			ArrayList<EnemyRating> enemyRatings = new ArrayList<>();

			// for all available enemies: check if they can be used and rate them
			outer: for (EnemyWaveReference reference: availableEnemies) {
				if (reference.rating() < slot.minRating() || reference.rating() > slot.maxRating()) {
					continue;
				}

				// check if this enemy has any "DISALLOWED" tags or for if it doesn't have any "REQUIRED" tags
				for (EnemyTag tag: reference.enemyTags()) {
					if (disqualifyingTags.contains(tag)) {
						// labels
						continue outer;
					}
				}
				for (EnemyTag tag: requiredTags) {
					if (!Arrays.asList(reference.enemyTags()).contains(tag)) {
						// labels with a kick
						continue outer;
					}
				}

				// enemies with the most "RECOMMENDED" tags are rated highest
				// NOTE: different rating from before, this is rating how well the enemy fits the slot
				double rating = 0;
				for (EnemyTag tag: reference.enemyTags()) {
					if (recommendedTags.contains(tag)) {
						rating += 1;
					}
				}

				enemyRatings.add(new EnemyRating(reference.objectName(), rating));
			}

			// sort the list so the highest rated enemies are first
			enemyRatings.sort((rating1, rating2) -> {
				if (rating1.rating() < rating2.rating()) {
					return -1;
				}
				else if (rating1.rating() > rating2.rating()) {
					return 1;
				}
				return 0;
			});

			String stringChoice;

			if (enemyRatings.isEmpty()) {
				// lol no that's not allowed
				Main.LOGGER.log(EngineLogger.Level.WARNING, "No enemy exists to satisfy wave slot requirements");
				Main.LOGGER.log(EngineLogger.Level.DEBUG, "Requirements: " + slot);
				return;
			}
			else if (enemyRatings.size() >= 5) {
				// pick randomly from the top five enemies
				stringChoice = enemyRatings.get(RandomUtil.randInt(5)).objectName();
			}
			else {
				// enemyRatings.size() is greater than 0 and less than 5
				stringChoice = enemyRatings.get(RandomUtil.randInt(enemyRatings.size())).objectName();
			}

			// add all enemies in this slot to the queue
			for (EnemyTimeSpacePosition position: slot.positions()) {
				toQueue.add(new EnemyWaveQueueState(stringChoice, position.x(), position.y(), position.frames()));
			}
		}

		timeToNextWave = wave.properties().framesToNext();
		queuedEnemies.addAll(toQueue);
	}

	private record EnemyRating(String objectName, double rating) {}
}
