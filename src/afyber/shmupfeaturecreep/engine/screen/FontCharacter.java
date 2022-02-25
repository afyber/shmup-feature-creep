package afyber.shmupfeaturecreep.engine.screen;

import java.util.Arrays;
import java.util.Objects;

record FontCharacter(int[][] imageData, int imageWidth, int imageHeight, int xOffs, int yOffs, int nextXOffs) {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FontCharacter that = (FontCharacter)o;
		return imageWidth == that.imageWidth && imageHeight == that.imageHeight && xOffs == that.xOffs && yOffs == that.yOffs && nextXOffs == that.nextXOffs && Arrays.equals(imageData, that.imageData);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(imageWidth, imageHeight, xOffs, yOffs, nextXOffs);
		result = 31 * result + Arrays.hashCode(imageData);
		return result;
	}

	@Override
	public String toString() {
		return "FontCharacter{" +
				"imageData=" + Arrays.toString(imageData) +
				", imageWidth=" + imageWidth +
				", imageHeight=" + imageHeight +
				", xOffs=" + xOffs +
				", yOffs=" + yOffs +
				", nextXOffs=" + nextXOffs +
				'}';
	}
}
