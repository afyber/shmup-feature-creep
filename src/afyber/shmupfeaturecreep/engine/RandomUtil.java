package afyber.shmupfeaturecreep.engine;

import java.util.List;
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

	public static boolean randBool() {
		return random.nextBoolean();
	}

	public static void setSeed(long seed) {
		random.setSeed(seed);
	}

	public static void randomize() {
		random.setSeed(System.currentTimeMillis());
	}

	public static <T> T choose(T... args) {
		return args[randInt(args.length)];
	}

	public static <T> T choose(List<T> list) {
		return list.get(randInt(list.size()));
	}
}
