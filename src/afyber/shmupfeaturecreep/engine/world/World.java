package afyber.shmupfeaturecreep.engine.world;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.rooms.StaticObject;
import afyber.shmupfeaturecreep.game.BattleController;
import afyber.shmupfeaturecreep.game.Player1;

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
	private ArrayList<ObjectDestructionReference> gameObjectsToRemove;
	// this system (I think) even allows for create code to create objects
	// note that these objects are NOT updated, alarmed, or drawn, they can ONLY have their fields changed
	// they are added to the allGameObjects list and will start doing that stuff after 1 frame
	private ArrayList<DynamicObject> gameObjectsCreatedThisFrame;

	private int nextAvailableGameObjectID = 1;

	private WorldMiddleman worldMiddleman;

	public World() {
		allTiles = new ArrayList<>();
		allGameObjects = new ArrayList<>();
		gameObjectsToRemove = new ArrayList<>();
		gameObjectsCreatedThisFrame = new ArrayList<>();
		worldMiddleman = new WorldMiddleman(this);
		// TODO: load room data here
		createInstance(BattleController.class, 0, 0, 0);
		createInstance(Player1.class, 320, 256, 100);
	}

	public void destroyAll() {
		for (ObjectDestructionReference destroyRef: gameObjectsToRemove) {
			if (destroyRef.useInstanceID()) {
				instanceDestroy(destroyRef.objRef());
			}
			else {
				instanceDestroy(destroyRef.classRef());
			}
		}
		gameObjectsToRemove.clear();
	}

	public void moveAll() {
		allGameObjects.addAll(gameObjectsCreatedThisFrame);
		gameObjectsCreatedThisFrame.clear();
	}

	public void drawAll() {
		for (StaticObject tile: allTiles) {
			tile.draw();
		}
		for (DynamicObject object: allGameObjects) {
			object.preDraw(worldMiddleman);
		}
		for (DynamicObject object: allGameObjects) {
			object.draw(worldMiddleman);
		}
		for (DynamicObject object: allGameObjects) {
			object.postDraw(worldMiddleman);
		}
	}

	public void updateAll() {
		for (DynamicObject object: allGameObjects) {
			object.preUpdate(worldMiddleman);
		}
		for (DynamicObject object: allGameObjects) {
			object.update(worldMiddleman);
		}
		for (DynamicObject object: allGameObjects) {
			object.postUpdate(worldMiddleman);
		}
	}

	public void alarmAll() {
		for (DynamicObject object: allGameObjects) {
			for (int i = 0; i < 10; i++) {
				// NOTE: here, if the alarm is > 0, subtract 1
				int value = object.getAlarm(i);
				if (value > 0) {
					object.setAlarm(i, value - 1);

					// here, if it has just become 0 from that subtraction, trigger the alarm
					if (object.getAlarm(i) == 0) {
						object.setAlarm(i, -1);
						switch (i) {
							case 0 -> object.alarm0(worldMiddleman);
							case 1 -> object.alarm1(worldMiddleman);
							case 2 -> object.alarm2(worldMiddleman);
							case 3 -> object.alarm3(worldMiddleman);
							case 4 -> object.alarm4(worldMiddleman);
							case 5 -> object.alarm5(worldMiddleman);
							case 6 -> object.alarm6(worldMiddleman);
							case 7 -> object.alarm7(worldMiddleman);
							case 8 -> object.alarm8(worldMiddleman);
							case 9 -> object.alarm9(worldMiddleman);
						}
					}
				}
			}
		}
	}

	public void queueObjectDestruction(int objRef) {
		gameObjectsToRemove.add(new ObjectDestructionReference(true, objRef, null));
	}

	public void queueObjectDestruction(Class classRef) {
		gameObjectsToRemove.add(new ObjectDestructionReference(false, -1, classRef));
	}

	public void setAlarm(int objRef, int alarm, int value) {
		DynamicObject object = objRefToObject(objRef);
		if (object != null) {
			object.setAlarm(alarm, value);
			return;
		}
		object = objRefToObjectInJustCreated(objRef);
		if (object != null) {
			object.setAlarm(alarm, value);
		}
	}

	public int getAlarm(int objRef, int alarm) {
		DynamicObject object = objRefToObject(objRef);
		if (object != null) {
			return object.getAlarm(alarm);
		}
		object = objRefToObjectInJustCreated(objRef);
		if (object != null) {
			return object.getAlarm(alarm);
		}
		return -1;
	}

	public int createInstance(Class classRef, float x, float y, int depth) {
		try {
			Constructor con = classRef.getConstructor(Float.TYPE, Float.TYPE, Integer.TYPE, Integer.TYPE);
			int id = getNextAvailableGameObjectID();
			DynamicObject newObject = (DynamicObject)(con.newInstance(x, y, depth, id));
			newObject.create(worldMiddleman);
			gameObjectsCreatedThisFrame.add(newObject);
			return id;
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
		return -1;
	}

	public void instanceDestroy(int objRef) {
		DynamicObject object = objRefToObject(objRef);
		if (object != null) {
			object.destroy(worldMiddleman);
			allGameObjects.remove(object);
			return;
		}
		object = objRefToObjectInJustCreated(objRef);
		if (object != null) {
			object.destroy(worldMiddleman);
			gameObjectsCreatedThisFrame.remove(object);
		}
	}

	public void instanceDestroy(Class classRef) {
		for (DynamicObject object: classRefToObjectList(classRef)) {
			object.destroy(worldMiddleman);
			allGameObjects.remove(object);
		}
		for (DynamicObject object: classRefToObjectListInJustCreated(classRef)) {
			object.destroy(worldMiddleman);
			gameObjectsCreatedThisFrame.remove(object);
		}
	}

	public boolean instanceExists(int objRef) {
		return objRefToObject(objRef) != null || objRefToObjectInJustCreated(objRef) != null;
	}

	public boolean instanceExists(Class classRef) {
		return !classRefToObjectList(classRef).isEmpty() || !classRefToObjectListInJustCreated(classRef).isEmpty();
	}

	private int getNextAvailableGameObjectID() {
		int toReturn = nextAvailableGameObjectID;
		nextAvailableGameObjectID++;
		return toReturn;
	}

	private DynamicObject objRefToObject(int objRef) {
		if (objRef != -1) {
			for (DynamicObject object: allGameObjects) {
				if (object.getInstanceID() == objRef) {
					return object;
				}
			}
		}
		return null;
	}

	private DynamicObject objRefToObjectInJustCreated(int objRef) {
		if (objRef != -1) {
			for (DynamicObject object: gameObjectsCreatedThisFrame) {
				if (object.getInstanceID() == objRef) {
					return object;
				}
			}
		}
		return null;
	}

	private ArrayList<DynamicObject> classRefToObjectList(Class classRef) {
		ArrayList<DynamicObject> list = new ArrayList<>();
		for (DynamicObject object: allGameObjects) {
			if (classRef.isInstance(object)) {
				list.add(object);
			}
		}
		return list;
	}

	private ArrayList<DynamicObject> classRefToObjectListInJustCreated(Class classRef) {
		ArrayList<DynamicObject> list = new ArrayList<>();
		for (DynamicObject object: gameObjectsCreatedThisFrame) {
			if (classRef.isInstance(object)) {
				list.add(object);
			}
		}
		return list;
	}
}
