package afyber.shmupfeaturecreep.engine;

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
		byte tmp1, tmp2, tmp3, tmp4;
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
}
