package afyber.shmupfeaturecreep.engine;

import afyber.shmupfeaturecreep.engine.rooms.Room;

import java.util.HashMap;

public class Registry {
	private Registry() {}

	private static final HashMap<String, Room> roomRegistry = new HashMap<>();
	// TODO: finally make it so you can reference a dynamic object via string instead of via class

	public static void registerRoom(String roomName, Room room) {
		roomRegistry.put(roomName, room);
	}

	public static Room getRoom(String roomName) {
		return roomRegistry.get(roomName);
	}

	public static boolean hasRoom(String roomName) {
		return roomRegistry.containsKey(roomName);
	}
}
