package afyber.shmupfeaturecreep.engine;

import java.util.Random;

/**
 * Utils for random numbers
 *
 * @author afyber
 */
public class RandomUtil {
	private RandomUtil() {}

	private static final Random random = new Random(System.currentTimeMillis());

	public static int randInt(int boundInclusive) {
		return random.nextInt(boundInclusive + 1);
	}

	public static float random() {
		return random.nextFloat();
	}

	public static float randFloat(float bound) {
		return random() * bound;
	}

	public static void setSeed(long seed) {
		random.setSeed(seed);
	}

	public static void randomize() {
		random.setSeed(System.currentTimeMillis());
	}
}
