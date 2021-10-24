package afyber.shmupfeaturecreep.engine;

/**
 * A few things that wanted their own class
 *
 * @author afyber
 */
public class GeneralUtil {
	private GeneralUtil() {}

	public static void reverseTopSpriteArray(byte[][] byteArray) {
		byte[] tmp;
		for (int i = 0; i < byteArray.length / 2; i++) {
			tmp = byteArray[i];
			byteArray[i] = byteArray[byteArray.length - i - 1];
			byteArray[byteArray.length - i - 1] = tmp;
		}
	}

	public static void reverseBottomSpriteArrays(byte[][] byteArray) {
		byte tmp1;
		byte tmp2;
		byte tmp3;
		byte tmp4;
		for (byte[] row: byteArray) {
			for (int i = 0; i < row.length / 2; i += 4) {
				// TODO: Someday I'll refactor the code to not require this
				tmp1 = row[i];
				tmp2 = row[i + 1];
				tmp3 = row[i + 2];
				tmp4 = row[i + 3];
				row[i] = row[row.length - i - 4];
				row[i + 1] = row[row.length - i - 3];
				row[i + 2] = row[row.length - i - 2];
				row[i + 3] = row[row.length - i - 1];
				row[row.length - i - 4] = tmp1;
				row[row.length - i - 3] = tmp2;
				row[row.length - i - 2] = tmp3;
				row[row.length - i - 1] = tmp4;
			}
		}
	}

	public static boolean areRectanglesIntersecting(int C1X, int C1Y, int C2X, int C2Y, int O1X, int O1Y, int O2X, int O2Y) {
		// Get obvious situations out of the way quickly (1 is always the top left corner and 2 is the bottom right)
		if (O1X > C2X || O1Y > C2Y || O2X < C1X || O2Y < C1Y) {
			return false;
		}
		/* NOTE: certain conditions have been removed because my sonarlint tells me they are always true, the original
		if statements looked like this:
		if ((C1X >= O1X && C1X <= O2X && C1Y >= O1Y && C1Y <= O2Y) || (C2X >= O1X && C2X <= O2X && C2Y >= O1Y && C2Y <= O2Y)) {
			return true;
		}
		if ((O1X >= C1X && O1X <= C2X && O1Y >= C1Y && O1Y <= C2Y) || (O2X >= C1X && O2X <= C2X && O2Y >= C1Y && O2Y <= C2Y)) {
			return true;
		}
		 */
		if ((C1X >= O1X && C1Y >= O1Y) || (C2X <= O2X && C2Y <= O2Y)) {
			return true;
		}
		if ((O1X >= C1X && O1Y >= C1Y) || (O2X <= C2X && O2Y <= C2Y)) {
			return true;
		}
		return false;
	}
}
