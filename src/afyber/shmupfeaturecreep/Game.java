package afyber.shmupfeaturecreep;

import afyber.shmupfeaturecreep.engine.Registry;
import afyber.shmupfeaturecreep.engine.rooms.ObjectCreationReference;
import afyber.shmupfeaturecreep.engine.rooms.Room;
import afyber.shmupfeaturecreep.engine.rooms.StaticObject;
import afyber.shmupfeaturecreep.game.BattleController;
import afyber.shmupfeaturecreep.game.Player1;
import afyber.shmupfeaturecreep.game.Scorecard1;

import java.util.ArrayList;

public class Game {
	private Game() {}

	public static void registerRooms() {
		ArrayList<StaticObject> roomTiles = new ArrayList<>();
		ArrayList<ObjectCreationReference> roomObjects = new ArrayList<>();
		roomObjects.add(new ObjectCreationReference(Player1.class, 300, 300, 1, 1, 0));
		roomObjects.add(new ObjectCreationReference(BattleController.class, 0, 0, 0, 0, 0));
		roomObjects.add(new ObjectCreationReference(Scorecard1.class, 0, 0, 1, 1, 100));
		Registry.registerRoom("roomStart", new Room(roomTiles, roomObjects));
	}
}
