package afyber.shmupfeaturecreep.engine.world;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.rooms.ObjectReference;

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

	public ObjectReference createInstance(Class classRef, float x, float y, int depth) {
		return world.createInstance(classRef, x, y, depth);
	}

	public void instanceDestroy(ObjectReference ref) {
		world.instanceDestroy(ref);
	}

	public void instanceDestroy(Class objectClass) {
		world.instanceDestroy(objectClass);
	}

	public boolean instanceExists(ObjectReference ref) {
		return world.instanceExists(ref);
	}

	public boolean instanceExists(Class objectClass) {
		return world.instanceExists(objectClass);
	}

	public boolean isColliding(DynamicObject caller, ObjectReference other) {
		return world.isColliding(caller, other);
	}

	public boolean isColliding(DynamicObject caller, Class other) {
		return world.isColliding(caller, other);
	}
}
