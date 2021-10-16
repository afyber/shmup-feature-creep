package afyber.shmupfeaturecreep.engine.world;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.Screen;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.rooms.ObjectReference;
import afyber.shmupfeaturecreep.engine.rooms.StaticObject;
import afyber.shmupfeaturecreep.engine.sprites.SpriteSheetRegion;
import afyber.shmupfeaturecreep.game.TestInstanceClass;
import afyber.shmupfeaturecreep.game.TestInstanceClass2;
import afyber.shmupfeaturecreep.game.TestInstanceClass3;

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

	private WorldMiddleman worldMiddleman;

	public World() {
		allTiles = new ArrayList<>();
		allGameObjects = new ArrayList<>();
		worldMiddleman = new WorldMiddleman(this);
		allTiles.add(new StaticObject("sprite_2", 16, 32));
		allTiles.add(new StaticObject("sprite_3", 64, 128));
		for (int i = 0; i < 100; i++) {
			createInstance(TestInstanceClass2.class, 128, 128, 0);
		}
		createInstance(TestInstanceClass.class, 64, 64, 0);
		createInstance(TestInstanceClass3.class, 256, 256, -10);
	}

	public void drawAll() {
		for (StaticObject tile: allTiles) {
			tile.draw();
		}
		for (DynamicObject gameObject: allGameObjects) {
			gameObject.preDraw(worldMiddleman);
		}
		for (DynamicObject gameObject: allGameObjects) {
			gameObject.draw(worldMiddleman);
		}
		for (DynamicObject gameObject: allGameObjects) {
			gameObject.postDraw(worldMiddleman);
		}
	}

	public void updateAll() {
		for (DynamicObject gameObject: allGameObjects) {
			gameObject.preUpdate(worldMiddleman);
		}
		for (DynamicObject gameObject: allGameObjects) {
			gameObject.update(worldMiddleman);
		}
		for (DynamicObject gameObject: allGameObjects) {
			gameObject.postUpdate(worldMiddleman);
		}
	}

	public void alarmAll() {
		for (DynamicObject gameObject: allGameObjects) {
			for (int i = 0; i < 10; i++) {
				// NOTE: here, if the alarm is > 0, subtract 1
				int value = gameObject.getAlarm(i);
				if (value > 0) {
					gameObject.setAlarm(i, value - 1);

					// here, if it has just become 0 from that subtraction, trigger the alarm
					if (gameObject.getAlarm(i) == 0) {
						gameObject.setAlarm(i, -1);
						switch (i) {
							case 0 -> gameObject.alarm1(worldMiddleman);
							case 1 -> gameObject.alarm2(worldMiddleman);
							case 2 -> gameObject.alarm3(worldMiddleman);
							case 3 -> gameObject.alarm4(worldMiddleman);
							case 4 -> gameObject.alarm5(worldMiddleman);
							case 5 -> gameObject.alarm6(worldMiddleman);
							case 6 -> gameObject.alarm7(worldMiddleman);
							case 7 -> gameObject.alarm8(worldMiddleman);
							case 8 -> gameObject.alarm9(worldMiddleman);
							case 9 -> gameObject.alarm10(worldMiddleman);
						}
					}
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
			newObject.create(worldMiddleman);
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
					object.destroy(worldMiddleman);
					allGameObjects.remove(object);
					break;
				}
			}
		}
	}

	public void instanceDestroy(Class objectClass) {
		for (DynamicObject object: allGameObjects) {
			if (object.getClass() == objectClass) {
				object.destroy(worldMiddleman);
				allGameObjects.remove(object);
				break;
			}
		}
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

	public boolean isColliding(DynamicObject caller, ObjectReference other) {
		DynamicObject otherObject = null;
		for (DynamicObject object: allGameObjects) {
			if (object.getInstanceID() == other.instanceID()) {
				otherObject = object;
				break;
			}
		}

		if (otherObject != null) {
			SpriteSheetRegion callerRegion = Screen.getSpriteScaled(caller.getCollisionIndex(), caller.getImageXScale(), caller.getImageYScale());
			SpriteSheetRegion otherRegion = Screen.getSpriteScaled(otherObject.getCollisionIndex(), otherObject.getImageXScale(), otherObject.getImageYScale());
			int otherCorner1X = Math.round(otherObject.getX() - otherRegion.originX());
			int otherCorner1Y = Math.round(otherObject.getY() - otherRegion.originY());
			int callerCorner1X = Math.round(caller.getX() - callerRegion.originX());
			int callerCorner1Y = Math.round(caller.getY() - callerRegion.originY());

			byte[][] callerData = callerRegion.data();
			byte[][] otherData = otherRegion.data();
			// for every pixel in the caller's collision
			for (int i = 0; i < callerRegion.dataHeight(); i++) {
				for (int c = 0; c < callerRegion.dataWidth(); c++) {
					if (callerData[i][c * 4 + 3] != 0) {
						// for every pixel in the other's collision
						for (int i2 = 0; i2 < otherRegion.dataHeight(); i2++) {
							for (int c2 = 0; c2 < otherRegion.dataWidth(); c2++) {
								if (otherData[i][c * 4 + 3] != 0) {
									if (callerCorner1X + c == otherCorner1X + c2 && callerCorner1Y + i == otherCorner1Y + i2) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	public boolean isColliding(DynamicObject caller, Class other) {
		for (DynamicObject object: allGameObjects) {
			if ((object.getClass() == other || object.getClass().isInstance(other)) && isColliding(caller, new ObjectReference(object.getInstanceID()))) {
				return true;
			}
		}
		return false;
	}
}
