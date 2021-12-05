package afyber.shmupfeaturecreep.engine;

import afyber.shmupfeaturecreep.MainClass;

import java.io.IOException;
import java.io.InputStream;

/**
 * A few things that wanted their own class
 *
 * @author afyber
 */
public class GeneralUtil {
	private GeneralUtil() {}

	public static void reverseTopSpriteArray(int[][] byteArray) {
		int[] tmp;
		for (int i = 0; i < byteArray.length / 2; i++) {
			tmp = byteArray[i];
			byteArray[i] = byteArray[byteArray.length - i - 1];
			byteArray[byteArray.length - i - 1] = tmp;
		}
	}

	public static void reverseBottomSpriteArrays(int[][] byteArray) {
		int tmp1;
		for (int[] row: byteArray) {
			for (int i = 0; i < row.length / 2; i++) {
				tmp1 = row[i];
				row[i] = row[row.length - i - 1];
				row[row.length - i - 1] = tmp1;
			}
		}
	}

	public static boolean areRectanglesIntersecting(int c1X, int c1Y, int c2X, int c2Y, int o1X, int o1Y, int o2X, int o2Y) {
		// this works because corner #1 is always the top left and corner #2 is always the bottom right,
		// if that ever becomes not true this function will no longer work
		return !(o1X > c2X || o1Y > c2Y || o2X < c1X || o2Y < c1Y);
	}

	public static CompactFrameArray arrayOfArraysToSingleArray(int[][] a) {
		int[] newArray = new int[a.length * a[0].length];
		int dataWidth = a[0].length;
		for (int i = 0; i < a.length; i++) {
			System.arraycopy(a[i], 0, newArray, dataWidth * i, dataWidth);
		}
		return new CompactFrameArray(newArray, dataWidth);
	}

	public static String readResourceToString(String fileName) throws IOException {
		try (InputStream stream = MainClass.class.getResourceAsStream(fileName)) {
			if (stream == null) {
				return null;
			}
			return readInputStreamToString(stream);
		}
	}

	public static String readInputStreamToString(InputStream stream) throws IOException {
		if (stream == null) {
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
