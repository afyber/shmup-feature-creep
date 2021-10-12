package afyber.shmupfeaturecreep.engine;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.rooms.ObjectReference;
import afyber.shmupfeaturecreep.engine.rooms.StaticObject;
import afyber.shmupfeaturecreep.game.TestInstanceClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * This class is very important because it does a lot of things, like holding the list of all objects and tiles.
 *
 * @author afyber
 */
public class World {

	private ArrayList<StaticObject> allTiles;
	private ArrayList<DynamicObject> allGameObjects;

	private int nextAvailableGameObjectID = 1;

	public World() {
		allTiles = new ArrayList<>();
		allGameObjects = new ArrayList<>();
		allTiles.add(new StaticObject("sprite_2", 16, 32));
		allTiles.add(new StaticObject("sprite_3", 64, 128));
		for (int i = 0; i < 100; i++) {
			allGameObjects.add(new DynamicObject(16, 48, 0, getNextAvailableGameObjectID()) {
				@Override
				public void update() {
					x = RandomUtil.randInt(639);
					y = RandomUtil.randInt(399);
				}
			});
		}
		createInstance(TestInstanceClass.class, 64, 64, 0);
	}

	public void drawAll() {
		for (StaticObject tile: allTiles) {
			tile.draw();
		}
		for (DynamicObject gameObject: allGameObjects) {
			gameObject.preDraw();
		}
		for (DynamicObject gameObject: allGameObjects) {
			gameObject.draw();
		}
		for (DynamicObject gameObject: allGameObjects) {
			gameObject.postDraw();
		}
	}

	public void updateAll() {
		for (DynamicObject gameObject: allGameObjects) {
			gameObject.preUpdate();
		}
		for (DynamicObject gameObject: allGameObjects) {
			gameObject.update();
		}
		for (DynamicObject gameObject: allGameObjects) {
			gameObject.postUpdate();
		}
	}

	public void alarmAll() {
		for (DynamicObject gameObject: allGameObjects) {
			for (int i = 0; i < 10; i++) {
				int value = gameObject.getAlarms(i);
				if (value == 0) {
					switch (i) {
						case 0 -> gameObject.alarm1();
						case 1 -> gameObject.alarm2();
						case 2 -> gameObject.alarm3();
						case 3 -> gameObject.alarm4();
						case 4 -> gameObject.alarm5();
						case 5 -> gameObject.alarm6();
						case 6 -> gameObject.alarm7();
						case 7 -> gameObject.alarm8();
						case 8 -> gameObject.alarm9();
						case 9 -> gameObject.alarm10();
					}
					gameObject.setAlarm(i, -1);
				}
				else if (value > 0) {
					gameObject.setAlarm(i, value - 1);
				}
			}
		}
	}

	private int getNextAvailableGameObjectID() {
		int toReturn = nextAvailableGameObjectID;
		nextAvailableGameObjectID++;
		return toReturn;
	}

	public ObjectReference createInstance(Class classRef, float x, float y, int depth) {
		try {
			Constructor con = classRef.getConstructor(Float.TYPE, Float.TYPE, Integer.TYPE, Integer.TYPE);
			int id = getNextAvailableGameObjectID();
			DynamicObject newObject = (DynamicObject)(con.newInstance(x, y, depth, id));
			newObject.create();
			allGameObjects.add(newObject);
			return new ObjectReference(id);
		}
		catch (NoSuchMethodException e) {
			if (MainClass.DEBUG)
				System.out.println("Attempt to create DynamicObject resulted in NoSuchMethodException.");
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			if (MainClass.DEBUG)
				System.out.println("Attempt to create DynamicObject resulted in InvocationTargetException.");
			e.printStackTrace();
		}
		catch (InstantiationException e) {
			if (MainClass.DEBUG)
				System.out.println("Attempt to create DynamicObject resulted in InstantiationException.");
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			if (MainClass.DEBUG)
				System.out.println("Attempt to create DynamicObject resulted in IllegalAccessException.");
			e.printStackTrace();
		}
		// -1 is not a valid instance ID, so nothing will happen if you try to do something to it
		return new ObjectReference(-1);
	}

	public void instanceDestroy(ObjectReference ref) {
		if (ref.instanceID() != -1) {
			for (DynamicObject object: allGameObjects) {
				if (object.getInstanceID() == ref.instanceID()) {
					allGameObjects.remove(object);
					break;
				}
			}
		}
	}

	public void instanceDestroy(Class objectClass) {
		allGameObjects.removeIf(object -> object.getClass() == objectClass);
	}

	public boolean instanceExists(ObjectReference ref) {
		if (ref.instanceID() != -1) {
			for (DynamicObject object: allGameObjects) {
				if (object.getInstanceID() == ref.instanceID()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean instanceExists(Class objectClass) {
		for (DynamicObject object: allGameObjects) {
			if (object.getClass() == objectClass) {
				return true;
			}
		}
		return false;
	}
}
