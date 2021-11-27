package afyber.shmupfeaturecreep.engine;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.rooms.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Registry {
	private Registry() {}

	private static final HashMap<String, Room> roomRegistry = new HashMap<>();
	private static final HashMap<String, Class<? extends DynamicObject>> dynamicObjectRegistry = new HashMap<>();
	private static final HashMap<String, ArrayList<String>> dynamicObjectChildRegistry = new HashMap<>();

	public static void registerRoom(String roomName, Room room) {
		roomRegistry.put(roomName, room);
	}

	public static Room getRoom(String roomName) {
		return roomRegistry.get(roomName);
	}

	public static boolean hasRoom(String roomName) {
		return roomRegistry.containsKey(roomName);
	}

	public static void registerObject(String objectName, Class<? extends DynamicObject> classRef) {
		dynamicObjectRegistry.put(objectName, classRef);
	}

	public static Class<? extends DynamicObject> getObject(String objectName) {
		return dynamicObjectRegistry.get(objectName);
	}

	public static boolean hasObject(String objectName) {
		return dynamicObjectRegistry.containsKey(objectName);
	}

	public static void registerObjectAsChildOf(String objectName, String objectParentName) {
		if (!dynamicObjectChildRegistry.containsKey(objectParentName)) {
			dynamicObjectChildRegistry.put(objectParentName, new ArrayList<>());
		}
		dynamicObjectChildRegistry.get(objectParentName).add(objectName);
	}

	public static List<String> getChildrenOfObject(String objectName) {
		return dynamicObjectChildRegistry.get(objectName);
	}

	public static boolean hasChildrenForObject(String objectName) {
		return dynamicObjectChildRegistry.containsKey(objectName);
	}
}
