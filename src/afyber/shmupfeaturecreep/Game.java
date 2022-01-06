package afyber.shmupfeaturecreep;

import afyber.shmupfeaturecreep.engine.Registry;
import afyber.shmupfeaturecreep.engine.rooms.ObjectCreationReference;
import afyber.shmupfeaturecreep.engine.rooms.Room;
import afyber.shmupfeaturecreep.engine.rooms.StaticObject;
import afyber.shmupfeaturecreep.engine.world.WorldMiddleman;
import afyber.shmupfeaturecreep.game.PlayerBulletBW;
import afyber.shmupfeaturecreep.game.PlayerShipBW;

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

	}

	public static void registerObjects() {
		Registry.registerObject(new PlayerShipBW(0,0,0,-1));
		Registry.registerObject(new PlayerBulletBW(0,0,0,-1));
	}

	public static void registerRooms() {
		ArrayList<StaticObject> tiles = new ArrayList<>();
		ArrayList<ObjectCreationReference> objects = new ArrayList<>();
		objects.add(new ObjectCreationReference("player_ship_bw", 320, 480, 2, 2, 100));
		Registry.registerRoom("roomStart", new Room(tiles, objects));
	}
}
