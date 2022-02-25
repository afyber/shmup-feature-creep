package afyber.shmupfeaturecreep.game;

import java.util.Arrays;
import java.util.Objects;

public record EnemyWaveSlot(double minRating, double maxRating, TagReference[] tags, EnemyTimeSpacePosition[] positions) {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EnemyWaveSlot that = (EnemyWaveSlot)o;
		return Double.compare(that.minRating, minRating) == 0 && Double.compare(that.maxRating, maxRating) == 0 && Arrays.equals(tags, that.tags) && Arrays.equals(positions, that.positions);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(minRating, maxRating);
		result = 31 * result + Arrays.hashCode(tags);
		result = 31 * result + Arrays.hashCode(positions);
		return result;
	}

	@Override
	public String toString() {
		return "EnemyWaveSlot{" +
				"minRating=" + minRating +
				", maxRating=" + maxRating +
				", tags=" + Arrays.toString(tags) +
				", positions=" + Arrays.toString(positions) +
				'}';
	}
}
