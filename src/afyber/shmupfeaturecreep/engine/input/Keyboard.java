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
		allKeys.put("up", 0);
		allKeys.put("down", 0);
		allKeys.put("left", 0);
		allKeys.put("right", 0);
		allKeys.put("escape", 0);
		allKeys.put("enter", 0);
		allKeys.put("backspace", 0);
		allKeys.put("comma", 0);
		allKeys.put("period", 0);
		allKeys.put("slash", 0);
		allKeys.put("backslash", 0);
		allKeys.put("semicolon", 0);
		allKeys.put("1", 0);
		allKeys.put("2", 0);
		allKeys.put("3", 0);
		allKeys.put("4", 0);
		allKeys.put("5", 0);
		allKeys.put("6", 0);
		allKeys.put("7", 0);
		allKeys.put("8", 0);
		allKeys.put("9", 0);
		allKeys.put("0", 0);
		allKeys.put("q", 0);
		allKeys.put("w", 0);
		allKeys.put("e", 0);
		allKeys.put("r", 0);
		allKeys.put("t", 0);
		allKeys.put("y", 0);
		allKeys.put("u", 0);
		allKeys.put("i", 0);
		allKeys.put("o", 0);
		allKeys.put("p", 0);
		allKeys.put("a", 0);
		allKeys.put("s", 0);
		allKeys.put("d", 0);
		allKeys.put("f", 0);
		allKeys.put("g", 0);
		allKeys.put("h", 0);
		allKeys.put("j", 0);
		allKeys.put("k", 0);
		allKeys.put("l", 0);
		allKeys.put("z", 0);
		allKeys.put("x", 0);
		allKeys.put("c", 0);
		allKeys.put("v", 0);
		allKeys.put("b", 0);
		allKeys.put("n", 0);
		allKeys.put("m", 0);
	}

	private record KeyStateChange(String key, int newState) {}
}
