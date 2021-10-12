package afyber.shmupfeaturecreep.engine;

import java.util.Random;

public class RandomUtil {
	private RandomUtil() {}

	private static Random random = new Random(System.currentTimeMillis());

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
}
