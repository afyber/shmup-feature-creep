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

	public void changeRoom(String roomName) {
		world.changeRoom(roomName);
	}

	public DynamicObject createInstance(String classRef, float x, float y, int depth) {
		return world.createInstance(classRef, x, y, depth);
	}

	public void instanceDestroy(int objRef) {
		world.queueObjectDestruction(objRef);
	}

	public void instanceDestroy(String classRef) {
		world.queueObjectDestruction(classRef);
	}

	public boolean instanceExists(int objRef) {
		return world.instanceExists(objRef);
	}

	public boolean instanceExists(String classRef) {
		return world.instanceExists(classRef);
	}

	// This function is now in a weird situation of being slightly useless, but possibly useful
	public DynamicObject getObject(int objRef) {
		return world.objRefToObject(objRef);
	}

	public List<DynamicObject> getObjectList(String classRef, boolean includingChildren) {
		if (includingChildren) {
			return world.classRefToObjectListInclChildren(classRef);
		}
		else {
			return world.classRefToObjectList(classRef);
		}
	}

	public boolean isColliding(DynamicObject caller, int objRef) {
		return world.isColliding(caller, objRef);
	}

	public boolean isColliding(DynamicObject caller, String classRef, boolean includingChildren) {
		return world.isColliding(caller, classRef, includingChildren);
	}
}
