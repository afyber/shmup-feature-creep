package afyber.shmupfeaturecreep.engine.world;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;

import java.util.List;

/**
 * This is shown to DynamicObjects so that they have a way to access world, yes this requires maintaining the same
 * methods twice, too bad.
 *
 * @author afyber
 */
public class WorldMiddleman {

	private final World world;

	public WorldMiddleman(World world) {
		this.world = world;
	}

	/**
	 * Tells the World to change rooms
	 * @param roomName The name of the room to change to
	 */
	public void changeRoom(String roomName) {
		world.changeRoom(roomName);
	}

	/**
	 * Creates an instance of an object initialized with the given parameters.
	 * @param classRef The type of object to create an instance of
	 * @param x The initial x position of the instance
	 * @param y The initial y position of the instance
	 * @param depth The depth of the instance
	 * @return The DynamicObject reference to the new instance, so you can change public fields
	 */
	public DynamicObject createInstance(String classRef, double x, double y, int depth) {
		return world.createInstance(classRef, x, y, depth);
	}

	/**
	 * Schedules a specific object to be removed based on instanceID
	 * @param objRef The instanceID of the object to remove
	 */
	public void instanceDestroy(int objRef) {
		world.queueObjectDestruction(objRef);
	}

	/**
	 * Schedules all objects of a given type to be removed
	 * @param classRef The type of object to remove
	 */
	public void instanceDestroy(String classRef) {
		world.queueObjectDestruction(classRef);
	}

	/**
	 * Tells you if an instance exists with the specified instanceID
	 * @param objRef The instanceID of the object
	 * @return True if the instance exists
	 */
	public boolean instanceExists(int objRef) {
		return world.instanceExists(objRef);
	}

	/**
	 * Tells you if any instances of a given object type exist
	 * @param classRef The object type to check for
	 * @return True if one or more instances exist
	 */
	public boolean instanceExists(String classRef) {
		return world.instanceExists(classRef);
	}

	/**
	 * Gives the DynamicObject for a given instanceID
	 * @param objRef The instanceID of the object to find
	 * @return The DynamicObject of the instance
	 */
	public DynamicObject getObject(int objRef) {
		return world.objRefToObject(objRef);
	}

	/**
	 * Gives a list of all the current instances of a given type, and, if includingChildren is true, also its children.
	 * @param classRef The object type to get a list of
	 * @param includingChildren Whether or not to include the cildren of classRef
	 * @return A list containing all the objects of the given type
	 */
	public List<DynamicObject> getObjectList(String classRef, boolean includingChildren) {
		if (includingChildren) {
			return world.classRefToObjectListInclChildren(classRef);
		}
		else {
			return world.classRefToObjectList(classRef);
		}
	}

	/**
	 * Redirects to World.isColliding(DynamicObject, int). Determines if the two specified objects are colliding.
	 * @param caller The object calling the method, or the object to check collision with
	 * @param objRef A number reference to the specific other object to check for collision with
	 * @return returns objRef if there was a collision, or -1 if not
	 */
	public int isColliding(DynamicObject caller, int objRef) {
		return world.isColliding(caller, objRef);
	}

	/**
	 * Redirects to World.isColliding(DynamicObject, String, boolean). Determines if the specified object is colliding
	 * with any objects of the specified type or are children of the specified type.
	 * @param caller The object calling the method, or the object to check collision with
	 * @param classRef A reference to the type of object to check for collision with
	 * @return The instanceID of the object with which there was a collision, or -1 if there was no collision
	 */
	public int isColliding(DynamicObject caller, String classRef) {
		return isColliding(caller, classRef, true);
	}

	/**
	 * Redirects to World.isColliding(DynamicObject, String, boolean). Determines if the specified object is colliding
	 * with any objects of the specified type, or, if includingChildren is true, with any of the children of the
	 * specified type.
	 * @param caller The object calling the method, or the object to check collision with
	 * @param classRef A reference to the type of object to check for collision with
	 * @param includingChildren true if checking for children of classRef as well as classRef, false if not
	 * @return The instanceID of the object with which there was a collision, or -1 if there was no collision
	 */
	public int isColliding(DynamicObject caller, String classRef, boolean includingChildren) {
		return world.isColliding(caller, classRef, includingChildren);
	}
}
