package afyber.shmupfeaturecreep;

import afyber.shmupfeaturecreep.engine.GeneralUtil;
import afyber.shmupfeaturecreep.engine.Registry;
import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.errors.EnemiesNotDefinedError;
import afyber.shmupfeaturecreep.engine.errors.WavesNotDefinedError;
import afyber.shmupfeaturecreep.engine.output.EngineLogger;
import afyber.shmupfeaturecreep.engine.particle.Particle;
import afyber.shmupfeaturecreep.engine.particle.ParticleRegistry;
import afyber.shmupfeaturecreep.engine.particle.physics.FrictionVelocity;
import afyber.shmupfeaturecreep.engine.particle.physics.VectorVelocity;
import afyber.shmupfeaturecreep.engine.particle.render.SpriteRenderer;
import afyber.shmupfeaturecreep.engine.particle.render.SquareRenderer;
import afyber.shmupfeaturecreep.engine.rooms.ObjectCreationReference;
import afyber.shmupfeaturecreep.engine.rooms.Room;
import afyber.shmupfeaturecreep.engine.rooms.StaticObject;
import afyber.shmupfeaturecreep.engine.world.Global;
import afyber.shmupfeaturecreep.game.*;
import afyber.shmupfeaturecreep.game.stage1.*;
import afyber.shmupfeaturecreep.game.stage1.boss.BossPartCannonBW;
import afyber.shmupfeaturecreep.game.stage1.boss.BossPartCommandCenterBW;
import afyber.shmupfeaturecreep.game.stage1.boss.BossPartParentBW;
import afyber.shmupfeaturecreep.game.stage1.enemies.*;

import java.io.IOException;
import java.util.ArrayList;

public class Game {
	private Game() {}

	public static final String GAME_NAME = "shmupfeaturecreep";
	public static final String GAME_NAME_NICE = "Shmup: Feature Creep";

	public static final int WINDOW_WIDTH = 640;
	public static final int WINDOW_HEIGHT = 640;

	public static final int IDEAL_FPS = 60;

	public static final boolean DEBUG = true;

	public static void gameStart() {
		Sound.setMusicGain("small_explosion_bw", 0.65);

		ParticleRegistry.registerParticle("small_enemy_thrust_bw", new Particle(-1, -1, 30, new FrictionVelocity(270, 3, 0.15), new SquareRenderer(3, 0xffffff), Particle.FadeRule.LINEAR, -100));

		ParticleRegistry.registerParticle("plus_one_coins_bw", new Particle(-1, -1, 60, new VectorVelocity(270, 1), new SpriteRenderer("plus_one_coins_bw", 0, 3, 3), Particle.FadeRule.CUBE, 300));

		loadEnemies();

		loadWaves();

		Global.setStringGlobal("stage", "BW");

		Global.setIntGlobal("guildCoins", 0);

		Global.setIntGlobal("enemiesUnlock", 0);
		Global.setIntGlobal("powerupsUnlock", 0);
		Global.setIntGlobal("boostsUnlock", 0);
		Global.setIntGlobal("bossUnlock", 0);
	}

	public static void registerObjects() {
		Registry.registerObject(new PlayerShipBW(0,0,0,-1));
		Registry.registerObject(new PlayerBulletParentBW(0,0,0,-1));
		Registry.registerObjectAsChildOf(new PlayerBulletBasic(0,0,0,-1), "player_bullet_parent_bw");

		Registry.registerObject(new EnemyShipParentBW(0,0,0,-1));
		Registry.registerObjectAsChildOf(new EnemyShipCannonFodderBW(0,0,0,-1), "enemy_ship_parent_bw");
		Registry.registerObjectAsChildOf(new EnemyShipSmallCannonBW(0,0,0,-1), "enemy_ship_parent_bw");
		Registry.registerObjectAsChildOf(new EnemyShipTinyCannonBW(0,0,0,-1), "enemy_ship_parent_bw");

		Registry.registerObject(new EnemyMineSmallBW(0,0,0,-1));

		Registry.registerObject(new EnemyBulletParentBW(0,0,0,-1));
		Registry.registerObjectAsChildOf(new EnemySmallBulletBW(0,0,0,-1), "enemy_bullet_parent_bw");

		Registry.registerObject(new ExplosionSmallBW(0,0,0,-1));

		Registry.registerObject(new WeaponPowerupBW(0,0,0,-1));
		Registry.registerObject(new FireRatePowerupBW(0,0,0,-1));

		Registry.registerObject(new BossPartParentBW(0,0,0,-1));
		Registry.registerObjectAsChildOf(new BossPartCommandCenterBW(0,0,0,-1), "boss_part_parent_bw");
		Registry.registerObjectAsChildOf(new BossPartCannonBW(0,0,0,-1), "boss_part_parent_bw");

		Registry.registerObject(new GuildUpgradeMenuBW(0,0,0,-1));

		Registry.registerObject(new WaveController(0,0,0,-1));

		Registry.registerObject(new PauseMenu(0,0,0,-1));
	}

	public static void registerRooms() {
		ArrayList<StaticObject> tiles = new ArrayList<>();
		ArrayList<ObjectCreationReference> objects = new ArrayList<>();
		objects.add(new ObjectCreationReference("player_ship_bw", 320, 480, 2, 2, 100));
		objects.add(new ObjectCreationReference("wave_controller", 0, 0, 0, 0, 0));
		objects.add(new ObjectCreationReference("pause_menu", 0, 0, 0, 0, 0));
		Registry.registerRoom("roomStart", new Room(tiles, objects));
	}

	public static void loadEnemies() {
		try {
			String[] lines = GeneralUtil.readResourceAsLineArray("/enemies.txt");

			for (String line: lines) {
				String[] vals = line.split(",");
				EnemyTag[] tags = new EnemyTag[vals.length - 3];
				for (int i = 3; i < vals.length; i++) {
					tags[i - 3] = EnemyTag.valueOf(vals[i]);
				}
				WaveController.allEnemies.add(new EnemyWaveReference(vals[0], WaveProperties.Stage.valueOf(vals[1]), Double.parseDouble(vals[2]), tags));
			}
		} catch (IOException e) {
			MainClass.LOGGER.log(EngineLogger.Level.ERROR, "Enemies are not defined");
			throw new EnemiesNotDefinedError();
		}
	}

	public static void loadWaves() {
		try {
			String[] lines = GeneralUtil.readResourceAsLineArray("/waves.txt");

			WaveProperties.Stage currentStage = null;
			for (String line: lines) {
				if (GeneralUtil.isLineConfigViable(line)) {
					switch (line) {
						case "bw" -> currentStage = WaveProperties.Stage.BW;
						case "crt" -> currentStage = WaveProperties.Stage.CRT;
						case "nes" -> currentStage = WaveProperties.Stage.NES;
						case "snes" -> currentStage = WaveProperties.Stage.SNES;
						case "doom" -> currentStage = WaveProperties.Stage.DOOM;
						default -> loadWave(line, currentStage);
					}
				}
			}
		} catch (IOException e) {
			MainClass.LOGGER.log(EngineLogger.Level.ERROR, "Waves are not defined");
			throw new WavesNotDefinedError();
		}
	}

	private static void loadWave(String name, WaveProperties.Stage stage) {
		try {
			String[] lines = GeneralUtil.readResourceAsLineArray("/waves/" + name);

			// wave information
			ArrayList<EnemyWaveSlot> enemies = new ArrayList<>();
			int framesToNext = -1;
			boolean repeatable = false;

			// enemy slot information
			double minRating = -1;
			double maxRating = -1;
			ArrayList<TagReference> tags = new ArrayList<>();
			ArrayList<EnemyTimeSpacePosition> slots = new ArrayList<>();
			int buildState = -1;

			for (String line: lines) {
				if (GeneralUtil.isLineConfigViable(line)) {
					if (line.startsWith("framesToNext:")) {
						framesToNext = Integer.parseInt(line.substring(13));
					}
					else if (line.startsWith("repeatable:")) {
						repeatable = Boolean.parseBoolean(line.substring(11));
					}
					if (line.equals("{")) {
						buildState = 0;
						minRating = -1;
						maxRating = -1;
						tags = new ArrayList<>();
						slots = new ArrayList<>();
					}
					else if (buildState == 0 && line.startsWith("[")) {
						line = line.substring(1, line.length() - 1);
						minRating = Double.parseDouble(line.split("-")[0]);
						maxRating = Double.parseDouble(line.split("-")[1]);
						buildState = 1;
					}
					else if (buildState == 1 && line.equals("[")) {
						buildState = 2;
					}
					else if (buildState == 2) {
						if (!line.startsWith("]")) {
							TagReference.TagState state = TagReference.TagState.valueOf(line.split(":")[1]);
							tags.add(new TagReference(EnemyTag.valueOf(line.split(":")[0]), state));
						}
						else {
							buildState = 3;
						}
					}
					else if (buildState == 3 && line.equals("[")) {
						buildState = 4;
					}
					else if (buildState == 4) {
						if (!line.startsWith("]")) {
							String[] vals = line.substring(1, line.length() - 1).split(",");
							double x = Double.parseDouble(vals[0]);
							double y = Double.parseDouble(vals[1]);
							int time = Integer.parseInt(vals[2]);
							slots.add(new EnemyTimeSpacePosition(x, y, time));
						}
						else {
							buildState = 5;
						}
					}
					else if (buildState == 5 || line.equals("}")) {
						enemies.add(new EnemyWaveSlot(minRating, maxRating, tags.toArray(new TagReference[0]), slots.toArray(new EnemyTimeSpacePosition[0])));
					}
				}
			}

			WaveController.allWaves.add(new Wave(enemies.toArray(new EnemyWaveSlot[0]), new WaveProperties(stage, framesToNext, repeatable)));
		} catch (IOException e) {
			throw new WavesNotDefinedError();
		}
	}
}
