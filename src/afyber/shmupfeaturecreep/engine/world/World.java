package afyber.shmupfeaturecreep.engine.world;

import afyber.shmupfeaturecreep.MainClass;
import afyber.shmupfeaturecreep.engine.Screen;
import afyber.shmupfeaturecreep.engine.rooms.DynamicObject;
import afyber.shmupfeaturecreep.engine.rooms.StaticObject;
import afyber.shmupfeaturecreep.engine.sprites.SpriteSheetRegion;
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
							case 0 -> object.alarm1(worldMiddleman);
							case 1 -> object.alarm2(worldMiddleman);
							case 2 -> object.alarm3(worldMiddleman);
							case 3 -> object.alarm4(worldMiddleman);
							case 4 -> object.alarm5(worldMiddleman);
							case 5 -> object.alarm6(worldMiddleman);
							case 6 -> object.alarm7(worldMiddleman);
							case 7 -> object.alarm8(worldMiddleman);
							case 8 -> object.alarm9(worldMiddleman);
							case 9 -> object.alarm10(worldMiddleman);
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

	public void queueObjectDestruction(int objRef) {
		gameObjectsToRemove.add(new ObjectDestructionReference(true, objRef, null));
	}

	public void queueObjectDestruction(Class classRef) {
		gameObjectsToRemove.add(new ObjectDestructionReference(false, -1, classRef));
	}

	public void setAlarm(int objRef, int alarm, int value) {
		for (DynamicObject object: allGameObjects) {
			if (object.getInstanceID() == objRef) {
				object.setAlarm(alarm, value);
			}
		}
		for (DynamicObject object: gameObjectsCreatedThisFrame) {
			if (object.getInstanceID() == objRef) {
				object.setAlarm(alarm, value);
			}
		}
	}

	public int getAlarm(int objRef, int alarm) {
		for (DynamicObject object: allGameObjects) {
			if (object.getInstanceID() == objRef) {
				return object.getAlarm(alarm);
			}
		}
		for (DynamicObject object: gameObjectsCreatedThisFrame) {
			if (object.getInstanceID() == objRef) {
				return object.getAlarm(alarm);
			}
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
		if (objRef != -1) {
			for (DynamicObject object: allGameObjects) {
				if (object.getInstanceID() == objRef) {
					object.destroy(worldMiddleman);
					allGameObjects.remove(object);
					break;
				}
			}
			for (DynamicObject object: gameObjectsCreatedThisFrame) {
				if (object.getInstanceID() == objRef) {
					object.destroy(worldMiddleman);
					allGameObjects.remove(object);
					break;
				}
			}
		}
	}

	public void instanceDestroy(Class classRef) {
		for (DynamicObject object: allGameObjects) {
			if (object.getClass() == classRef) {
				object.destroy(worldMiddleman);
				allGameObjects.remove(object);
			}
		}
		for (DynamicObject object: gameObjectsCreatedThisFrame) {
			if (object.getClass() == classRef) {
				object.destroy(worldMiddleman);
				allGameObjects.remove(object);
			}
		}
	}

	public boolean instanceExists(int objRef) {
		if (objRef != -1) {
			for (DynamicObject object: allGameObjects) {
				if (object.getInstanceID() == objRef) {
					return true;
				}
			}
			for (DynamicObject object: gameObjectsCreatedThisFrame) {
				if (object.getInstanceID() == objRef) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean instanceExists(Class classRef) {
		for (DynamicObject object: allGameObjects) {
			if (object.getClass() == classRef) {
				return true;
			}
		}
		for (DynamicObject object: gameObjectsCreatedThisFrame) {
			if (object.getClass() == classRef) {
				return true;
			}
		}
		return false;
	}

	public boolean isColliding(DynamicObject caller, int objRef) {
		DynamicObject otherObject = null;
		for (DynamicObject object: allGameObjects) {
			if (object.getInstanceID() == objRef) {
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
			// TODO: inefficient, checks even if they're too far away for a collision to be possible
			// FIXME: Nothing about this makes sense

			byte[][] callerData = callerRegion.data();
			byte[][] otherData = otherRegion.data();
			// for every pixel in the caller's collision
			for (int i = 0; i < callerRegion.dataHeight(); i++) {
				for (int c = 0; c < callerRegion.dataWidth(); c++) {
					if (callerData[i][c * 4 + 3] != 0) {
						// for every pixel in the other's collision
						for (int i2 = 0; i2 < otherRegion.dataHeight(); i2++) {
							for (int c2 = 0; c2 < otherRegion.dataWidth() / 4; c2++) {
								if (otherData[i2][c2 * 4 + 3] != 0) {
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

	public boolean isColliding(DynamicObject caller, Class classRef) {
		for (DynamicObject object: allGameObjects) {
			if ((object.getClass() == classRef || object.getClass().isInstance(classRef)) && isColliding(caller, object.getInstanceID())) {
				return true;
			}
		}
		return false;
	}
}
