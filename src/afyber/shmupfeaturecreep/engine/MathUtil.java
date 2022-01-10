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
}
