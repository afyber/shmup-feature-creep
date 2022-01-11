package afyber.shmupfeaturecreep;

import afyber.shmupfeaturecreep.engine.Registry;
import afyber.shmupfeaturecreep.engine.audio.Sound;
import afyber.shmupfeaturecreep.engine.particle.Particle;
import afyber.shmupfeaturecreep.engine.particle.ParticleRegistry;
import afyber.shmupfeaturecreep.engine.particle.physics.FrictionVelocity;
import afyber.shmupfeaturecreep.engine.particle.render.SquareRenderer;
import afyber.shmupfeaturecreep.engine.rooms.ObjectCreationReference;
import afyber.shmupfeaturecreep.engine.rooms.Room;
import afyber.shmupfeaturecreep.engine.rooms.StaticObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;
import afyber.shmupfeaturecreep.game.stage1.*;
import afyber.shmupfeaturecreep.game.stage1.enemies.EnemyShipCannonFodderBW;
import afyber.shmupfeaturecreep.game.stage1.enemies.EnemyShipParentBW;
import afyber.shmupfeaturecreep.game.stage1.enemies.EnemyShipSmallCannonBW;

import java.util.ArrayList;

public class Game {

	public static final String GAME_NAME = "shmupfeaturecreep";
	public static final String GAME_NAME_NICE = "Shmup: Feature Creep";

	public static final int WINDOW_WIDTH = 640;
	public static final int WINDOW_HEIGHT = 640;

	public static final int IDEAL_FPS = 60;

	public static final boolean DEBUG = true;

	private Game() {}

	public static void gameStart(WorldMiddleman world) {
		Sound.setMusicGain("small_explosion_bw", 0.65);

		ParticleRegistry.registerParticle("small_enemy_thrust_bw", new Particle(-1, -1, 30, new FrictionVelocity(270, 3, 0.15), new SquareRenderer(3, 0xffffff), Particle.FadeRule.LINEAR, -100));
	}

	public static void registerObjects() {
		Registry.registerObject(new PlayerShipBW(0,0,0,-1));
		Registry.registerObject(new PlayerBulletParentBW(0,0,0,-1));
		Registry.registerObjectAsChildOf(new PlayerBulletBasic(0,0,0,-1), "player_bullet_parent_bw");

		Registry.registerObject(new EnemyShipParentBW(0,0,0,-1));
		Registry.registerObjectAsChildOf(new EnemyShipCannonFodderBW(0,0,0,-1), "enemy_ship_parent_bw");
		Registry.registerObjectAsChildOf(new EnemyShipSmallCannonBW(0,0,0,-1), "enemy_ship_parent_bw");

		Registry.registerObject(new ExplosionSmallBW(0,0,0,-1));

		Registry.registerObject(new WaveControllerBW(0,0,0,-1));
	}

	public static void registerRooms() {
		ArrayList<StaticObject> tiles = new ArrayList<>();
		ArrayList<ObjectCreationReference> objects = new ArrayList<>();
		objects.add(new ObjectCreationReference("player_ship_bw", 320, 480, 2, 2, 100));
		objects.add(new ObjectCreationReference("wave_controller_bw", 0, 0, 0, 0, 0));
		Registry.registerRoom("roomStart", new Room(tiles, objects));
	}
}
