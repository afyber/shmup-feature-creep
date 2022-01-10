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

	public static int randInt(int bound) {
		return random.nextInt(bound);
	}
	public static int randInt(int minimum, int bound) {
		return random.nextInt(minimum, bound);
	}

	public static double random() {
		return random.nextDouble();
	}

	public static double randDouble(float bound) {
		return random() * bound;
	}

	public static void setSeed(long seed) {
		random.setSeed(seed);
	}

	public static void randomize() {
		random.setSeed(System.currentTimeMillis());
	}

	public static String choose(String... args) {
		return args[randInt(args.length)];
	}

	public static int choose(Integer... args) {
		return args[randInt(args.length)];
	}

	public static double choose(Double... args) {
		return args[randInt(args.length)];
	}
}
