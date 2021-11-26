package afyber.shmupfeaturecreep.engine;

import java.util.Arrays;
import java.util.Objects;

public record CompactFrameArray(int[] a, int dataWidth) {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CompactFrameArray that = (CompactFrameArray)o;
		return dataWidth == that.dataWidth && Arrays.equals(a, that.a);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(dataWidth);
		result = 31 * result + Arrays.hashCode(a);
		return result;
	}

	@Override
	public String toString() {
		return "CompactFrameArray{" +
				"a=" + Arrays.toString(a) +
				", dataWidth=" + dataWidth +
				'}';
	}
}
