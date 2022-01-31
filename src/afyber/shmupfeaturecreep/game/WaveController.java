package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.Game;
import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.RandomUtil;
import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.output.EngineLogger;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.Global;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

import java.util.ArrayList;
import java.util.Arrays;

public class WaveController extends DynamicObject {

	public static final ArrayList<Wave> allWaves = new ArrayList<>();
	public static final ArrayList<EnemyWaveReference> allEnemies = new ArrayList<>();
	public static final int BOSS_WAVE = 6;

	private final ArrayList<EnemyWaveReference> availableEnemies = new ArrayList<>();
	private final ArrayList<Wave> availableWaves = new ArrayList<>();
	private final ArrayList<EnemyWaveQueueState> queuedEnemies = new ArrayList<>();
	private int timeToNextWave = 0;
	private int lastWaveIndex = -1;
	private int wavesUntilNext = -1;

	private int currentWave = 0;
	private int state = 0;
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
			WaveProperties.Stage availableStage = WaveProperties.Stage.valueOf(Global.getStringGlobal("stage"));
			for (Wave wave: allWaves) {
				if (wave.properties().stage() == availableStage) {
					availableWaves.add(wave);
				}
			}
			for (EnemyWaveReference reference: allEnemies) {
				if (reference.stage() == availableStage) {
					availableEnemies.add(reference);
				}
			}
		}
	}

	@Override
	public void draw(WorldMiddleman world) {
		if (state == 2) {
			double scale = (timer + 360) / 120.0;
			if (currentWave < BOSS_WAVE) {
				drawTextExtCentered("WAVE " + currentWave, Game.WINDOW_WIDTH / 2.0, 200, scale, scale, -1, (timer + 60) / 180.0);
			}
			else if (currentWave == BOSS_WAVE) {
				drawTextExtCentered("BOSS INCOMING", Game.WINDOW_WIDTH / 2.0, 200, scale, scale, -1, Math.cos(Math.toRadians(timer * 3.0)) / 2 + 0.5);
			}
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
				if (currentWave < BOSS_WAVE) {
					state = 0;
					wavesUntilNext = RandomUtil.randInt(4, 8);
				} else if (currentWave == BOSS_WAVE) {
					// it's boss time
					state = 3;
					wavesUntilNext = -1;
					world.createInstance("boss_part_command_center_bw", 320, -128, 200);
				}
			}
		}
	}

	@Override
	public void alarm8(WorldMiddleman world) {
		if (currentWave == BOSS_WAVE) {
			Sound.playSound("boss_incoming_bw");
			Sound.setSoundGain("boss_incoming_bw", 0.6);
		}
		state = 2;
	}

	private void queueWave(Wave wave) {
		timeToNextWave = wave.properties().framesToNext();

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
				MainClass.LOGGER.log(EngineLogger.Level.WARNING, "No enemy exists to satisfy wave slot requirements");
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
				queuedEnemies.add(new EnemyWaveQueueState(stringChoice, position.x(), position.y(), position.frames()));
			}
		}
	}

	private record EnemyRating(String objectName, double rating) {}
}
