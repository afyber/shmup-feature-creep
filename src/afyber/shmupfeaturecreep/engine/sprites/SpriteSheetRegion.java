package afyber.shmupfeaturecreep.engine.sprites;

import java.util.Arrays;
import java.util.Objects;

/**
 * This is only in a separate file because it needs to be accessed by Screen.
 *
 * @author afyber
 */
public record SpriteSheetRegion(byte[][] data, int dataWidth, int dataHeight, int originX, int originY) {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SpriteSheetRegion region = (SpriteSheetRegion)o;
		return Arrays.deepEquals(data, region.data) && dataWidth == region.dataWidth && dataHeight == region.dataHeight;
	}

	@Override
	public int hashCode() {
		int dataHash = Objects.hash(dataWidth) * Objects.hash(dataHeight);
		return 13 * dataHash * Arrays.deepHashCode(data);
	}

	@Override
	public String toString() {
		return "SpriteSheetRegion{" + "data=" + Arrays.deepToString(data) + ", dataWidth=" + dataWidth + ", dataHeight=" + dataHeight + "}";
	}
}
