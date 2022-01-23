package afyber.shmupfeaturecreep.engine.world;

import java.util.HashMap;

/**
 * Stores global variables for more GML-like code
 *
 * @author afyber
 */
public class Global {
	private Global() {}

	private static final HashMap<String, Integer> allIntGlobals = new HashMap<>();
	private static final HashMap<String, Double> allFloatGlobals = new HashMap<>();
	private static final HashMap<String, String> allStringGlobals = new HashMap<>();

	public static void setIntGlobal(String name, int value) {
		allIntGlobals.put(name, value);
	}

	public static void setFloatGlobal(String name, double value) {
		allFloatGlobals.put(name, value);
	}

	public static void setStringGlobal(String name, String value) {
		allStringGlobals.put(name, value);
	}

	public static int getIntGlobal(String name) {
		return allIntGlobals.get(name);
	}

	public static double getFloatGlobal(String name) {
		return allFloatGlobals.get(name);
	}

	public static String getStringGlobal(String name) {
		return allStringGlobals.get(name);
	}
}
