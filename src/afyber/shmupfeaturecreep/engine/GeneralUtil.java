package afyber.shmupfeaturecreep.engine;

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
			for (int i = 0; i < row.length / 2; i += 4) {
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
}
