package afyber.shmupfeaturecreep.engine.world;

import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;

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

	public int createInstance(Class classRef, float x, float y, int depth) {
		return world.createInstance(classRef, x, y, depth);
	}

	public void instanceDestroy(int objRef) {
		world.queueObjectDestruction(objRef);
	}

	public void instanceDestroy(Class classRef) {
		world.queueObjectDestruction(classRef);
	}

	public boolean instanceExists(int objRef) {
		return world.instanceExists(objRef);
	}

	public boolean instanceExists(Class classRef) {
		return world.instanceExists(classRef);
	}

	public boolean isColliding(DynamicObject caller, int objRef) {
		return world.isColliding(caller, objRef);
	}

	public boolean isColliding(DynamicObject caller, Class classRef) {
		return world.isColliding(caller, classRef);
	}

	public void setAlarm(int objRef, int alarm, int value) {
		world.setAlarm(objRef, alarm, value);
	}

	public int getAlarm(int objRef, int alarm) {
		return world.getAlarm(objRef, alarm);
	}
}
