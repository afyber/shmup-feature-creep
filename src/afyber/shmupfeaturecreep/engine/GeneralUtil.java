package afyber.shmupfeaturecreep.engine;

import afyber.shmupfeaturecreep.Main;
import afyber.shmupfeaturecreep.engine.output.EngineLogger;

import java.io.IOException;
import java.io.InputStream;

/**
 * A few things that wanted their own class
 *
 * @author afyber
 */
public class GeneralUtil {
	private GeneralUtil() {}

	public static void reverseTopSpriteArray(int[][] frameArray) {
		int[] tmp;
		for (int i = 0; i < frameArray.length / 2; i++) {
			tmp = frameArray[i];
			frameArray[i] = frameArray[frameArray.length - i - 1];
			frameArray[frameArray.length - i - 1] = tmp;
		}
	}

	public static void reverseBottomSpriteArrays(int[][] frameArray) {
		int tmp1;
		for (int[] row: frameArray) {
			for (int i = 0; i < row.length / 2; i++) {
				tmp1 = row[i];
				row[i] = row[row.length - i - 1];
				row[row.length - i - 1] = tmp1;
			}
		}
	}

	public static boolean isLineConfigViable(String line) {
		return !line.equals("") && !(line.startsWith("//") || line.startsWith(" ") || line.startsWith("\t"));
	}

	public static void sleepHandlingInterrupt(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Main.LOGGER.log(EngineLogger.Level.ERROR, "InterruptedException occurred waiting in the main thread", e);
			Thread.currentThread().interrupt();
		}
	}

	public static CompactFrameArray arrayOfArraysToSingleArray(int[][] a) {
		if (a.length == 0) {
			return new CompactFrameArray(new int[0], 0);
		}

		int[] newArray = new int[a.length * a[0].length];
		int dataWidth = a[0].length;
		for (int i = 0; i < a.length; i++) {
			System.arraycopy(a[i], 0, newArray, dataWidth * i, dataWidth);
		}
		return new CompactFrameArray(newArray, dataWidth);
	}

	public static String[] readResourceAsLineArray(String fileName) throws IOException {
		return readResourceToString(fileName).split("\r\n");
	}

	public static String readResourceToString(String fileName) throws IOException {
		try (InputStream stream = Main.class.getResourceAsStream(fileName)) {
			if (stream == null) {
				Main.LOGGER.log(EngineLogger.Level.ERROR, "No such internal resource: \"" + fileName + "\"");
				throw new IOException();
			}
			return readInputStreamToString(stream);
		}
	}

	public static String readInputStreamToString(InputStream stream) throws IOException {
		if (stream == null) {
			Main.LOGGER.log(EngineLogger.Level.ERROR, "Attempt to read string from null stream");
			throw new IOException();
		}
		byte[] allBytes = stream.readAllBytes();

		stream.close();

		StringBuilder builder = new StringBuilder();
		for (byte byt: allBytes) {
			builder.append((char)byt);
		}

		return builder.toString();
	}
}
