package afyber.shmupfeaturecreep.engine.world;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.GeneralUtil;
import afyber.shmupfeaturecreep.engine.Screen;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.rooms.StaticObject;
import afyber.shmupfeaturecreep.engine.sprites.SpriteInformation;
import afyber.shmupfeaturecreep.engine.sprites.SpriteSheetRegion;
import afyber.shmupfeaturecreep.game.BattleController;
import afyber.shmupfeaturecreep.game.Player1;
import afyber.shmupfeaturecreep.game.Scorecard1;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is very important because it does a lot of things, like holding the list of all objects and tiles.
 *
 * @author afyber
 */
public class World {

	private final ArrayList<StaticObject> allTiles;
	private final ArrayList<DynamicObject> allGameObjects;
	private final ArrayList<ObjectDestructionReference> gameObjectsToRemove;
	// this system (I think) even allows for create code to create objects
	// note that these objects are NOT updated, alarmed, or drawn, they can ONLY have their fields changed
	// they are added to the allGameObjects list and will start doing that stuff on the next frame
	private final ArrayList<DynamicObject> gameObjectsCreatedThisFrame;

	private int nextAvailableGameObjectID = 1;

	private final WorldMiddleman worldMiddleman;

	public World() {
		allTiles = new ArrayList<>();
		allGameObjects = new ArrayList<>();
		gameObjectsToRemove = new ArrayList<>();
		gameObjectsCreatedThisFrame = new ArrayList<>();

		worldMiddleman = new WorldMiddleman(this);
		// TODO: load room data here
		createInstance(BattleController.class, 0, 0, 0);
		createInstance(Player1.class, 320, 256, 100);
		createInstance(Scorecard1.class, 0, 0, 1000);
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

	public void moveAllNewlyAdded() {
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
							default -> System.out.println("What on earth happened this is impossible.");
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
		}
	}

	public void instanceDestroy(Class classRef) {
		for (DynamicObject object: classRefToObjectList(classRef)) {
			object.destroy(worldMiddleman);
			allGameObjects.remove(object);
		}
	}

	public boolean instanceExists(int objRef) {
		return objRefToObject(objRef) != null;
	}

	public boolean instanceExists(Class classRef) {
		return !classRefToObjectList(classRef).isEmpty();
	}

	// FIXME: I have found one situation where this works differently for different callers with the same two objects
	// This is likely to do with scaling inaccuracy
	private boolean isColliding(DynamicObject caller, DynamicObject other) {
		if (other == null || caller == null) {
			return false;
		}

		SpriteInformation callerInfo = Screen.getScaledSpriteInfo(caller.getCollisionIndex(), caller.getImageXScale(), caller.getImageYScale());
		SpriteInformation otherInfo = Screen.getScaledSpriteInfo(other.getCollisionIndex(), other.getImageXScale(), other.getImageYScale());

		if (otherInfo == null || callerInfo == null) {
			return false;
		}

		int callerCorner1X = Math.round(caller.getX() - callerInfo.originX());
		int callerCorner1Y = Math.round(caller.getY() - callerInfo.originY());
		int callerCorner2X = callerCorner1X + (callerInfo.dataWidth() / 4);
		int callerCorner2Y = callerCorner1Y + callerInfo.dataHeight();
		int otherCorner1X = Math.round(other.getX() - otherInfo.originX());
		int otherCorner1Y = Math.round(other.getY() - otherInfo.originY());
		int otherCorner2X = otherCorner1X + otherInfo.dataWidth() / 4;
		int otherCorner2Y = otherCorner1Y + otherInfo.dataHeight();

		if (GeneralUtil.areRectanglesIntersecting(callerCorner1X, callerCorner1Y, callerCorner2X, callerCorner2Y,
				otherCorner1X, otherCorner1Y, otherCorner2X, otherCorner2Y)) {
			SpriteSheetRegion callerRegion = Screen.getSpriteScaled(caller.getCollisionIndex(), caller.getImageXScale(), caller.getImageYScale());
			SpriteSheetRegion otherRegion = Screen.getSpriteScaled(other.getCollisionIndex(), other.getImageXScale(), other.getImageYScale());

			if (otherRegion == null || callerRegion == null) {
				return false;
			}

			byte[][] callerData = callerRegion.data();
			byte[][] otherData = otherRegion.data();

			for (int i1 = 0; i1 < callerRegion.dataHeight(); i1++) {
				for (int c1 = 0; c1 < callerRegion.dataWidth() / 4; c1++) {
					if (Byte.toUnsignedInt(callerData[i1][c1 * 4 + 3]) != 0x0) {
						int otherI = callerCorner1X + c1 - otherCorner1X;
						int otherC = callerCorner1Y + i1 - otherCorner1Y;

						if (otherI < 0 || otherI >= otherRegion.dataHeight() || otherC < 0 || otherC >= otherRegion.dataWidth() / 4) {
							continue;
						}

						if (Byte.toUnsignedInt(otherData[otherI][otherC * 4 + 3]) != 0x0) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isColliding(DynamicObject caller, int objRef) {
		return isColliding(caller, objRefToObject(objRef));
	}

	public boolean isColliding(DynamicObject caller, Class classRef) {
		ArrayList<DynamicObject> allObj = (ArrayList<DynamicObject>)classRefToObjectList(classRef);
		for (DynamicObject object: allObj) {
			if (isColliding(caller, object)) {
				return true;
			}
		}
		return false;
	}

	private int getNextAvailableGameObjectID() {
		int toReturn = nextAvailableGameObjectID;
		nextAvailableGameObjectID++;
		return toReturn;
	}

	public DynamicObject objRefToObject(int objRef) {
		if (objRef != -1) {
			for (DynamicObject object: allGameObjects) {
				if (object.getInstanceID() == objRef) {
					return object;
				}
			}
			for (DynamicObject object: gameObjectsCreatedThisFrame) {
				if (object.getInstanceID() == objRef) {
					return object;
				}
			}
		}
		return null;
	}

	public List<DynamicObject> classRefToObjectList(Class classRef) {
		ArrayList<DynamicObject> list = new ArrayList<>();
		for (DynamicObject object: allGameObjects) {
			if (classRef.isInstance(object)) {
				list.add(object);
			}
		}
		for (DynamicObject object: gameObjectsCreatedThisFrame) {
			if (classRef.isInstance(object)) {
				list.add(object);
			}
		}
		return list;
	}
}
