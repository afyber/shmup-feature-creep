package afyber.shmupfeaturecreep.engine;

public class MathUtil {
	private MathUtil() {}

	public static double interpolateExp(double number, double target, double factor, double max) {
		if (Math.abs(number - target) < 0.000000001) {
			return target;
		}
		double change = (target - number) / factor;
		if (Math.abs(change) > max) {
			return number + (change >= 0 ? max : -max);
		}
		return number + change;
	}

	public static boolean areRectanglesIntersecting(int c1X, int c1Y, int c2X, int c2Y, int o1X, int o1Y, int o2X, int o2Y) {
		// this works because corner #1 is always the top left and corner #2 is always the bottom right,
		// if that ever becomes not true this function will no longer work
		return !(o1X > c2X || o1Y > c2Y || o2X < c1X || o2Y < c1Y);
	}
}
