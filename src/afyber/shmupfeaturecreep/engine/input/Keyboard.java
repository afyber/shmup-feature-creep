package afyber.shmupfeaturecreep.engine.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A cool method of avoiding the multi-threading weirdness is to just use a queue. (it doesn't but whatever)
 *
 * @author afyber
 */
public class Keyboard {
	private Keyboard() {}

	// The string is the key code name, the int is 0 if the key is not pressed, 1 if it has just been pressed this frame
	// and 2 if it is being held down
	private static final HashMap<String, Integer> allKeys = new HashMap<>();

	private static final ArrayList<KeyStateChange> keyStateQueue = new ArrayList<>();

	// NOTE: PACKAGE-PRIVATE
	static synchronized void queueKeyDown(String keyName) {
		keyStateQueue.add(new KeyStateChange(keyName, 1));
	}

	static synchronized void queueKeyUp(String keyName) {
		keyStateQueue.add(new KeyStateChange(keyName, 0));
	}

	public static synchronized void applyKeyQueue() {
		for (KeyStateChange change: keyStateQueue) {
			if (change.key() == null || (change.newState() == 1 && allKeys.get(change.key()) > 0)) {
				continue;
			}
			allKeys.put(change.key(), change.newState());
		}
		keyStateQueue.clear();
	}

	public static void frameDone() {
		for (Map.Entry<String, Integer> entry: allKeys.entrySet()) {
			if (entry.getValue() == 1) {
				entry.setValue(2);
			}
		}
	}

	public static boolean keyDown(String name) {
		return allKeys.get(name) > 0;
	}

	public static boolean keyJustDown(String name) {
		return allKeys.get(name) == 1;
	}

	public static void clearKeys() {
		for (Map.Entry<Integer, String> entry: KeyboardHandler.keyMap.entrySet()) {
			allKeys.put(entry.getValue(), 0);
		}
	}

	private record KeyStateChange(String key, int newState) {}
}
