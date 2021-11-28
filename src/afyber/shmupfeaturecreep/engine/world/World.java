package afyber.shmupfeaturecreep.engine.world;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.GeneralUtil;
import afyber.shmupfeaturecreep.engine.Registry;
import afyber.shmupfeaturecreep.engine.Screen;
import afyber.shmupfeaturecreep.engine.errors.ObjectNotDefinedError;
import afyber.shmupfeaturecreep.engine.output.LoggingLevel;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.rooms.ObjectCreationReference;
import afyber.shmupfeaturecreep.engine.rooms.Room;
import afyber.shmupfeaturecreep.engine.rooms.StaticObject;
import afyber.shmupfeaturecreep.engine.sprites.SpriteInformation;
import afyber.shmupfeaturecreep.engine.sprites.SpriteSheetRegion;

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

	private String roomChange = "";

	private final ArrayList<StaticObject> allTiles;
	private final ArrayList<DynamicObject> allGameObjects;
	// These objects will be removed the frame after the call to instanceDestroy()
	private final ArrayList<ObjectDestructionReference> gameObjectsToRemove;
	// this system (I think) even allows for create code to create objects
	// note that these objects are NOT updated, alarmed, or drawn, they can ONLY have their fields changed
	// they are added to the allGameObjects list and will start doing that stuff on the next frame
	private final ArrayList<DynamicObject> gameObjectsCreatedThisFrame;

	private int nextAvailableGameObjectID = 1;

	private final WorldMiddleman worldMiddleman;

	public World(Room room) {
		allTiles = new ArrayList<>();
		allGameObjects = new ArrayList<>();
		gameObjectsToRemove = new ArrayList<>();
		gameObjectsCreatedThisFrame = new ArrayList<>();

		worldMiddleman = new WorldMiddleman(this);

		loadRoomData(room);
	}

	public void loadRoomData(Room room) {
		allTiles.addAll(room.tiles());
		for (ObjectCreationReference ref: room.objects()) {
			createInstance(ref.classOfObject(), ref.x(), ref.y(), ref.imageXScale(), ref.imageYScale(), ref.depth());
		}
	}

	public void changeRoom(String roomName) {
		roomChange = roomName;
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

	public void physicsUpdateAll() {
		for (DynamicObject object: allGameObjects) {
			object.x += object.xSpeed;
			object.y += object.ySpeed;
		}
	}

	public void drawAll() {
		Screen.setIsDrawing(true);
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
		Screen.setIsDrawing(false);
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
				if (object.alarm[i] > 0) {
					object.alarm[i] -= 1;

					// here, if it has just become 0 from that subtraction, trigger the alarm
					if (object.alarm[i] == 0) {
						object.alarm[i] = -1;
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
							default -> MainClass.LOGGER.log(LoggingLevel.WARNING, "Something has gone very wrong in the alarm code");
						}
					}
				}
			}
		}
	}

	public void queueObjectDestruction(int objRef) {
		gameObjectsToRemove.add(new ObjectDestructionReference(true, objRef, null));
	}

	public void queueObjectDestruction(String classRef) {
		gameObjectsToRemove.add(new ObjectDestructionReference(false, -1, classRef));
	}

	public DynamicObject createInstance(String classRef, float x, float y, int depth) {
		return createInstance(classRef, x, y, 1, 1, depth);
	}
	public DynamicObject createInstance(String classRef, float x, float y, float imageXScale, float imageYScale, int depth) {
		if (Registry.hasObject(classRef)) {
			Class<? extends DynamicObject> objectClass = Registry.getObject(classRef);
			try {
				Constructor<? extends DynamicObject> con = objectClass.getConstructor(Float.TYPE, Float.TYPE, Integer.TYPE, Integer.TYPE);
				DynamicObject newObject = con.newInstance(x, y, depth, getNextAvailableGameObjectID());
				newObject.imageXScale = imageXScale;
				newObject.imageYScale = imageYScale;
				newObject.create(worldMiddleman);
				gameObjectsCreatedThisFrame.add(newObject);
				return newObject;
			} catch (NoSuchMethodException e) {
				MainClass.LOGGER.log(LoggingLevel.WARNING, "Attempt to create DynamicObject resulted in NoSuchMethodException:", e);
			} catch (InvocationTargetException e) {
				MainClass.LOGGER.log(LoggingLevel.WARNING, "Attempt to create DynamicObject resulted in InvocationTargetException:", e);
			} catch (InstantiationException e) {
				MainClass.LOGGER.log(LoggingLevel.WARNING, "Attempt to create DynamicObject resulted in InstantiationException:", e);
			} catch (IllegalAccessException e) {
				MainClass.LOGGER.log(LoggingLevel.WARNING, "Attempt to create DynamicObject resulted in IllegalAccessException:", e);
			}
		}
		else {
			MainClass.LOGGER.log(LoggingLevel.WARNING, "Object name \"" + classRef + "\" is not registered, unable to create");
			throw new ObjectNotDefinedError();
		}
		return null;
	}

	public void instanceDestroy(int objRef) {
		DynamicObject object = objRefToObject(objRef);
		if (object != null) {
			object.destroy(worldMiddleman);
			allGameObjects.remove(object);
		}
	}

	public void instanceDestroy(String classRef) {
		for (DynamicObject object: classRefToObjectList(classRef)) {
			object.destroy(worldMiddleman);
			allGameObjects.remove(object);
		}
	}

	public boolean instanceExists(int objRef) {
		return objRefToObject(objRef) != null;
	}

	public boolean instanceExists(String classRef) {
		return !classRefToObjectList(classRef).isEmpty();
	}

	// FIXME: I have found one situation where this works differently for different callers with the same two objects
	// This is likely to do with scaling inaccuracy
	private boolean isColliding(DynamicObject caller, DynamicObject other) {
		if (other == null || caller == null) {
			return false;
		}

		SpriteInformation callerInfo = Screen.getScaledSpriteInfo(caller.collisionIndex, caller.imageXScale, caller.imageYScale);
		SpriteInformation otherInfo = Screen.getScaledSpriteInfo(other.collisionIndex, other.imageXScale, other.imageYScale);

		if (otherInfo == null || callerInfo == null) {
			return false;
		}

		int callerCorner1X = Math.round(caller.x - callerInfo.originX());
		int callerCorner1Y = Math.round(caller.y - callerInfo.originY());
		int callerCorner2X = callerCorner1X + callerInfo.dataWidth();
		int callerCorner2Y = callerCorner1Y + callerInfo.dataHeight();
		int otherCorner1X = Math.round(other.x - otherInfo.originX());
		int otherCorner1Y = Math.round(other.y - otherInfo.originY());
		int otherCorner2X = otherCorner1X + otherInfo.dataWidth();
		int otherCorner2Y = otherCorner1Y + otherInfo.dataHeight();

		if (GeneralUtil.areRectanglesIntersecting(callerCorner1X, callerCorner1Y, callerCorner2X, callerCorner2Y,
				otherCorner1X, otherCorner1Y, otherCorner2X, otherCorner2Y)) {
			SpriteSheetRegion callerRegion = Screen.getSpriteScaled(caller.collisionIndex, caller.imageXScale, caller.imageYScale);
			SpriteSheetRegion otherRegion = Screen.getSpriteScaled(other.collisionIndex, other.imageXScale, other.imageYScale);

			if (otherRegion == null || callerRegion == null) {
				return false;
			}

			int[][] callerData = callerRegion.data();
			int[][] otherData = otherRegion.data();

			for (int i1 = 0; i1 < callerRegion.dataHeight(); i1++) {
				for (int c1 = 0; c1 < callerRegion.dataWidth(); c1++) {
					if ((callerData[i1][c1] >> 24 & 0xFF) != 0x0) {
						// these coordinates are relative to the top-left corner of the other sprite
						int otherI = callerCorner1X + c1 - otherCorner1X;
						int otherC = callerCorner1Y + i1 - otherCorner1Y;

						if (otherI < 0 || otherI >= otherRegion.dataHeight() || otherC < 0 || otherC >= otherRegion.dataWidth()) {
							continue;
						}

						if ((otherData[otherI][otherC] >> 24 & 0xFF) != 0x0) {
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

	public boolean isColliding(DynamicObject caller, String classRef, boolean includingChildren) {
		ArrayList<DynamicObject> allObj;
		if (includingChildren) {
			allObj = (ArrayList<DynamicObject>)classRefToObjectListInclChildren(classRef);
		}
		else {
			 allObj = (ArrayList<DynamicObject>)classRefToObjectList(classRef);
		}
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
				if (object.instanceID == objRef) {
					return object;
				}
			}
			for (DynamicObject object: gameObjectsCreatedThisFrame) {
				if (object.instanceID == objRef) {
					return object;
				}
			}
		}
		return null;
	}

	public List<DynamicObject> classRefToObjectList(String classRef) {
		ArrayList<DynamicObject> list = new ArrayList<>();
		for (DynamicObject object: allGameObjects) {
			if (classRef.equals(object.objectName)) {
				list.add(object);
			}
		}
		for (DynamicObject object: gameObjectsCreatedThisFrame) {
			if (classRef.equals(object.objectName)) {
				list.add(object);
			}
		}
		return list;
	}

	public List<DynamicObject> classRefToObjectListInclChildren(String classRef) {
		ArrayList<DynamicObject> list = new ArrayList<>();
		boolean hasChildren = false;
		ArrayList<String> children = new ArrayList<>();
		if (Registry.hasChildrenForObject(classRef)) {
			children.addAll(Registry.getChildrenOfObject(classRef));
			hasChildren = true;
		}
		for (DynamicObject object: allGameObjects) {
			if (classRef.equals(object.objectName)) {
				list.add(object);
			}
			else if (hasChildren) {
				for (String child: children) {
					if (child.equals(object.objectName)) {
						list.add(object);
					}
				}
			}
		}
		for (DynamicObject object: gameObjectsCreatedThisFrame) {
			if (classRef.equals(object.objectName)) {
				list.add(object);
			}
			else if (hasChildren) {
				for (String child: children) {
					if (classRef.equals(child)) {
						list.add(object);
					}
				}
			}
		}
		return list;
	}

	public boolean getIsRoomChange() {
		return !roomChange.equals("");
	}

	public String getRoomChangeRoomName() {
		return roomChange;
	}
}
