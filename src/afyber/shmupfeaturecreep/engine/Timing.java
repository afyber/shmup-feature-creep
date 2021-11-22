package afyber.shmupfeaturecreep.engine;

/**
 * Do the timing stuff n things.
 *
 * @author afyber
 */
public class Timing {
	private Timing() {}

	private static long lastMeasuredStartMillis;
	private static long lastMeasuredEndMillis;
	private static long idealFrameTimeMillis;

	public static void mainLoopBodyStarted() {
		lastMeasuredStartMillis = System.currentTimeMillis();
	}

	public static void mainLoopBodyEnded() {
		lastMeasuredEndMillis = System.currentTimeMillis();
	}

	public static long calculateTimeAndWaitThread() throws InterruptedException {
		if (lastMeasuredEndMillis - lastMeasuredStartMillis < idealFrameTimeMillis) {
			Thread.sleep(idealFrameTimeMillis - (lastMeasuredEndMillis - lastMeasuredStartMillis));
		}
		return (lastMeasuredEndMillis - lastMeasuredStartMillis);
	}

	public static void setIdealFrameTimeMillis(long frameTimeMillis) {
		idealFrameTimeMillis = frameTimeMillis;
	}

	public static long getIdealFrameTimeMillis() {
		return idealFrameTimeMillis;
	}
}
