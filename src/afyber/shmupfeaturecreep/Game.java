package afyber.shmupfeaturecreep;

import afyber.shmupfeaturecreep.engine.Registry;
import afyber.shmupfeaturecreep.engine.rooms.ObjectCreationReference;
import afyber.shmupfeaturecreep.engine.rooms.Room;
import afyber.shmupfeaturecreep.engine.rooms.StaticObject;
import afyber.shmupfeaturecreep.game.*;

import java.util.ArrayList;

public class Game {
	private Game() {}

	public static void registerObjects() {
		// NOTE: It is imperative, and I mean IMPERATIVE that this name is the same as the field of the same name in the object's class
		Registry.registerObject("player_object", Player1.class);
		Registry.registerObject("player_bullet", Player1Bullet.class);
		Registry.registerObject("battle_controller", BattleController.class);
		Registry.registerObject("scorecard", Scorecard1.class);
		Registry.registerObject("enemy_ship_parent", EnemyShipParent.class);
		Registry.registerObject("enemy_ship_1_L", EnemyShip1L.class);
		Registry.registerObjectAsChildOf("enemy_ship_1_L", "enemy_ship_parent");
		Registry.registerObject("enemy_ship_cannon_fodder", EnemyShipCannonFodder.class);
		Registry.registerObjectAsChildOf("enemy_ship_cannon_fodder", "enemy_ship_parent");

		Registry.registerObject("test_1", TestInstanceClass.class);
	}

	public static void registerRooms() {
		ArrayList<StaticObject> roomTiles = new ArrayList<>();
		ArrayList<ObjectCreationReference> roomObjects = new ArrayList<>();
		roomObjects.add(new ObjectCreationReference("player_object", 300, 300, 1, 1, 0));
		roomObjects.add(new ObjectCreationReference("battle_controller", 0, 0, 0, 0, 0));
		roomObjects.add(new ObjectCreationReference("scorecard", 0, 0, 1, 1, 100));
		roomObjects.add(new ObjectCreationReference("test_1", 100, 100, 1, 1, 0));
		Registry.registerRoom("roomStart", new Room(roomTiles, roomObjects));
	}
}
