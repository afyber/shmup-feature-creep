package afyber.shmupfeaturecreep.engine;

/**
 * Do the timing stuff n things.
 *
 * @author afyber
 */
public class Timing {
	private Timing() {}

	private static long lastMeasuredStartNanos;
	private static long lastMeasuredEndNanos;
	private static long idealFrameTimeNanos;

	public static void mainLoopBodyStarted() {
		lastMeasuredStartNanos = System.nanoTime();
	}

	public static void mainLoopBodyEnded() {
		lastMeasuredEndNanos = System.nanoTime();
	}

	public static long calculateTimeAndWaitThread() {
		if (lastMeasuredEndNanos - lastMeasuredStartNanos < idealFrameTimeNanos) {
			long millis = Math.round(idealFrameTimeNanos - (lastMeasuredEndNanos - lastMeasuredStartNanos) / 1000.0);
			GeneralUtil.sleepHandlingInterrupt(millis);
		}
		return (lastMeasuredEndNanos - lastMeasuredStartNanos);
	}

	public static void setIdealFrameTimeNanos(long frameTimeNanos) {
		idealFrameTimeNanos = frameTimeNanos;
	}

	public static long getIdealFrameTimeNanos() {
		return idealFrameTimeNanos;
	}
}
