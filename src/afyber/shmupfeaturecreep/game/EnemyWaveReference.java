package afyber.shmupfeaturecreep.game;

import java.util.Arrays;
import java.util.Objects;

public record EnemyWaveReference(String objectName, WaveProperties.Stage stage, int batch, double rating, EnemyTag[] enemyTags) {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EnemyWaveReference reference = (EnemyWaveReference)o;
		return batch == reference.batch && Double.compare(reference.rating, rating) == 0 && objectName.equals(reference.objectName) && stage == reference.stage && Arrays.equals(enemyTags, reference.enemyTags);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(objectName, stage, batch, rating);
		result = 31 * result + Arrays.hashCode(enemyTags);
		return result;
	}

	@Override
	public String toString() {
		return "EnemyWaveReference{" +
				"objectName='" + objectName + '\'' +
				", stage=" + stage +
				", batch=" + batch +
				", rating=" + rating +
				", enemyTags=" + Arrays.toString(enemyTags) +
				'}';
	}
}
