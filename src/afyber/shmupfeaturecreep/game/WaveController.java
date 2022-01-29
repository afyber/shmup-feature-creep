package afyber.shmupfeaturecreep.game;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.RandomUtil;
import afyber.shmupfeaturecreep.engine.output.EngineLogger;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.world.Global;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;

import java.util.ArrayList;
import java.util.Arrays;

public class WaveController extends DynamicObject {

	public static final ArrayList<Wave> allWaves = new ArrayList<>();
	public static final ArrayList<EnemyWaveReference> allEnemies = new ArrayList<>();

	private final ArrayList<EnemyWaveReference> availableEnemies = new ArrayList<>();
	private final ArrayList<Wave> availableWaves = new ArrayList<>();
	private final ArrayList<EnemyWaveQueueState> queuedEnemies = new ArrayList<>();
	private int timeToNextWave = 0;
	private int lastWaveIndex = -1;

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
	public void update(WorldMiddleman world) {
		if (timeToNextWave <= 0) {
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
		}
		else {
			for (EnemyWaveQueueState state: queuedEnemies) {
				if (--state.frames <= 0) {
					world.createInstance(state.objectName, state.x, state.y, 0);
				}
			}
			queuedEnemies.removeIf(enemy -> enemy.frames <= 0);

			timeToNextWave--;
		}
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
